package main.scala.instances

import graphical_model.factor_graph._
import graphical_model.factors.{TIntIntParametricBinaryFactor, TIntParametricUnaryFactor}
import breeze.linalg.{SparseVector, DenseVector}
import edu.illinois.cs.cogcomp.indsup.learning.WeightVector
import main.scala.utils.Extender._

object FactorGraphBuilderConf {
  val savedWPath = "data/w.txt"

  def loadW: WeightVector = {
    new WeightVector(
      io.Source.fromFile(savedWPath).getLines().next().trim.split(",").map(_.toDouble),
      1.0
    )
  }
}


class FactorGraphBuilder (classCount: Int = 2) {

  var inst2category: Array[Int] = _

  def build(instances: Instances, inst_edges: Vector[Edge], withGroundLabels: Boolean): FactorGraph = {

    val weights = Range(0, classCount).map(cl => DenseVector.zeros[Double](instances.data(0).getSparseX.length))

    val g = new FactorGraph()

    g.Wn = weights
    g.We = Seq(
      DenseVector.zeros[Double](100),
      DenseVector.zeros[Double](100),
      DenseVector.zeros[Double](105)
    )

    val nodes = Vector.newBuilder[FactorNode]

    var i = 0
    for (inst <- instances.data) {

      val node =
        if (withGroundLabels) {
          val factor = new TIntParametricUnaryFactor(inst.getSparseX, inst.y,
            (inst.min_pxCount.get.toDouble / classCount) / instances.class2count.get(1 - inst.y.get).get,
            (inst.max_pxCount.get.toDouble / classCount) / instances.class2count.get(inst.y.get).get
          )

          new GroundFactorNode(factor)
        } else {
          val factor = new TIntParametricUnaryFactor(inst.getSparseX, None, 0, 0)
          new UnlabeledFactorNode(factor)
        }

      nodes += node

      i += 1

    }

    g.setNodes(nodes.result())

    val edges = inst_edges.flatMap(e => Seq(e, e.invert())).map(e => {

      val rgb_cat2cat_vec = instances.rgb_category_vec(inst2category(e.from - 1), inst2category(e.to - 1), instances.cos(e.from - 1, e.to - 1))

      val eq0_vector = rgb_cat2cat_vec
      val eq1_vector = rgb_cat2cat_vec
      val neq_vector = rgb_cat2cat_vec.add(Array(0, 0, e.w(0), instances.dist(e.from - 1, e.to - 1, 0.1), instances.cos(e.from - 1, e.to - 1)))

      val factor = new TIntIntParametricBinaryFactor(
        eq0_vector,
        eq1_vector,
        neq_vector
      )

      new FactorEdge(e.from - 1, e.to - 1, g, factor)
    }).groupBy(fe => fe.from).map({case (v, es) => (v, es.map(fe => (fe.to, fe)).toMap)})
    .toVector.sortBy(_._1)

    assert(edges.length == g.size)
    assert(edges(0)._1 == 0)
    assert(edges.last._1 == g.size - 1)

    g.setEdges(edges.map(_._2))

    g
  }

  def buildFromFile(loader: InstancesLoader, withGroundLabels: Boolean): (FactorGraph, SegmentationInstance) = {

    (build(loader.instances, loader.edges, withGroundLabels), new SegmentationInstance(loader.name, loader.pixel_to_sp_map))
  }

  def buildWithParams(graphName: String): (FactorGraph, SegmentationInstance) = {
    val loader = new InstancesLoader("data/test", name = graphName, prefix = "imgTest_")
    loader.loadTestImage()
    inst2category = loader.sp_cats
    val g = build(loader.instances, loader.edges, false)
    g.setWeights(FactorGraphBuilderConf.loadW)
    (g, new SegmentationInstance(loader.name, loader.pixel_to_sp_map))
  }

  def buildStructPair(graphName: String): (FactorsInstance, FeaturesStructure) = {

    val loader = new InstancesLoader("data/train", name = graphName, prefix = "imgTrain_")
    loader.loadImageData()

    inst2category = loader.sp_cats

    val (unlabG, data1) = buildFromFile(loader, false)
    val (refG, data2) = buildFromFile(loader, true)

    (new FactorsInstance(unlabG, data1), FeaturesStructure(refG))
  }

}
