/**
 * In previous chapters we saw how functors and monads let us sequence operations using map and flatMap.
 * the problem with map and flatmap make the assumption that each computation is dependent on the previous one
 * because monadic comprehension only allows us to run computation in sequence.
 *
 * We need a weaker construct-one that doesn’t guarantee sequencing—to achieve the result we want.
 * In this chapter we will look at three type classes that support this pattern:
 *
 * Semigroupal:
 *      encompasses the notion of composing pairs of contexts. Cats provides a cats.syntax.apply module that
 *      makes use of Semigroupal and Functor to allow users to sequence functions with multiple arguments.
 *
 * Parallel:
 *      converts types with a Monad instance to a related type with a Semigroupal instance.
 * Applicative:
 *      extends Semigroupal and Functor. It provides a way of applying functions to parameters within a context.
 *      Applicative is the source of the pure method we introduced in Chapter 4.
 */