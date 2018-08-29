package graphical_model.factor_graph

import breeze.linalg.DenseVector
import edu.illinois.cs.cogcomp.indsup.learning.WeightVector

object FeaturesExtractor {
  implicit def wrapp(g: FactorGraph) = new FeaturesExtractor(g)
}

class FeaturesExtractor(val g: FactorGraph) {

  val length: Int = g.getNodeParamsCount

  private def getGroundNodeFeaturesSum(): DenseVector[Double] = {
    var res = DenseVector.zeros[Double](length)

    g.forEachNode((i, node) => res += node.getFeatures())

    res
  }

  private def getNodeFeaturesSum(values: Array[Int]): DenseVector[Double] = {
    var res = DenseVector.zeros[Double](length)

    g.forEachNode((i, node) => res += node.getFeatures(values(i)))

    res
  }

  private def getEdgesFeaturesSum(values: Array[Int]): DenseVector[Double] = {
    var res = DenseVector.zeros[Double](g.getEdgesParamsCount)
    g.forEachUndirEdge(e => res += e.getFeatures(values(e.from), values(e.to)))
    res
  }

  private def getEdgesFeaturesSum(): DenseVector[Double] = {
    var res = DenseVector.zeros[Double](g.getEdgesParamsCount)
    g.forEachUndirEdge(e => res += e.getFeatures())
    res
  }

  def getFeatures(values: Array[Int]): Array[Double] = {
    (getNodeFeaturesSum(values).toArray ++ getEdgesFeaturesSum(values).toArray).map(- _)
  }

  def getFeatures(): Array[Double] = {
    (getGroundNodeFeaturesSum().toArray ++ getEdgesFeaturesSum().toArray).map(- _)
  }

}
