/** Dada una lista como List(5,3,4,2,8,10,7,0) y un target = 7 Retornar las
  * tuplas que sumadas den el valor del target (5,2)(4,3)(7,0)
  */
def getTuples(list: List[Int], target: Int): List[(Int, Int)] = {
  val map = list
    .filter(_ <= target)
    .foldRight(Map.empty[Int, Int])((key, acc) => acc.updated(key, key))
  map
    .foldRight(List.empty[(Int, Int)]) { (pair, list) =>
      val maybeComplement = map.get(target - pair._1)
      if( maybeComplement.isDefined)
        (pair._1, maybeComplement.get) :: list
      else
        list
    }
}

getTuples(List(5, 3, 4, 2, 8, 10, 7, 0), 7)
