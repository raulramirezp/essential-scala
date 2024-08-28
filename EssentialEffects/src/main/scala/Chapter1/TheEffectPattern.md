# The Effect Pattern
If we impose some conditions, we can tame the side effects into something safer; we’ll call these effects

```
Effect Pattern Checklist
1. Does the type of the program tell us
   a. what kind of effects the program will perform; and 
   b. what type of value it will produce?
2. When externally-visible side effects are required, is the effect description separate from the execution?
```
### Is Future an effect?

Future is known to have issues that aren’t easily seen. For example, look at this code, 
where we reference the same Future to run it twice:

```
val print = Future(println("Hello World!")) 
val twice = print.flatMap(_ => print)
```
What output is produced?
```
Hello World!
```
It is only printed once! Why is that? 

The reason is that the Future is scheduled to be run immediately upon construction.

### Compare this to what happens when we substitute the definition of print into twice:

1. 
  ```
val print = Future(println("Hello World!"))
val twice = Future(println("Hello World!")).flatMap(_ => print)
  ```
2. 
  ```
val print = Future(println("Hello World!"))
val twice = 
    Future(println("Hello World!"))
     .flatMap(_ => Future(println("Hello World!")))
```
Running it, we then see:
```
Hello World!
Hello World!
```

## Effect Pattern Checklist: Future[A]
```
1. Does the type of the program tell us
    a. what kind of effects the program will perform; and
        ✅ A Future represents an asynchronous computation.
    b. what type of value it will produce?
       ✅  A value of type A, if the asynchronous computation is successful.
2. When externally-visible side effects are required, is the effect description
   separate from the execution?
  
   ✅ Externally-visible side effects are required: the body of a Future
      can do anything, including side effects.
   
   ❌ But those side effects are not executed after the description of composed operations; 
      the execution is scheduled immediately upon construction.
   
Therefore, Future does not separate effect description from execution: it is unsafe.
```