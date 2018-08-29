package main.scala.instances

import breeze.linalg.DenseMatrix
import img.Image


class SegmentationInstance( val name: String,
                            val pixel2sp: DenseMatrix[Int]
) {
  def makeSegmentationRes(spY: Array[Int]): Image = {
    val segm_matrix = DenseMatrix.zeros[Int](pixel2sp.rows, pixel2sp.cols)

    for (i <- Range(0, pixel2sp.rows); j <- Range(0, pixel2sp.cols)) {
      segm_matrix(i, j) = spY(pixel2sp(i, j) - 1)
    }

    // println(name + ": img_loss = " + loss(segm_matrix))

    new Image(segm_matrix)
  }

  def loss(segm_matrix: DenseMatrix[Int]): Double = {
    val ground_img = Image("data/train/" + "imgTrain_" + name + "_groundtruth.png").toBAW()
    val cl2count  = Array(0, 0)

    ground_img.foreachPixel((i, p) => {
      val col = ground_img.matrix(p.x, p.y)
      cl2count(col) += 1
    })

    var res = 0.0
    ground_img.foreachPixel((i, p) => {
      val col = ground_img.matrix(p.x, p.y)
      if (segm_matrix(p.x, p.y) != ground_img.matrix(p.x, p.y)) res += 0.5 / cl2count(col)
    })

    res
  }
}
