package graphical_model.optimizer

import graphical_model.factor_graph.FactorGraph
import breeze.linalg.DenseVector
import main.scala.instances.{FactorGraphBuilder, InstancesLoader}

class SimpleOptimizer extends Optimizer {

  def optimize(factorGraph: FactorGraph): Array[Int] = {

    val best_Y = Array.fill(factorGraph.size)(0)

    for (n <- Range(0, factorGraph.size)) {
      best_Y(n) = factorGraph.labelRange.map(y => (y, factorGraph.nodeEnergy(n, y))).maxBy(_._2)._1
    }

    best_Y
  }
}


