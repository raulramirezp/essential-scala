/**
 * contravariant functor
 * Provides the contramap function that represents “prepending” an operation to a chain
 * The contramap method only makes sense for data types that represent transformations.
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
implicit def boxFmt[T](implicit printable: Printable[T]): Printable[Box[T]] = printable.contramap(_.value)

format(Box("hello world"))
format(Box(true))
//format(Box(123))


/**
 * Generally speaking, if you have some context F[A] for type A, and you can get an A value out of a B value —
 * Contravariant allows you to get the F[B] context for B.
 * @param fa is the context F[A]
 * @param fn is the function to get A value from B
 * @tparam A
 * @tparam B
 * @return the F[B] context for B
 */
def miContramap[A,B](fa: Printable[A])(fn: B => A): Printable[B] =
  (value: B) => fa.format(fn(value))

format(Box(123))(miContramap[Int, Box[Int]]((value: Int) => value.toString)(_.value))

/**
 * Invariant functors and the imap methodInvariant functors and the imap method
 */