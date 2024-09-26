package com.example.benchmark

import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
open class HelloWorldTest {

    @Setup
    fun setUp() {
        runBlocking {
            println("set up")
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    fun check(blackhole: Blackhole) {
        blackhole.consume(println("hello world1"))
    }
}
