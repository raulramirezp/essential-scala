// Sequence
val list = Seq(1,2,3)
val listSortedDes = list.sortWith(_ > _)
4 +: listSortedDes :+ 0
Seq(1,2) ++ Seq(3,4)
// List
List(1,2) :: 3 :: Nil
1 :: List(3,4)
List(1,2) ::: List(3,4)

val emptyList = Nil
val list2 = 1 :: 2 :: 3 :: 4 :: Nil

val animals = Seq("cat", "dog", "penguin")
"mouse" +: animals :+ "tyrannosaurus"
1 +: animals

val seq1 = Seq(1,2,3)
for {
  num <- seq1
} yield num * 2

val data = Seq(Seq(1), Seq(2, 3), Seq(4, 5, 6))
for {
  seq <- data
  value <- seq
} yield value * 2

for {
  seq <- Seq(Seq(1), Seq(2, 3))
  elt <- seq
} println(elt * 2)

for(number <- 0 to 10) println(number)

def addOptions(op1: Option[Int], op2: Option[Int]): Option[Int] =
  for {
    num1 <- op1
    num2 <- op2
  } yield num1 + num2