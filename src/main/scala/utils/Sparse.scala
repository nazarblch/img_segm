package main.scala.utils

import breeze.linalg.SparseVector

object Sparse {
  implicit def init(array: Array[Double]) = new Sparse(array)
}

class Sparse(val array: Array[Double]) {

  def toSparse: SparseVector[Double] = {
    val tmp = array.zipWithIndex.filter(p => math.abs(p._1) > 0.00001)
    new SparseVector(tmp.map(_._2), tmp.map(_._1), tmp.length, array.length)
  }
}
