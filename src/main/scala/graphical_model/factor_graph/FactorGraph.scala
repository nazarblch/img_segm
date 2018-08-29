package graphical_model.factor_graph

import breeze.linalg.DenseVector
import utils.NToN2Projector
import edu.illinois.cs.cogcomp.indsup.learning.WeightVector

class FactorGraph(val labelRange: Set[Int] = Set(0, 1),
                  val nToN2Projector: Option[NToN2Projector] = None) {

  private final var nodes: Vector[FactorNode] = _
  private final var edges: Vector[Map[Int, FactorEdge]] = _

  private var nodeLabels: Array[Int] = _

  var Wn: Seq[DenseVector[Double]] = _

  var We: Seq[DenseVector[Double]] = _

  def getEdgeW = We

  final def parametersCount: Int = getNodeParamsCount + getEdgesParamsCount

  final def getNodeParamsCount = Wn.map(_.length).sum

  final def getEdgesParamsCount = We.map(_.length).sum

  def setWeights(weight: WeightVector) {

    val length: Int = Wn.map(_.length).sum

    // println(weight.getWeightArray.length)

    assert(length == weight.getWeightVectorLength - 1 - getEdgesParamsCount || weight.getWeightVectorLength == 10000)

    val W = weight.getWeightArray.slice(1, getNodeParamsCount + 1).splitAt(length / 2)

    Wn = Seq[DenseVector[Double]](
      DenseVector[Double](W._1),
      DenseVector[Double](W._2)
    )

    val We_tmp = weight.getWeightArray.slice(length + 1, parametersCount + 1)

    We = Seq[DenseVector[Double]](
      DenseVector[Double](We_tmp.slice(0, We(0).length)),
      DenseVector[Double](We_tmp.slice(We(0).length, We(0).length + We(1).length)),
      DenseVector[Double](We_tmp.slice(We(0).length + We(1).length, getEdgesParamsCount))
    )
  }

  def size: Int = nodes.size

  def setNodes(nodes: Vector[FactorNode]) {
    this.nodes = nodes
  }

  def forEachUndirEdge(f: (FactorEdge) => Unit) {
    edges.foreach(_.foreach(e => {
      if (e._2.from < e._2.to) f(e._2)
    }))
  }

  def forEachNode(f: (Int, FactorNode) => Unit) {
    Range(0, size).foreach(i => f(i, nodes(i)))
  }

  def sum(f: (Int, FactorNode) => Double): Double = {
    var res = 0.0
    Range(0, size).foreach(i => res += f(i, nodes(i)))
    res
  }

  def setEdges(edges: Vector[Map[Int, FactorEdge]]) {
    this.edges = edges
  }

  def nodeEnergy(index: Int, label: Int): Double = {
    getNodeByIndex(index).E(label, Wn)
  }

  def nodeEnergy(index: Int): Double = {
    getNodeByIndex(index).E(nodeLabels(index), Wn)
  }

  def getLabel(index: Int): Int = nodeLabels(index)

  def getEdge(from: Int, to: Int): FactorEdge = edges(from)(to)

  def getNodeByIndex(index: Int) = nodes(index)

  def getNeighbourIndexes(index: Int): Iterable[Int] = edges(index).keys

  def energy(silent: Boolean = false): Double = {

    val e1 = sum((i, node) => node.E(nodeLabels(i), Wn))

    var e2 = 0.0

    forEachUndirEdge(fe => e2 += fe.energy)

    if (!silent) println("E_nodes = " + e1 + " E_edges = " + e2)

    e1 + e2
  }

  def print() {
    nodes.zipWithIndex.filter(_._2 < 10000).foreach({case (node, index) => {
      println(index + ": " + getNeighbourIndexes(index).mkString(", "))
    }})
  }


  def getLabels(): Vector[Int] = nodeLabels.toVector

  def setLabels(values: Array[Int]) {
    assert(values.length == size)
    values.foreach(i => assert(labelRange.contains(i)))
    nodeLabels = values
  }

  def nodeWithEdgesEnergy(index: Int): Double = {
    edges(index).map(_._2.energy).sum + nodes(index).E(nodeLabels(index), Wn)
  }

  def getLoss(values: Array[Int]): Double = {
    var sum = 0.0
    forEachNode((i, node) => sum += node.loss(values(i)))
    sum
  }

  def getHammingLoss(values: Array[Int]): Double = {
    var sum = 0
    forEachNode(
      (i, node) => {
        if (node.getGroundValue != values(i))
          sum += 1
      }
    )
    sum.toDouble / size
  }

}
