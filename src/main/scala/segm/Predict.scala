package main.scala.segm

import graphical_model.optimizer.{BestSegmentationFinder, GraphCutsOptimizer}

object Predict extends App {

  def loadRange(from: Int, to: Int): Seq[String] = {
    io.Source.fromFile("data/test_data.txt").getLines().filter(_.startsWith("img")).map(_.trim.split("_")(1)).slice(from, to).toSeq
  }

  val range = (args(args.indexOf("-range") + 1).toInt, args(args.indexOf("-range") + 2).toInt)

  val opt = new GraphCutsOptimizer
  val inst_manager = new InstancesManager()

  inst_manager.runTest(loadRange(range._1, range._2), opt)

}
