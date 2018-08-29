package graphical_model.factor_graph

import graphical_model.factors.{TIntIntParametricBinaryFactor, TIntIntBinaryFactor}
import breeze.linalg.{SparseVector, DenseVector}

class FactorEdge(val from: Int, val to: Int, val graph: FactorGraph, val binaryFactor: TIntIntParametricBinaryFactor) {
  def energy: Double = {
    val fromValue = graph.getLabel(from)
    val toValue = graph.getLabel(to)

    binaryFactor(fromValue, toValue, graph.getEdgeW)
  }

  def E(from_value: Option[Int], to_value: Option[Int]): Double = {
    val fromValue = from_value.getOrElse(graph.getLabel(from))
    val toValue = to_value.getOrElse(graph.getLabel(to))

    binaryFactor(fromValue, toValue, graph.getEdgeW)
  }

  def E(from_value: Int, to_value: Int): Double = E(Some(from_value), Some(to_value))

  def getFeatures(from_value: Int, to_value: Int) = binaryFactor.getFeatures(from_value, to_value)

  def getFeatures(): SparseVector[Double] = {
    val fromValue = graph.getNodeByIndex(from).getGroundValue
    val toValue = graph.getNodeByIndex(to).getGroundValue
    binaryFactor.getFeatures(fromValue, toValue)
  }

}
