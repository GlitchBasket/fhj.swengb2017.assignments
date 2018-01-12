package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.nio.file.Files
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea, Button}
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.{BattleShipProtobuf, BattleShipProtocol}


class BattleShipFxController extends Initializable {


  @FXML private var battleGroundGridPane: GridPane = _

  /**
    * A text area box to place the history of the game
    */
  @FXML private var log: TextArea = _
  @FXML private var historySlider: Slider = _
  @FXML private var historyButton: Button = _



  @FXML
  def newGame(): Unit = initGame()

  var actualGame: BattleShipGame = _
  var simMode: Boolean = false

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(game: BattleShipGame, clicks: List[BattlePos]) : Unit = {
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    game.getCells().foreach(c => c.init)
    game.loadOrder(clicks, false)
  }


  private def initGame(): Unit = {
    actualGame = createGame()
    init(actualGame, List())
    simMode = true
    history()
    appendLog("New game started.")
  }

  private def createGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battleField: BattleField = BattleField.placeRandomly(field)

    BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog)
  }

  def saveGame(): Unit = {
    val fileChooser = new FileChooser
    fileChooser.setInitialFileName("Save file")
    fileChooser.setInitialFileName("BattleShip")
    val selectedFile = fileChooser.showSaveDialog(null)
    if (selectedFile != null) {
      val actualGameProto = BattleShipProtocol.convert(actualGame)
      actualGameProto.writeTo(Files.newOutputStream(selectedFile.toPath))
    }
  }

  def loadGame(): Unit = {
    val fileChooser = new FileChooser
    fileChooser.setInitialFileName("Load file")
    fileChooser.setInitialFileName("BattleShip")
    val selectedFile = fileChooser.showOpenDialog(null)
    if (selectedFile != null) {
      val loadGameProto = BattleShipProtobuf.BattleShipGame.parseFrom(Files.newInputStream(selectedFile.toPath))
      val convGame = BattleShipProtocol.convert(loadGameProto)
      actualGame = BattleShipGame(convGame.battleField, getCellHeight, getCellHeight, appendLog)
      init(actualGame, convGame.clicks)
    }

  }

  def history(): Unit = {
    if (simMode == false) {
      simMode = true
      historySlider.setMax(actualGame.clicks.size)
      historySlider.setValue(actualGame.clicks.size)
      historySlider.setDisable(false)
      historyButton.setStyle("-fx-text-fill: rgba(255, 0, 0, 1);")
      sliderAction()
    }
    else {
      simMode = false
      historySlider.setDisable(true)
      historyButton.setStyle("-fx-text-fill: rgba(20, 20, 20, 1);")
      sliderAction()
      actualGame.loadOrder(actualGame.clicks, true)
      historySlider.setValue(actualGame.clicks.size)
    }
  }

  def sliderAction(): Unit = {
    val currVal = historySlider.getValue.toInt

    val simClickPos: List[BattlePos] = actualGame.clicks.take(currVal).reverse

    battleGroundGridPane.getChildren.clear()
    for (c <- actualGame.getCells()) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
      c.init()
      c.setDisable(simMode)
    }

    actualGame.loadOrder(simClickPos, true)
  }

}

