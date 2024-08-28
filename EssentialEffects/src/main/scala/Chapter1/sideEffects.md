In functional programming, **side effects** are operations or interactions that go beyond the computation of a result and interact with the outside world or the program's state in a way that breaks the **substitution model**.

### Definition of Side Effects

A **side effect** is any observable effect of a function other than producing a value. These effects can include:

- **Modifying a variable** or object outside its local scope.
- **Changing the state** of the system (e.g., writing to a file, updating a database).
- **Interacting with I/O** (e.g., reading user input, printing to the console).
- **Throwing an exception** or altering the flow of execution in an unexpected way.

### The Substitution Model

The substitution model is a principle where you can replace a function call with its output value without changing the program's behavior. Pure functions, which are core to functional programming, adhere strictly to this model:

- Given the same input, a pure function will always produce the same output.
- A pure function does not have any side effects.

When a function has side effects, you can no longer replace the function call with its output and expect the program to behave identically. This is because the function does more than just compute a value; it also interacts with or changes the environment, which can affect the program's state or the world outside the program.

### Example

Consider the following function:

```scala
def add(a: Int, b: Int): Int = a + b
```

This is a pure function. You can replace `add(2, 3)` with `5` anywhere in your program, and nothing will change.

Now consider this function:

```scala
def addAndPrint(a: Int, b: Int): Int = {
  val result = a + b
  println(s"The result is $result")
  result
}
```

This function has a side effect: it prints to the console. Replacing `addAndPrint(2, 3)` with `5` would change the behavior of the program because the printing would not happen.

### Summary

So yes, in functional programming, a **side effect** is any interaction with the environment or outside world that breaks the substitution model. The presence of side effects means that a function does more than just compute and return a value—it affects the external state, making it less predictable and harder to reason about.