package utils

import java.util.Random

object Permutator {
  implicit def wrap[T: Manifest](array: Iterable[T]) = new Permutator[T](array)
}

class Permutator[T: Manifest](val array: Iterable[T]) {

  def randomPermutation: Array[T] = {

    val n = array.size
    val res: Array[T] = array.toArray.clone()

    val r = new Random()

    for(i <- 0 to n-2) {
      val randPos = i + r.nextInt(n-i-1)
      val tmp: T = res(i)

      res(i) = res(randPos)
      res(randPos) = tmp
    }

    res
  }
}
