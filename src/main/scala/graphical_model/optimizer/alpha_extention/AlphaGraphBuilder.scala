package graphical_model.optimizer.alpha_extention

import graphical_model.factor_graph.{FactorEdge, FactorGraph}
import collection.mutable.ArrayBuffer
import mincut.Tweight
import breeze.linalg.DenseVector


class AlphaGraphBuilder(val factorGraph: FactorGraph, val multFactor: Int) {

  val src_index = factorGraph.size
  val sink_index = factorGraph.size + 1

  val src_edges: Array[Double] = Array.fill(factorGraph.size)(0.0)
  val sink_edges: Array[Double] = Array.fill(factorGraph.size)(0.0)
  val in_edges = Vector.newBuilder[AlphaEdge]

  def new_AlphaEdge(from: Int, to: Int, E: Double) {
    if (from == src_index) {
      src_edges(to) += multFactor * E
    } else if (to == sink_index) {
      sink_edges(from) += multFactor * E
    } else {
      in_edges += new AlphaEdge(from, to, multFactor * E)
    }
  }

  def createAlphaEdges(factorEdge: FactorEdge, alpha: Int) {

    assert(factorEdge.from < factorEdge.to)

//    if (
//    factorEdge.E(Some(alpha), None) + factorEdge.E(None, Some(alpha))
//    - factorEdge.energy - factorEdge.E(Some(alpha), Some(alpha)) < 0.0) return

    new_AlphaEdge(src_index, factorEdge.from, factorEdge.energy)
    new_AlphaEdge(factorEdge.from, sink_index, factorEdge.E(Some(alpha), None))
    new_AlphaEdge(factorEdge.to, sink_index, factorEdge.E(Some(alpha), Some(alpha)) - factorEdge.E(Some(alpha), None))
    new_AlphaEdge(factorEdge.to,
                    factorEdge.from,
                    factorEdge.E(Some(alpha), None) + factorEdge.E(None, Some(alpha))
                      - factorEdge.energy - factorEdge.E(Some(alpha), Some(alpha)))

  }

  def createAlphaEdges(node_index: Int, alpha: Int) {

    new_AlphaEdge(src_index, node_index, factorGraph.nodeEnergy(node_index))
    new_AlphaEdge(node_index, sink_index, factorGraph.nodeEnergy(node_index, alpha))

  }

  def build(alpha: Int): AlphaGraph = {

    for (i <- Range(0, factorGraph.size)) {
      createAlphaEdges(i, alpha)
    }

    factorGraph.forEachUndirEdge(fe => createAlphaEdges(fe, alpha))

    new AlphaGraph(factorGraph, in_edges.result().filter(_.weight > 0.0), src_edges.toVector, sink_edges.toVector, alpha)
  }
}
