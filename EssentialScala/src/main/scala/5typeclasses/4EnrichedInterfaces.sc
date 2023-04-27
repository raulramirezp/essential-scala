def numberOfVowels(str: String) =
  str.count(Seq('a', 'e', 'i', 'o', 'u').contains(_))

numberOfVowels("the quick brown fox")

implicit class ExtraStringMethods(str: String) {
  val vowels = Seq('a', 'e', 'i' ,'o', 'u')

  def numberOfVowels() =
    str.count(vowels contains _)
}

"the quick brown fox".numberOfVowels()

/**
 * When the compiler processes our call to numberOfVowels, it interprets it as a type error because there
 * is no such method in String. Rather than give up, the compiler attempts to fix the error by searching
 * for an implicit class that provides the method and can be constructed from a String. It finds ExtraStringMethods.
 * The compiler then inserts an invisible constructor call, and our code type checks correctly.
 *
 * Only a single implicit class will be used to resolve a type error.
 * The compiler will not look to construct a chain of implicit classes to access the desired method.
 */
