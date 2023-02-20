/*
Functors and Monads
 A type like F[A] with a map method is called a functor. If a functor also has a flatMap method it is called a monad
 The general idea is a monad represents a value in some context. The context depends on the monad we’re using

 */

/**************************
* 5.5.4 Exercises
 **************************/

val list = List(1, 2, 3)
list.flatMap(value => List(value, -value))

