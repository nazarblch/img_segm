package main.scala.instances

import img.{Image}
import main.scala.utils.MatrixManager
import collection.mutable.HashMap
import main.scala.clustering.{Clusterer, ClustererConf}
import java.io.{File, FileWriter}

class InstancesLoader(val dir: String, val prefix: String, val name: String) {

  final var instances: Instances = _
  final var edges: Vector[Edge] = EdgesLoader.load(dir + "/" + prefix + name + "_edges.txt").toVector

  val ground_img =
    if (new File(dir + "/" + prefix + name + "_groundtruth.png").exists()) Image(dir + "/" + prefix + name + "_groundtruth.png").toBAW()
    else null
  val pixel_to_sp_map = MatrixManager.loadFromFile(dir + "/" + prefix + name + "_superpixels.txt")
  val features_map = MatrixManager.loadFromFileDouble(dir + "/" + prefix + name + "_features.txt")
  val sp_cats =
    if (new File(dir + "/" + prefix + name + "_spcats.txt").exists())
      io.Source.fromFile(dir + "/" + prefix + name + "_spcats.txt").getLines().next().trim.split(",").map(_.toInt)
    else
      null

  def saveRGBCategories(cl: Clusterer) {
    val fw = new FileWriter(dir + "/" + prefix + name + "_spcats.txt")
    fw.write(cl.classify(instances).mkString(","))
    fw.close()
  }

  def loadFeatures(): Instances = {

    val inst_data = Vector.newBuilder[Instance]

    for (i <- Range(0, features_map.rows)) {
      inst_data += new Instance(features_map(i, ::).toDenseVector.toArray)
    }

    instances = new Instances(inst_data.result())

    instances
  }

  private def loadClasses() {

    Image.scale = 1

    val sp_to_class_map = HashMap[Int, Array[Int]]()

    ground_img.foreachPixel((index, p) => {
      val sp_index = pixel_to_sp_map(p.x, p.y)
      val classLabel = ground_img.matrix(p.x, p.y)
      val labels = sp_to_class_map.getOrElse(sp_index, Array[Int]()) ++ Array(classLabel)

      sp_to_class_map.put(sp_index, labels)
    })

    val Y = sp_to_class_map.map({case (k, v) => {
      (k - 1, v.groupBy(x => x).maxBy(_._2.length)._1)
    }}).toVector.sortBy(_._1).map(_._2)

    val S = sp_to_class_map.map({case (k, v) => {
      (k - 1, v.groupBy(x => x).map({case (cl, seq) => (cl, seq.length)}))
    }}).toVector.sortBy(_._1).map(_._2)

    val cl2count = Array[Int](0, 0)

    for (i <- Range(0, Y.length)) {
      instances.data(i).y = Some(Y(i))
      instances.data(i).max_pxCount = Some(S(i).get(Y(i)).get)
      instances.data(i).min_pxCount = Some(S(i).get(1 - Y(i)).getOrElse(0))

      cl2count(Y(i)) += instances.data(i).max_pxCount.get
      cl2count(1 - Y(i)) += instances.data(i).min_pxCount.get
    }

    instances.class2count = Map(0 -> cl2count(0), 1 -> cl2count(1))

    println("load " + name + ": " + instances.class2count)
  }

  def loadImageData() {
    loadFeatures()
    loadClasses()
  }

  def loadTestImage() {
    loadFeatures()
  }
}


object LoadInstances extends App {

  val names = Array("001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016")

  names.map(str => new InstancesLoader("data/train", name = str, prefix = "imgTrain_").loadFeatures()).reduce(_++_).saveARFF(ClustererConf.arffPath)

  println("loaded")

}

object SaveCats extends App {

  val names = io.Source.fromFile("data/test_data.txt").getLines().filter(_.startsWith("img")).map(_.trim.split("_")(1))
  val cl = new Clusterer()
  cl.build()

  names.foreach(str => {
    val loader = new InstancesLoader("data/test", name = str, prefix = "imgTest_")
    loader.loadFeatures()
    loader.saveRGBCategories(cl)
  })

}
