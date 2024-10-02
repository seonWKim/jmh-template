package com.example.benchmark

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.jodah.expiringmap.ExpiringMap
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@State(Scope.Benchmark)
open class ProbabilityEarlyExpirationTest {

    private val expiringMap = ExpiringMap.builder()
        .maxSize(1000)
        .expiration(5 * 1000, TimeUnit.MILLISECONDS)
        .build<String, String>()
    private val earlyExpirationWindowMillis = 500.0
    private val computeKeyCallCount = AtomicInteger(0)

    @Setup
    fun setUp() {
        for (i in 0..1000) {
            expiringMap.put("key$i", "value$i")
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(time = 2, timeUnit = TimeUnit.MINUTES)
    fun checkWithProbabilityEarlyExpiration() {
        runBlocking(Dispatchers.IO) {
            (0..10_000_000).map { i ->
                async {
                    val key = "key${i % 10}"
                    if (shouldExpire(key)) {
                        expiringMap[key] = computeKey(key)
                    }

                    delay(100)
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(time = 2, timeUnit = TimeUnit.MINUTES)
    fun checkWithoutProbabilityEarlyExpiration() {
        runBlocking(Dispatchers.IO) {
            (0..10_000_000).map { i ->
                async {
                    val key = "key${i % 10}"
                    if (!expiringMap.containsKey(key)) {
                        expiringMap[key] = computeKey(key)
                    }

                    delay(100)
                }
            }
        }
    }

    private fun shouldExpire(key: String): Boolean {
        val expirationLeft = kotlin.runCatching {
            expiringMap.getExpectedExpiration(key)
        }.getOrElse { Long.MIN_VALUE }

        if (expirationLeft <= 0) {
            return true
        }

        return if (expirationLeft > earlyExpirationWindowMillis) {
            false
        } else {
            Math.random() < 1 - (expirationLeft.toDouble() / earlyExpirationWindowMillis)
        }
    }

    private suspend fun computeKey(key: String): String {
        computeKeyCallCount.incrementAndGet()
        delay(2500)
        return "key:$key"
    }

    @TearDown
    fun tearDown() {
        println("computeKey was called ${computeKeyCallCount.get()} times")
    }
}
