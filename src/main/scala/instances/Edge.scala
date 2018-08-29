package main.scala.instances

class Edge(val from: Int, val to: Int, val w: Array[Double]) {
  def invert() = new Edge(to, from, w)
}

object Edge {
  def apply(str: String) = {
    val arr = str.trim.split(Array(',', ' ', '\t'))
    new Edge(arr(0).toInt, arr(1).toInt, arr.drop(2).map(_.toDouble))
  }
}
