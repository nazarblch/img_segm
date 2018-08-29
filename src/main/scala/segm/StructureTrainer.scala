package main.scala.segm

import graphical_model.factor_graph.{FeaturesStructure, FactorsInstance}
import graphical_model.optimizer.{GraphCutsOptimizer, SimpleOptimizer, BestSegmentationFinder}
import edu.illinois.cs.cogcomp.indsup.learning
import learning.L2Loss.{L2LossParallelJLISLearner, L2LossJLISLearner}
import learning.{JLISParameters, WeightVector, StructuredProblem}
import main.scala.instances.{FactorGraphBuilderConf, FactorGraphBuilder, InstancesLoader}
import edu.illinois.cs.cogcomp.indsup.inference.AbstractLossSensitiveStructureFinder
import java.io.FileWriter



class StructureTrainer(val finder: BestSegmentationFinder) {

  var time: Double = _

  def startTimer() {
    time = System.currentTimeMillis() / 1000.0
  }

  def printDuration() {
    println("Duration = " + (System.currentTimeMillis() / 1000.0 - time) + " s")
  }

  def trainSequenceSSVM(C: Double, trainPairs: Seq[(FactorsInstance, FeaturesStructure)]): WeightVector = {


    val problem: StructuredProblem = new StructuredProblem()

    trainPairs.foreach({case (ins, struc) => {
      problem.input_list.add(ins)
      problem.output_list.add(struc)
    }})

    val para = new JLISParameters();
    para.total_number_features = trainPairs(0)._2.g.parametersCount;
    para.c_struct = C;

    val learner = new L2LossJLISLearner();

    // train the model!
    learner.trainStructuredSVM(finder, problem, para);

  }

  def trainParallelSSVM(C: Double, trainPairs: Seq[(FactorsInstance, FeaturesStructure)], par: Int): WeightVector = {
    val problem: StructuredProblem = new StructuredProblem()

    trainPairs.foreach({case (ins, struc) => {
      problem.input_list.add(ins)
      problem.output_list.add(struc)
    }})

    val para = new JLISParameters();
    para.total_number_features = trainPairs(0)._2.g.parametersCount;
    para.c_struct = C;
    para.MAX_SVM_ITER = 1000;

    para.MAX_OUT_ITER = 150;
    para.DUAL_GAP = 0.07
    para.check_inference_opt = false
    para.OUTTER_STOP = 0.001

    val learner = new L2LossParallelJLISLearner();

    val s_finder_list: Array[AbstractLossSensitiveStructureFinder] = Array.fill(par)(finder.clone())

    startTimer()
    // parallel training the classifiers
    val res = learner.parallelTrainStructuredSVM(s_finder_list, problem, para);

    printDuration()

    saveW(res)

    res
  }

  def saveW(w: WeightVector) {
    val fw = new FileWriter(FactorGraphBuilderConf.savedWPath)
    fw.write(w.getWeightArray.mkString(","))
    fw.close()
  }

}


