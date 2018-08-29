package graphical_model.optimizer

import graphical_model.factor_graph.FactorGraph

trait Optimizer {
  def optimize(factorGraph: FactorGraph): Array[Int]
}
