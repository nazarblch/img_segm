package graphical_model.optimizer

import edu.illinois.cs.cogcomp.indsup.inference.{IStructure, IInstance, AbstractLossSensitiveStructureFinder}
import edu.illinois.cs.cogcomp.indsup.learning.WeightVector
import edu.illinois.cs.cogcomp.core.datastructures.Pair
import graphical_model.factor_graph.{FeaturesStructure, FactorsInstance}
import java.lang.Double


class BestSegmentationFinder(val optimizer: Optimizer) extends AbstractLossSensitiveStructureFinder {

  override def clone(): BestSegmentationFinder = {
    new BestSegmentationFinder(optimizer)
  }

  def getBestStructure(weight: WeightVector, ins: IInstance): IStructure = {
//    val g = ins.asInstanceOf[FactorsInstance].g
//    g.setWeights(weight)
//    val best_Y = optimizer.optimize(g)
//    FeaturesStructure(g, best_Y)
    null
  }

  def getLossSensitiveBestStructure(weight: WeightVector, ins: IInstance, goldStructure: IStructure): Pair[IStructure, Double] = {
    val g = goldStructure.asInstanceOf[FeaturesStructure].g
    g.setWeights(weight)
    val best_Y = optimizer.optimize(g)

    new Pair(FeaturesStructure(g, best_Y), g.getLoss(best_Y))
  }
}
