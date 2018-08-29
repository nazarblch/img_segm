package graphical_model.optimizer.alpha_extention

import graphical_model.factor_graph.FactorGraph
import breeze.linalg.DenseVector
import mincut.{BidirEdge, Tweight, MincutJNI}
import java.util.Random
import scala.Array


class AlphaGraph(val factorGraph: FactorGraph,
                 val in_edges: Vector[AlphaEdge],
                 val src_edges: Vector[Double],
                 val sink_edges: Vector[Double],
                 val alpha: Int ) {

  val src_index = factorGraph.size
  val sink_index = factorGraph.size + 1
  final var min_cat_value: Double = _

  def printCutSize(partition: Array[Int]): Double = {

    val z = partition ++ Array(0, 1)

    var cut_size = 0.0

    in_edges.foreach(e => {
      if (z(e.from) != z(e.to) && (z(e.to) == 1)) cut_size += e.weight
    })

    for (i <- Range(0, factorGraph.size)) {
      if (z(src_index) != z(i) && (z(i) == 1)) cut_size += src_edges(i)
      if (z(i) != z(sink_index)) cut_size += sink_edges(i)
    }

    assert(math.abs(min_cat_value - cut_size) < 0.00001)

    println("cut size = " + cut_size)

    cut_size
  }



  def getSrcSinkWeightsPerNode(): Vector[Tweight] = {

    val vb = Vector.newBuilder[Tweight]

    for (id <- Range(0, factorGraph.size)) {
      vb += new Tweight(id, src_edges(id), sink_edges(id))
    }

    vb.result()
  }

  def getInternalEdges(): Vector[BidirEdge] = {

    in_edges.map(e => {
      if (e.from < e.to) {
        new BidirEdge(e.from, e.to, e.weight, 0.0)
      } else {
        new BidirEdge(e.to, e.from, 0.0, e.weight)
      }
    })
//    .groupBy(_._1).toVector.map({
//      case (e, seq) => new BidirEdge(e._1,
//        e._2,
//        seq.map(_._2).max,
//        seq.map(_._3).max)
//    })

  }

  def minCutJNI() : (Array[Int], Double) = {

    println("src cut: " + src_edges.sum)
    println("sink cut: " + sink_edges.sum)

    val tweights = getSrcSinkWeightsPerNode()

    val rest_edges = getInternalEdges()

    val minCutAlg = new MincutJNI();

    val z = minCutAlg.minCut(rest_edges.toArray, tweights.toArray, factorGraph.size)

    println("1 fraction: " + (z.filter(_ == 1).size.toDouble / z.size) )

    (z, minCutAlg.minCut)
  }



  def findExtendedVector(): DenseVector[Int] = {
    val y0 = DenseVector[Int](factorGraph.getLabels().toArray)

    val z2flow = minCutJNI()
    val z = DenseVector[Int](z2flow._1)
    min_cat_value = z2flow._2
    val notz = z.map(el => if (el == 1) 0 else 1)

    // printCutSize(z2flow._1)

    val new_y: DenseVector[Int] = (y0 :* z) + (notz :* alpha)
    // val new_y: DenseVector[Int] = DenseVector.ones[Int](z.size) :* alpha

    println("|dy| = " + (new_y - y0).norm(1))

    new_y
  }

}
