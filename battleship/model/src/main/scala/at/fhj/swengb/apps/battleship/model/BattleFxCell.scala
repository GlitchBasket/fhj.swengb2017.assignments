package at.fhj.swengb.apps.battleship.model

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
;

/**
  * Represents one part of a vessel or one part of the ocean.
  */

case class BattleFxCell(pos: BattlePos
                        , width: Double
                        , height: Double
                        , log: String => Unit
                        , someVessel: Option[Vessel] = None
                        , fn: (Vessel, BattlePos) => Unit
                        , clicked: BattlePos => Unit
                       ) extends Rectangle(width, height) {

  def init(): Unit = {
      setFill(Color.BLUE)
  }

  setOnMouseClicked(e => {
    mouseClick
  })

  def mouseClick() = {
    someVessel match {
      case None =>
        log(s"Missed. Just hit water.")
        setFill(Color.AQUAMARINE)
        clicked(pos)
      case Some(v) =>
        // log(s"Hit an enemy vessel!")
        fn(v, pos)
        setFill(Color.RED)
        clicked(pos)
    }
  }
  def simClick() = {
    someVessel match {
      case None =>
        setFill(Color.AQUAMARINE)
      case Some(v) =>
        setFill(Color.RED)
    }
  }
}
