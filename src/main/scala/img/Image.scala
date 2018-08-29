package img

import breeze.linalg._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.Color
import util.Random
import utils.NToN2Projector



class Image(val matrix: DenseMatrix[Int], val path: Option[String] = None) {
  def save(path: String) {

    val results = new BufferedImage(matrix.rows, matrix.cols, BufferedImage.TYPE_INT_RGB);

    for (i <- Range(0, matrix.rows); j <- Range(0, matrix.cols)) {
      val rgb = matrix(i, j)
      val r = ((rgb&0x00FF0000)>>>16).toInt; // Red level
      val g = ((rgb&0x0000FF00)>>>8).toInt;  // Green level
      val b = (rgb&0x000000FF).toInt;       // Blue level

      val color = new Color(r,g,b);

      results.setRGB(i, j, color.getRGB());
    }

    ImageIO.write(results, "PNG", new File(path));
  }

  def saveBAW(path: String) {
    val results = new BufferedImage(matrix.rows, matrix.cols, BufferedImage.TYPE_INT_RGB);

    for (i <- Range(0, matrix.rows); j <- Range(0, matrix.cols)) {
      val rgb = matrix(i, j)

      val color =
        if (rgb == 0)
          new Color(0, 0, 0);
        else
          new Color(255, 255, 255);

      results.setRGB(i, j, color.getRGB());
    }

    ImageIO.write(results, "PNG", new File(path));
  }

  def movePixels(movementsVector: DenseVector[Int]): Image = {
    movePixels((i, j) => {
      val index = vectorIndex(N2Point(i, j))
      movementsVector(index)
    })
  }

  def movePixels(movementsMatrix: DenseMatrix[Int]): Image = {
    movePixels((i, j) => movementsMatrix(i, j))
  }

  def movePixels(f: (Int, Int) => Int ): Image = {

    val new_matrix = DenseMatrix.zeros[Int](matrix.rows, matrix.cols)

    for (i <- Range(0, matrix.rows); j <- Range(0, matrix.cols)) {
      val new_i = i + f(i, j)
      if (new_i >= 0 && new_i < matrix.rows) {
        new_matrix(new_i, j) = matrix(i, j)
      }
    }

    new Image(new_matrix)
  }

  def smoose(): Image = {
    for (i <- Range(0, matrix.rows); j <- Range(0, matrix.cols)) {
        if (matrix(i, j) == 0) {
          matrix(i, j) = ColorDifference.average(Array(
            matrix(math.max(i-1, 0), j),
            matrix(math.min(i+1, matrix.rows - 1), j),
            matrix(i, math.max(j-1, 0)),
            matrix(i, math.min(j+1, matrix.cols - 1))
          ))
        }
    }
    this
  }

  def getRGB(p: N2Point, dx: Int = 0) = matrix(p.x + dx, p.y)



  def detColorDifference(p1: N2Point, p2: N2Point): Double = {
    ColorDifference.findDifference(getRGB(p1), getRGB(p2))
  }

  def vectorIndex(p: N2Point) = p.y * matrix.rows + p.x

  def foreachPixel(f: (Int, N2Point) => Unit) {

    var index: Int = 0
    for (j <- Range(0, matrix.cols)) {
      for (i <- Range(0, matrix.rows)) {
        val p = N2Point(i, j)
        f(index, p)
        assert(index == vectorIndex(p))
        index += 1
      }
    }
  }

  def vectorToMatrix(vector: DenseVector[Int]): DenseMatrix[Int] = {
    val new_matrix = DenseMatrix.zeros[Int](matrix.rows, matrix.cols)

    foreachPixel((index, p) => {
      val i = index % matrix.rows
      val j = index / matrix.rows
      new_matrix(i, j) = vector(index)
    })

    new_matrix
  }

  def getNToN2Projector: NToN2Projector = new NToN2Projector(matrix.rows, matrix.cols)

  def toBAW(): Image = {
    val baw_matrix: DenseMatrix[Int] = DenseMatrix.zeros[Int](matrix.rows, matrix.cols)

    foreachPixel((index, p) => {
      baw_matrix(p.x, p.y) = ColorDifference.toBAW(matrix(p.x, p.y))
    })

    new Image(baw_matrix)
  }
}


object Image {

  var scale = 1

  def apply(path: String): Image = {


    val input: BufferedImage = ImageIO.read(new File(path))
//    val scaler = new ImageScaler(input1)
//    scaler.createScaledImage(input1.getWidth() / scale, input1.getHeight() / scale, ImageScaler.ScaleType.FILL)
//    val input = scaler.getResult

    val matrix: DenseMatrix[Int] = DenseMatrix.zeros(input.getWidth, input.getHeight)

    for (i <- Range(0, input.getWidth); j <- Range(0, input.getHeight)) {
      matrix(i, j) = input.getRGB(i, j)
    }

    new Image(matrix, Some(path))
  }

}


