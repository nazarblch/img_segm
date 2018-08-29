package main.scala.instances


object EdgesLoader {

  def load(path: String): Iterator[Edge] = {
    io.Source.fromFile(path).getLines().map(Edge(_))
  }
}
