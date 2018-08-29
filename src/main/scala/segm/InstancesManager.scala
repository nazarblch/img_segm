package main.scala.segm

import graphical_model.factor_graph.{FeaturesStructure, FactorsInstance}
import graphical_model.optimizer.Optimizer
import main.scala.instances.FactorGraphBuilder
import collection.mutable
import edu.illinois.cs.cogcomp.indsup.learning.WeightVector


class InstancesManager {

  var trainPairs: mutable.HashMap[String, (FactorsInstance, FeaturesStructure)] = _
  val builder = new FactorGraphBuilder()

  def loadTrainData(names: Seq[String]) {
     trainPairs = mutable.HashMap()

     names.foreach(name => {
       val pair = builder.buildStructPair(name)
       trainPairs.put(name, pair)
     })
  }

  def getTrainSeq: Seq[(FactorsInstance, FeaturesStructure)] = trainPairs.values.toSeq

  def printPredictionSummery(pair: (FactorsInstance, FeaturesStructure), opt: Optimizer, w: WeightVector): (Float, Float) = {

    pair._1.g.setWeights(w)
    val bestY = opt.optimize(pair._1.g)
    val h_loss = pair._2.g.getHammingLoss(bestY).toFloat
    val loss = pair._2.g.getLoss(bestY).toFloat

    println(pair._1.segm_data.name + ": ham_loss=" + h_loss + ", loss=" + loss)

    pair._1.segm_data.makeSegmentationRes(bestY).saveBAW("data/predict/" + pair._1.segm_data.name + "_segm_res.png")

    (h_loss, loss)
  }

  def printQuality(names: Seq[String], opt: Optimizer, w: WeightVector) {

    var avg_h_loss = 0.0
    var avg_loss = 0.0

    names.foreach(name => {
      val pair = trainPairs.get(name).getOrElse(builder.buildStructPair(name))
      val (h_loss, loss) = printPredictionSummery(pair, opt, w)
      avg_h_loss += h_loss
      avg_loss += loss
    })

    println("avg_ham_loss=" + (avg_h_loss / names.size) + ", avg_loss=" + (avg_loss / names.size) )
  }


  def runTest(names: Seq[String], opt: Optimizer) {

    names.foreach(name => {
      val (g, info) = builder.buildWithParams(name)
      println("making segmentation of " + name)
      val bestY = opt.optimize(g)
      info.makeSegmentationRes(bestY).saveBAW("data/predict_test/" + info.name + "_segm_res.png")
    })

  }


  def printTrainQuality(opt: Optimizer, w: WeightVector) {
    printQuality(trainPairs.keySet.toSeq, opt, w)
  }



}
