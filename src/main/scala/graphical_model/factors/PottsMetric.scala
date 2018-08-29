package graphical_model.factors

class PottsMetric(maxValue: Int) extends TIntUnaryFactor {
  def apply(k: Int): Double = math.min(k, maxValue)
}

class TDoublePottsMetric(maxValue: Double) extends TDoubleUnaryFactor {
  def apply(k: Double): Double = math.min(k, maxValue)
}
