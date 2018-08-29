package graphical_model.factor_graph

import edu.illinois.cs.cogcomp.indsup.inference.IInstance
import main.scala.instances.SegmentationInstance


class FactorsInstance(val g: FactorGraph, val segm_data: SegmentationInstance) extends IInstance {
  def size(): Double = 1.0
}
