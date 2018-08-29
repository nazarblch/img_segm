package main.scala.utils

import breeze.linalg.SparseVector

object Extender {
  implicit def initarr(array: SparseVector[Double]) = new Extender(array)
}

class Extender(val array: SparseVector[Double]) {

  def add(w: Double): SparseVector[Double] = {
    val res =  new SparseVector(array.index.map(_ + 1), array.data, array.activeSize, array.length + 1)
    res.update(0, w)
    res
  }

  def add(w: Array[Double]): SparseVector[Double] = {
    val res =  new SparseVector(array.index.map(_ + w.length), array.data, array.activeSize, array.length + w.length)
    Range(0, w.length).foreach(i => res.update(i, w(i)))
    res
  }
}
