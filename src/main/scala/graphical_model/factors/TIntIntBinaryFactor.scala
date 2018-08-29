package graphical_model.factors

trait TIntIntBinaryFactor {
  def apply(i: Int, j: Int): Double
}
