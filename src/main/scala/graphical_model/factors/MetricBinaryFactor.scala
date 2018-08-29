package graphical_model.factors

class MetricBinaryFactor (val coef: Double, val metric: TIntUnaryFactor) extends TIntIntBinaryFactor {
  def apply(i: Int, j: Int): Double = coef * metric(math.abs(i - j))
}
