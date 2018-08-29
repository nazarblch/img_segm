package utils

import img.N2Point

class NToN2Projector(val width: Int, val height: Int) {

  def getCoordinates(index: Int): N2Point = {
    N2Point(index / width, index % width)
  }

}
