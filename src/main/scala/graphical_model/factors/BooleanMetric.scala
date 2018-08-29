package graphical_model.factors


class BooleanMetric extends TIntUnaryFactor {
  def apply(k: Int): Double = if (k == 0) 0 else 1
}