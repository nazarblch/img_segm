package utils

object Normalizer {
  implicit def wrapp(array: Array[Double]) = new Normalizer(array)
}

class Normalizer(val array: Array[Double]) {

  def normalize(): Array[Double] = {
    val norm = array.sum
    array.map(_ / norm)
  }
}
