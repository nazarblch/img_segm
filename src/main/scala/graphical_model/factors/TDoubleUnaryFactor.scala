package graphical_model.factors

trait TDoubleUnaryFactor {
  def apply(x: Double): Double
}