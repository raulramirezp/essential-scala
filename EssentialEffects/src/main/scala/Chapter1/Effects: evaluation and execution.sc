/**
 * To explore what effects are, and how we can leverage them,
 * we’ll distinguish two aspects of code: computing values and interacting with the environment.
 * At the same time, we’ll talk about how transparent, or not, our code can be in describing these aspects,
 * and what we as programmers can do about it.
 */

/**
 *  The substitution model of evaluation
 *  Let’s start with the first aspect, computing values.
 *  As programmers we write some code, say a method, and it computes a value that gets returned to the
 *  caller of that method:
 */
def plusOne(i: Int): Int =
  return i +1;

val x = plusOne(plusOne(12))

/**
 * Let’s use substitution to evaluate this code.
 * 1. val x = plusOne(12 + 1)
 * 2. val x = plusOne(13)
 * 3. val x = 13 + 1
 * 4. val x = 14
 *
 * a) There are no references to anything outside of it. This is sometimes referred to as local reasoning.
 * b) Under substitution, programs mean the same thing if they evaluate to the same value.
 *    13 + 1 means exactly the same thing as 14. So does plusOne(12 + 1), or even (12 + 1) + 1.
 *    This is known as referential transparency.
 */