package graphical_model

class Logger {
  def trace(s: String) {
    println(s)
  }
  def debug(s: String) {
    println(s)
  }
}


trait Logging {
  val log = new Logger()
}
