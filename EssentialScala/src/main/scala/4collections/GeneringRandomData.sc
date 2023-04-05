val subjects = List("Noel", "The cat", "The dog")
val verbs = List("wrote", "chased", "slept on")
val verbs2 = List("wrote", "chased", "slept on", "meowed at", "barked at")
val objects = List("the book", "the ball", "the bed")

for (
  subject <- subjects;
  verb <- verbs;
  obj <- objects
)
  println(s"${subject} ${verb} ${obj}")


for {
  subject <- subjects
  verbs = subject match {
    case "The cat" => List("meowed at", "chased", "slept on")
    case "The dog" => List("barked at", "chased", "slept on")
    case _ => List("wrote", "chased", "slept on")
  }
  verb <- verbs
  objects = verb match {
    case "wrote" => List("the book", "the letter", "the code")
    case "chased" => List("the ball", "the dog", "the cat")
    case "slept on" => List("the bed", "the mat", "the train")
    case "meowed at" => List("Noel", "the door", "the food cupboard")
    case "barked at" => List("the postman", "the car", "the cat")
  }
  obj <- objects
} println(s"${subject} ${verb} ${obj}")
