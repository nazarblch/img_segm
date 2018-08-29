package img

import graphical_model.factors.{PottsMetric, MetricBinaryFactor, TIntUnaryFactor, TIntIntBinaryFactor}


trait NeighboursManager {

  var image: Image = _

  def setImage(image: Image) {
    this.image = image
  }

  def getNeighbourPoints(p: N2Point): scala.Vector[N2Point]

  def getEdgeFactor(p1: N2Point, p2: N2Point): TIntIntBinaryFactor
}

class ScanlineNeighboursManager(val coef: Double, val metric: TIntUnaryFactor) extends NeighboursManager {

  def getNeighbourPoints(p: N2Point): Vector[N2Point] = {
    val vb = scala.Vector.newBuilder[N2Point]
    if (p.x + 1 < image.matrix.rows) vb += N2Point(p.x + 1, p.y)
    if (p.x - 1 >= 0) vb += N2Point(p.x - 1, p.y)

    vb.result()
  }

  def getEdgeFactor(p1: N2Point, p2: N2Point): TIntIntBinaryFactor = {
    val diff = image.detColorDifference(p1, p2) + 0.001
    new MetricBinaryFactor(coef * diff, metric)
  }
}


class SquareNeighboursManager(val horiz_coef: Double, val vert_coef: Double, val metric: TIntUnaryFactor) extends NeighboursManager {

  def getNeighbourPoints(p: N2Point): Vector[N2Point] = {
    val vb = scala.Vector.newBuilder[N2Point]
    if (p.x + 1 < image.matrix.rows) vb += N2Point(p.x + 1, p.y)
    if (p.x - 1 >= 0) vb += N2Point(p.x - 1, p.y)
    if (p.y + 1 < image.matrix.cols) vb += N2Point(p.x, p.y + 1)
    if (p.y - 1 >= 0) vb += N2Point(p.x, p.y - 1)

    vb.result()
  }

  def getEdgeFactor(p1: N2Point, p2: N2Point): TIntIntBinaryFactor = {
    val diff = image.detColorDifference(p1, p2) + 0.001
    val coef = if (p1.y == p2.y) horiz_coef else vert_coef

    new MetricBinaryFactor(coef * diff, metric)
  }
}
