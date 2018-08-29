package main.scala.instances

import breeze.linalg.{SparseVector, DenseVector}
import main.scala.utils.Sparse._


class Instance(private val x: SparseVector[Double],
               var y: Option[Int],
               var max_pxCount: Option[Int] = None,
               var min_pxCount: Option[Int] = None) {

  def this(arr: Array[Double]) = this(arr.toSparse, None)
  def this(str: String, sep: String = ",") = this(str.trim.split(",").map(_.toDouble).toSparse, None)
  def this(i: Instance, y: Int) = this(i.x, Some(y))

  override def toString = x.toArray.mkString(",") + (if (y.isDefined) "," + y else "")

  def getSparseX: SparseVector[Double] = {
    x
  }

  def getArray = x.toArray
}
