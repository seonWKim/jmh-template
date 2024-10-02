# Running benchmark using Kotlin 

## How to run

- Use java17 
- Benchmark classes should not be FINAL!! Add `open` keyword on kotlin classes 

```shell
./gradlew jmh 
```

Running specific benchmark class 
```shell 
./gradlew jmh -PjmhIncludes=<className> 
```

## Concepts  

- Blackhole 
  - utility used to prevent optimizations by the JIT compiler, particularly dead code elimination 
  - blackhole consumes a value in a way that ensures the computation is not optimized away by the JIT compiler, while still not performing any actual operation on the value 
  
             
## JMH benchmarking  
- JMH handles the intricacies of the Java runtime, including JIT compilation and warm-up, to provide accurate and reliable benchmarking results 
- Supports 
  - Control over JVM optimizations e.g. method inlining and loop unrolling,  
  - Benchmark modes e.g. time, throughput, sample time ...  
  - Multithreaded benchmarking 
  - Parameterization 
  - Result profiling 
  
How to interpret results?
```shell
Benchmark                     Mode  Cnt  Score   Error  Units
MyFirstBenchmark.testMethod  avgt   20  123.456 ± 10.123  ns/op
```
- Mode: benchmark mode, `avgt` in this case mean average time taken
  - `avgt`(average time): average execution time 
  - `thrpt`(throughput): how many operations were performed in a unit of time 
  - `sample`(sample time): records the time for each operation 
  - `ss`(single shot time): measures the time for a single operation, useful for cold-start measurements 
  - `all`: runs all of the above methods 
- Cnt: number of benchmark iterations 
- Score: primary result of the benchmark
- Error: error margin of the score 
                  
- Analyzing results 
  - Compare with baseline 
  - Look for anomalies 
  - Consider the big picture 
  - Reproducibility 
