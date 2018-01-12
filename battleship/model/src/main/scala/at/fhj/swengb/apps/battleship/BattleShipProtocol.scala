package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.model._

import scala.collection.JavaConverters._

object BattleShipProtocol {

  def convert(battlePos: BattleShipProtobuf.BattlePos): BattlePos = BattlePos(battlePos.getX, battlePos.getY)

  def convert(battlePos: BattlePos): BattleShipProtobuf.BattlePos = BattleShipProtobuf.BattlePos.newBuilder().setX(battlePos.x).setY(battlePos.y).build()

  def convert(vessel: BattleShipProtobuf.Vessel): Vessel = {
    val vesselDirection: Direction = {
      vessel.getDirection match {
        case BattleShipProtobuf.Direction.Horizontal => Horizontal
        case BattleShipProtobuf.Direction.Vertical => Vertical
        case _ => ???
      }
    }
    Vessel(NonEmptyString(vessel.getName), convert(vessel.getStartPos), vesselDirection, vessel.getSize)
  }

  def convert(vessel: Vessel): BattleShipProtobuf.Vessel = {
    val vesselDirection = {
      vessel.direction match {
        case Horizontal => BattleShipProtobuf.Direction.Horizontal
        case Vertical => BattleShipProtobuf.Direction.Vertical
        case _ => ???
      }
    }
    BattleShipProtobuf.Vessel
      .newBuilder()
      .setName(vessel.name.value)
      .setStartPos(convert(vessel.startPos))
      .setDirection(vesselDirection)
      .setSize(vessel.size)
      .build()
  }

  def convert(fleet: BattleShipProtobuf.Fleet): Fleet = Fleet(fleet.getVesselsList.asScala.map(x => convert(x)).toSet)

  def convert(fleet: Fleet): BattleShipProtobuf.Fleet = {
    val convertedVessels = fleet.vessels.map(x => convert(x))
    val pFleet = BattleShipProtobuf.Fleet.newBuilder()

    convertedVessels.map(x => pFleet.addVessels(x))
    pFleet.build()
  }

  def convert(battleField: BattleShipProtobuf.BattleField): BattleField = BattleField(battleField.getWidth, battleField.getHeight, convert(battleField.getFleet))

  def convert(battleField: BattleField): BattleShipProtobuf.BattleField = {
    BattleShipProtobuf.BattleField
      .newBuilder()
      .setWidth(battleField.width)
      .setHeight(battleField.height)
      .setFleet(convert(battleField.fleet))
      .build()
  }

  def convert(g : BattleShipProtobuf.BattleShipGame) : BattleShipGame = {
    val convOrder = g.getOrderList.asScala.map(x => convert(x)).toList
    val game = BattleShipGame(
      convert(g.getBattlefield),
      x => x.toDouble,
      x => x.toDouble,
      x => ()
    )
    game.clicks = convOrder
    game
  }

  def convert(g : BattleShipGame) : BattleShipProtobuf.BattleShipGame = {
    val convOrder = g.clicks.map(x => convert(x))
    val pgame = BattleShipProtobuf.BattleShipGame.newBuilder().setBattlefield(convert(g.battleField))

    convOrder.map(x => pgame.addOrder(x))
    pgame.build()
  }
}
