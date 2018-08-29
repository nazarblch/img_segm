package graphical_model.optimizer.alpha_extention

class AlphaEdge(val from: Int, val to: Int, val weight: Double) {
  def + (other: AlphaEdge): AlphaEdge = {
    assert(from == other.from)
    assert(to == other.to)
    new AlphaEdge(from, to, weight + other.weight)
  }
}
