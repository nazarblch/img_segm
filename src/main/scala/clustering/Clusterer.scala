package main.scala.clustering

import weka.clusterers.SimpleKMeans
import main.scala.instances
import weka.core.{FastVector, Instances}
import java.io.{BufferedReader, FileReader}

object ClustererConf {
  val arffPath: String = "data/weka_insts.arff"
  val nClusters = 10
}

class Clusterer {

  val cl = new SimpleKMeans

  def convertInst(inst: instances.Instance): weka.core.Instance = {
    new weka.core.Instance(1.0, inst.getArray)
  }

  def getInstances(): Instances = {
    // train.reduce(_++_).saveARFF(ClustererConf.arffPath)
    new Instances(new BufferedReader(new FileReader(ClustererConf.arffPath)))
  }

  def build() {

    println("building clusterer ...")

    cl.setMaxIterations(10)
    cl.setNumClusters(ClustererConf.nClusters)
    val data = getInstances()
    // data.deleteAttributeAt(data.attribute("class").index())
    println(">> instances loaded")

    cl.buildClusterer(data)

  }

  def classify(inst: instances.Instance): Int = {
    cl.clusterInstance(convertInst(inst))
  }

  def classify(insts: instances.Instances): Array[Int] = {
    insts.data.map(inst => classify(inst)).toArray
  }

}
