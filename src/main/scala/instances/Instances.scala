package main.scala.instances

import java.io.FileWriter
import main.scala.clustering.ClustererConf
import breeze.linalg.SparseVector


class Instances (val data: Vector[Instance]) {
  var class2count: Map[Int, Int] = _

  def saveARFF(path: String) {
    val fw = new FileWriter(path)

    fw.write("@RELATION cars \n\n")

    for (f <- Range(0, data(0).getSparseX.length)) {
      fw.write("@ATTRIBUTE f_" + f + " NUMERIC \n")
    }

    if (class2count != null)
      fw.write("\n@ATTRIBUTE class {0, 1} \n\n")

    fw.write("@DATA\n")

    for (inst <- data) {
      fw.write(inst.toString + "\n")
    }

    fw.close()

  }

  def ++ (other: Instances): Instances = {
    new Instances(data ++ other.data)
  }

  def cos(i: Int, j: Int): Double = {
    (data(i).getSparseX dot data(j).getSparseX) / math.sqrt(data(i).getSparseX.norm(2) * data(j).getSparseX.norm(2) + 1)
  }

  def dist(i: Int, j: Int, gamma: Double): Double = {
    math.exp(gamma * (data(i).getSparseX - data(j).getSparseX).norm(2))
  }

  def potts_dist(i: Int, j: Int, gamma: Double): Double = {
    math.max((data(i).getSparseX - data(j).getSparseX).norm(1), gamma)
  }

  def rgb_category_vec(ci: Int, cj: Int, w: Double): SparseVector[Double] = {
    assert(ci < ClustererConf.nClusters)
    assert(cj < ClustererConf.nClusters)

    val index = ClustererConf.nClusters * math.min(ci, cj) + math.max(ci, cj)
    val res = new SparseVector[Double](Array(index), Array(w) , 1, ClustererConf.nClusters * ClustererConf.nClusters)

    res
  }

}
