package graphical_model.factor_graph

import graphical_model.factors.{TIntParametricUnaryFactor, TIntUnaryFactor}
import breeze.linalg.{SparseVector, DenseVector}


abstract class FactorNode {
  def E(value: Int, w: Seq[DenseVector[Double]]): Double
  def loss(label: Int): Double
  def getFeatures(classLabel: Int): SparseVector[Double]
  def getFeatures(): SparseVector[Double]
  def getGroundValue: Int
}

class UnlabeledFactorNode(val unaryFactor: TIntParametricUnaryFactor) extends FactorNode {

  def E(value: Int, w: Seq[DenseVector[Double]]) =
    unaryFactor(value, w: Seq[DenseVector[Double]], false)

  def loss(label: Int) = {
    throw new Exception("undefined method")
    ???
  }

  def getFeatures(classLabel: Int) = unaryFactor.getFeatures(classLabel)

  def getFeatures() = {
    throw new Exception("undefined method")
    ???
  }

  def getGroundValue: Int = ???
}

class GroundFactorNode(val unaryFactor: TIntParametricUnaryFactor) extends FactorNode {

  def E(value: Int, w: Seq[DenseVector[Double]]) =
    unaryFactor(value, w: Seq[DenseVector[Double]], true)

  def loss(label: Int) = unaryFactor.loss(label)

  def getFeatures(classLabel: Int) = unaryFactor.getFeatures(classLabel)

  def getFeatures() = unaryFactor.getFeatures()

  def getGroundValue: Int = unaryFactor.y.get
}
