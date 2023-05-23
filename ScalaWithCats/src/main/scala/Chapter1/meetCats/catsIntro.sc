import Chapter1.WorkingWithImplicits.Cat
import cats.Show
import cats.syntax.show.toShow
val showInt: Show[Int] = Show.apply[Int]
import java.util.Date

showInt.show(123)

456.show
789.typeClassInstance
789.self

// Creating instances of show
// Creating a new type class instance manually
/*implicit val dateShow: Show[Date] =
  new Show[Date] {
    def show(date: Date): String =
      s"${date.getTime}ms since the epoch."
  }*/

//Using the cats methods to create a type class instance
//implicit val dateShow: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
implicit val dateShow: Show[Date] = Show.fromToString
new Date().show

val mauricio: Cat = Cat("Mauricio", 2, "grey and white")
val cat = Cat("Garfield", 41, "ginger and black")

implicit val catShow: Show[Cat] =
  Show((cat: Cat) => s"${cat.name} is a ${cat.age} year-old ${cat.color} cat")

cat.show
println(mauricio.show)


// Eq
import cats.Eq
import cats.syntax.eq._
import cats.syntax.option._

Eq[Int].eqv(123, 123)
implicitly(Eq[Int])

123 === 123

Option(1) === Option.empty[Int]
1.some =!= none[Int]

implicit val dateEq: Eq[Date] = Eq.instance((date1, date2) =>
  date1.getTime === date2.getTime)

val x = new Date() // now
val y = new Date()

x === x
x === y

// Exercise
implicit val catEq: Eq[Cat] = Eq.instance{(cat1, cat2) =>
  cat1.name === cat2.name &&
    cat1.age == cat2.age &&
    cat1.color === cat2.color
}

val cat1 = Cat("Garfield",   38, "orange and black")
val cat2 = Cat("Heathcliff", 33, "orange and black")
val optionCat1 = Option(cat1)
val optionCat2 = Option.empty[Cat]

cat1 === cat2
optionCat1 === optionCat2