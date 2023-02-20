/**
 * Variance annotations allow us to control sub-class relationships between types with type parameters.
 */

sealed trait Maybe[+A]
final case class Full[A](value: A) extends Maybe[A]
case object Empty extends Maybe[Nothing]

/*
If Maybe is invariant, the next line fails because Maybe[Noting] is not a subtype of Maybe[Int] and
in invariant case Maybe[Int] and Maybe[Noting] are unrelated
 */
val possible: Maybe[Int] = Empty

sealed trait Life

case class Human() extends Life
sealed trait Animal extends Life{
  def info: String = "I'm an Animal"
}
case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal
case class Crocodile(name: String) extends Animal

/**************************************************************************************************************
 *                                      Invariance
 * A type Foo[T] is invariant in terms of T, meaning that the types Foo[A] and Foo[B] are unrelated
 * regardless of the relationship between A and B. This is the default variance of any generic type in Scala.
 *************************************************************************************************************/
class MyInvariantList[T]
val animal: Animal = Dog("laika")
//val invalidTypes: MyInvariantList[Animal] = new MyInvariantList[Dog]
/**************************************************************************************************************
 *                                      Covariance
 * A type Foo[+T] is covariant in terms ot T, meaning that Foo[A] is a supertype of Foo[B] if A is a supertype of B.
 * Most Scala collections classes are covariant in terms of their contents
 *************************************************************************************************************/
class MyCovariantList[+T]{
  def add[S >: T](element: S): MyCovariantList[S] = ???
}
val animal: Animal = Dog("Laika")
val animals: MyCovariantList[Animal] = new MyCovariantList[Dog]
//animals.add(Human())

/**************************************************************************************************************
 *                                      Contravariance
 * A type Foo[-T] is contravariant in terms of T, meaning that Foo[A] is a subtype of Foo[B] if A is a supertype of B.
 * This is used in the Functions arguments
 *************************************************************************************************************/
class MyContravariantList[-T]{
  def add(element: T): MyContravariantList[T] = ???
}
val animals: MyContravariantList[Dog] = new MyContravariantList[Animal]
//animals.add(Dog("Kosito"))

/**
The Liskov Substitution Principle
The following principle, stated by Barbara Liskov, tells us when type can be a subtype of another.
If A <: B, then everything one can to do with a value of type B one should also be able to do with a value of type A.

The actual definition Liskov used is a bit more formal. It says:
Let q(x) be a property provable about objects x of type B.
Then q(y) should be provable for objects y of type A where
A <: B.
 */
val a: AnyVal = 5
val listIsCovariant: List[AnyVal] = List(1,2,3)
val list2: List[Int] = List[Nothing]()

/**
 * Given that NonEmpty <: IntSet
Say you have two function types:
type A = IntSet => NonEmpty
type B = NonEmpty => IntSet
According to the Liskov Substitution Principle: A <: B

Generally, we have the following rule for subtyping between function
types:
If A2 <: A1 and B1 <: B2, then
A1 => B1 <: A2 => B2

 FUNCTIONS in general are contravariant in their parameters and covariant in their result type
Taken from chatGPT:
 "Overall, this combination of contravariant function parameters and covariant function response types ensures
 that we can substitute more specific types for more general types without violating the Substitution Principle."

That means that if we have a function, for example:
  def add(fn: A2 => B2)
The LSP says that we should be able to replace the Supertype with the Subtype without change the program behaviour,
that means that we can do:
  add(A1 => B1) where A1 => B1 should be <: A2 => B2
 */

case class Box[+A](value: A) {
  /** Apply `func` to `value`, returning a `Box` of the result. */
  def map[B](func: Function1[A, B]): Box[B] =
    Box(func(value))

  def fun[S >: A](element: S): Box[S] = Box(element)
}


val box = Box[Dog](Dog("Mora"))
box.map[Any]( (a: Animal) => a.info)
// A => B
// A => subtype of B Ok
// Supertype of A => B Ok

// 5.6.4.1 Exercise: Covariant Sum
/*
 f1 = A1 => B1
 f2 = A2 => B2
 Then f1 <: f2 if A1 >: A2 and B1 <: B2
 f1 = B => Sum[A,U]
 f2 = BB (BB <: B) => SS (SS >: Sum[A,U])
 */

sealed trait Sum[+A, +B]{
  def flatMap[U, AA >: A](fn: B => Sum[AA,U]): Sum[AA,U] = this match {
    case Success(value) => fn(value)
    case Failure(error) => Failure(error)
  }

}
case class Failure[A](error: A) extends Sum[A,Nothing]
case class Success[B](value: B) extends Sum[Nothing,B]

val listTest: List[Int] = List(1,2,3,4)
listTest.appended("I'm a subtype of Any")

val listAny: List[AnyVal] = listTest
listAny.appended(1)
listAny.appended(null)
listAny.appended(new Object())

val list1: List[Animal] = List[Dog](Dog("Moris"))
list1.appended(Cat("Mauricio"))

/**
 * Contravariant Position Pattern
If A of a covariant type T and a method f of A complains that T is used in a contravariant position,
introduce a type TT >: T in f.

case class A[+T]() {
  def f[TT >: T](t: TT): A[TT] = ???
}
 */

// 5.6.6 Exercises

/**
 * Siamese <: Cat <: Animal; and
 * Purr <: CatSound <: Sound

def groom(groomer: Cat => CatSound): CatSound = {
  val oswald = Cat("Black", "Cat food")
  groomer(oswald)
}

Animal => Purr

 which of the following can I pass to groom?
• A function of type Animal=>Purr  : This because Animal => Purr is <: Cat => CatSound
• A function of type Siamese=>Purr
• A function of type Animal=>Sound
 */




