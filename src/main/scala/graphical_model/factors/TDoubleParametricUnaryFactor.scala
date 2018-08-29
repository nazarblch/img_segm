package graphical_model.factors


import breeze.linalg.{SparseVector, DenseVector}
import main.scala.utils.Sparse._

class TIntParametricUnaryFactor(val features: SparseVector[Double], val y: Option[Int], val c_eq: Double, val c_neq: Double) {

 val y0features = new SparseVector[Double](
   features.index,
   features.data,
   features.activeSize,
   2 * features.length)

 val y1features = new SparseVector[Double](
   features.index.map(_ + features.length),
   features.data,
   features.activeSize,
   2 * features.length)

 def apply(classLabel: Int, w: Seq[DenseVector[Double]], withLoss: Boolean): Double = {
   val prod = (features dot w(classLabel))

   if (withLoss)
     -loss(classLabel) + prod
   else
     prod
 }

 def loss(classLabel: Int): Double = {
   if (classLabel != y.get)
     c_neq
   else
     c_eq
 }

 def getFeatures(classLabel: Int): SparseVector[Double] = {
   if (classLabel == 0) y0features
   else if (classLabel == 1) y1features
   else throw new Exception("bad class label")
 }

  def getFeatures(): SparseVector[Double] = {
    getFeatures(y.get)
  }

}