package graphical_model.factor_graph

import edu.illinois.cs.cogcomp.indsup.inference.IStructure
import edu.illinois.cs.cogcomp.indsup.learning.FeatureVector
import breeze.linalg.DenseVector
import edu.illinois.cs.cogcomp.indsup.learning
import FeaturesExtractor._

class FeaturesStructure(val fv: FeatureVector, val g: FactorGraph) extends IStructure {
  def getFeatureVector: FeatureVector = {
    fv
  }
}


object FeaturesStructure {
  def apply(g: FactorGraph): FeaturesStructure = {
    val v = g.getFeatures()
    val fv = new FeatureVector(Range(1, v.length+1).toArray, v)

    assert(v.length == 561)

    new FeaturesStructure(fv, g)
  }

  def apply(g: FactorGraph, labels: Array[Int]): FeaturesStructure = {
    val v = g.getFeatures(labels)
    val fv = new FeatureVector(Range(1, v.length+1).toArray, v)

    assert(v.length == 561)
    new FeaturesStructure(fv, g)
  }
}
