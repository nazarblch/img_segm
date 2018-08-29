package main.scala.segm

import graphical_model.optimizer.{BestSegmentationFinder, GraphCutsOptimizer}

object Train extends App {

  def loadRange(from: Int, to: Int): Seq[String] = {
    io.Source.fromFile("data/train_data.txt").getLines().filter(_.startsWith("img")).map(_.trim.split("_")(1)).slice(from, to).toSeq
  }

  val trainRange = (args(args.indexOf("-train") + 1).toInt, args(args.indexOf("-train") + 2).toInt)
  val testRange = (args(args.indexOf("-test") + 1).toInt, args(args.indexOf("-test") + 2).toInt)

  val opt = new GraphCutsOptimizer
  val finder = new BestSegmentationFinder(opt)

  val trainer = new StructureTrainer(finder)
  val inst_manager = new InstancesManager()
  inst_manager.loadTrainData(
    loadRange(trainRange._1, trainRange._2)
  )

  val w = trainer.trainParallelSSVM(0.04, inst_manager.getTrainSeq, 12)

  inst_manager.printTrainQuality(opt, w)
  inst_manager.printQuality(loadRange(testRange._1, testRange._2), opt, w)

}
