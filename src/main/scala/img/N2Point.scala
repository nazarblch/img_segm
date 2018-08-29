package img

class N2Point(val x: Int, val y: Int) {
  def getX = x
}

object N2Point {
  def apply(x: Int, y: Int) = new N2Point(x,y)
}
