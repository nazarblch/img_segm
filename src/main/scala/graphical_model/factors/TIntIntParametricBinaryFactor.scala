package graphical_model.factors

import breeze.linalg.{SparseVector, DenseVector}
import main.scala.utils.Sparse._


class TIntIntParametricBinaryFactor(val eq0_features: SparseVector[Double],
                                    val eq1_features: SparseVector[Double],
                                    val neq_features: SparseVector[Double]) {

  val ext_length = eq0_features.length + eq1_features.length + neq_features.length

  val eq0_features_ext = new SparseVector[Double](
    eq0_features.index,
    eq0_features.data,
    eq0_features.activeSize,
    ext_length)
  val eq1_features_ext = new SparseVector[Double](
    eq1_features.index.map(_ + eq0_features.length),
    eq1_features.data,
    eq1_features.activeSize,
    ext_length)
  val neq_features_ext = new SparseVector[Double](
    neq_features.index.map(_ + eq0_features.length + eq1_features.length),
    neq_features.data,
    neq_features.activeSize,
    ext_length)

  def apply(yi: Int, yj: Int, w: Seq[DenseVector[Double]]): Double = {

    if (yi == yj && yj == 0) {
      (eq0_features dot w(0))
    } else if (yi == yj && yj == 1) {
      (eq1_features dot w(1))
    } else {
      (neq_features dot w(2))
    }

  }

  def getFeatures(yi: Int, yj: Int): SparseVector[Double] = {

    if (yi == yj && yj == 0) {
      eq0_features_ext
    } else if (yi == yj && yj == 1) {
      eq1_features_ext
    } else {
      neq_features_ext
    }
  }

}
