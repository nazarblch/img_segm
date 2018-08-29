package graphical_model.optimizer

import alpha_extention.{AlphaGraph, AlphaGraphBuilder}
import graphical_model.factor_graph.FactorGraph
import graphical_model.Logging
import breeze.linalg.DenseVector

class GraphCutsOptimizer extends Logging with Optimizer {

  val check = new SimpleOptimizer


  def optimize(factorGraph: FactorGraph): Array[Int] = {
    factorGraph.setLabels(Array.fill(factorGraph.size)(0))

    val builder = new AlphaGraphBuilder(factorGraph, 1)
    val alpha_graph: AlphaGraph = builder.build(1)
    val new_y: DenseVector[Int] = alpha_graph.findExtendedVector()

    // val check_y = check.optimize(factorGraph)
    factorGraph.setLabels(new_y.toArray)
    //assert(math.abs(factorGraph.energy() - alpha_graph.min_cat_value) < 1.5)

    // factorGraph.setLabels(new_y.toArray)
    log.trace("min cut = " + alpha_graph.min_cat_value)
    new_y.toArray
  }

}
