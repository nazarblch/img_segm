package main.scala.utils

import breeze.linalg._
import breeze.numerics._

object MatrixManager {

  def loadFromFile(path: String): DenseMatrix[Int] = {
    val rows = scala.Vector.newBuilder[DenseVector[Int]]
    var width: Int = 0
    var height: Int = 0

    for (line <- io.Source.fromFile(path).getLines()) {
      val row = line.trim.split(",").map(_.toInt)
      if (width == 0) width = row.length
      else if (width != row.length) throw new Exception("rows have different length")

      rows += DenseVector[Int](row)
      height += 1
    }

    val res: DenseMatrix[Int] = DenseMatrix.zeros[Int](width, height)

    var i: Int = 0
    for (row <- rows.result()) {
      res(::, i) := row
      i += 1
    }

    res
  }


  def loadFromFileDouble(path: String): DenseMatrix[Double] = {
    val rows = scala.Vector.newBuilder[DenseVector[Double]]
    var width: Int = 0
    var height: Int = 0

    for (line <- io.Source.fromFile(path).getLines()) {
      val row = line.trim.split(",").map(_.toDouble)
      if (width == 0) width = row.length
      else if (width != row.length) throw new Exception("rows have different length")

      rows += DenseVector[Double](row)
      height += 1
    }

    val res: DenseMatrix[Double] = DenseMatrix.zeros[Double](width, height)

    var i: Int = 0
    for (row <- rows.result()) {
      res(::, i) := row
      i += 1
    }

    res
  }

}
