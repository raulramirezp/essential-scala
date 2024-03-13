/** contravariant functor Provides the contramap function that represents
  * “prepending” an operation to a chain The contramap method only makes sense
  * for data types that represent transformations.
  *
  * we denote contravariant type parameters with a - symbol. For instance, if we
  * have a contravariant type Writer[-A], it means that if A is a subtype of B,
  * then Writer[B] is a subtype of Writer[A]. This behavior allows us to
  * "consume" values of type A and "produce" values of type B.
  */

trait Printable[A] { self =>
  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      def format(value: B): String =
        self.format(func(value))
    }
}

def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

implicit val stringFormat: Printable[String] = (value: String) => value
implicit val booleanFormat: Printable[Boolean] = {
  case true => "yes"
  case false => "no"
}

format("hello")
format(true)

final case class Box[A](value: A)
implicit def boxFmt[T](implicit printable: Printable[T]): Printable[Box[T]] =
  printable.contramap(_.value)

format(Box("hello world"))
format(Box(true))
//format(Box(123))

/** Generally speaking, if you have some context F[A] for type A, and you can
  * get an A value out of a B value — Contravariant allows you to get the F[B]
  * context for B.
  * @param fa
  *   is the context F[A]
  * @param fn
  *   is the function to get A value from B
  * @tparam A
  * @tparam B
  * @return
  *   the F[B] context for B
  */
def miContramap[A, B](fa: Printable[A])(fn: B => A): Printable[B] =
  (value: B) => fa.format(fn(value))

format(Box(123))(
  miContramap[Int, Box[Int]]((value: Int) => value.toString)(_.value)
)

/** Invariant functors and the imap method. imap that is informally equivalent
  * to a combination of map and contramap
  */

trait Codec[A] { self =>
  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] =
    new Codec[B] {
      override def encode(value: B) = self.encode(enc(value))

      override def decode(value: String) = dec(self.decode(value))
    }
}
def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

implicit val stringCodec: Codec[String] =
  new Codec[String] {
    def encode(value: String): String = value
    def decode(value: String): String = value
  }

implicit val intCodec: Codec[Int] =
  stringCodec.imap(_.toInt, _.toString)

implicit val booleanCodec: Codec[Boolean] =
  stringCodec.imap(_.toBoolean, _.toString)

implicit val doubleCodec: Codec[Double] =
  stringCodec.imap(_.toDouble, _.toString)

implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] =
  codec.imap(Box(_), _.value)

encode(123)
decode[Int]("123")

encode(true)
decode[Boolean]("true")

encode(123.21)
decode[Double]("123.21")

encode(Box(123))
decode[Box[String]]("123")

encode(Box(123.13))
decode[Box[Double]]("123.4")

/** Contravariant in Cats
  */
import cats.{Contravariant, Show}

val showString = Show[String]
val showSymbol: Show[Symbol] =
  Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")
showSymbol.show(Symbol("raul"))

import cats.syntax.contravariant._
showString
  .contramap[Symbol](sym => s"'${sym.name}")
  .show(Symbol("Marcela"))

/** Invariant in cats
  */
import cats.Monoid
import cats.Invariant
import cats.syntax.semigroup._
import cats.implicits._ // using syntax for invariant

// we want Monoid[Symbol]
/*implicit val symbolMonoid: Monoid[Symbol] =
    Invariant.catsInvariantMonoid
      .imap[String, Symbol](Monoid[String])(Symbol(_))(_.name)*/

implicit val symbolMonoid: Monoid[Symbol] =
  Monoid[String].imap(Symbol(_))(_.name)

Monoid[Symbol].empty
Symbol("a") |+| Symbol("new") |+| Symbol("words")
