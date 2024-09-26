# Running benchmark using Kotlin 

## How to run
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
