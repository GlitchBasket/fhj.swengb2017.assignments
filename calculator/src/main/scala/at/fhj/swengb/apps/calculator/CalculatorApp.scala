package at.fhj.swengb.apps.calculator

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.beans.property.{ObjectProperty, SimpleObjectProperty}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.TextField
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success}
import scala.util.control.NonFatal

object CalculatorApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[CalculatorFX], args: _*)
  }
}

class CalculatorFX extends javafx.application.Application {

  val fxml = "/at/fhj/swengb/apps/calculator/calculator.fxml"
  val css = "/at/fhj/swengb/apps/calculator/calculator.css"

  def mkFxmlLoader(fxml: String): FXMLLoader = {
    new FXMLLoader(getClass.getResource(fxml))
  }

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("Calculator")
      setSkin(stage, fxml, css)
      stage.show()
      stage.setMinWidth(stage.getWidth)
      stage.setMinHeight(stage.getHeight)
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

  def setSkin(stage: Stage, fxml: String, css: String): Boolean = {
    val scene = new Scene(mkFxmlLoader(fxml).load[Parent]())
    stage.setScene(scene)
    stage.getScene.getStylesheets.clear()
    stage.getScene.getStylesheets.add(css)
  }

}

class CalculatorFxController extends Initializable {

  val calculatorProperty: ObjectProperty[RpnCalculator] = new SimpleObjectProperty[RpnCalculator](RpnCalculator())

  def getCalculator() : RpnCalculator = calculatorProperty.get()

  def setCalculator(rpnCalculator : RpnCalculator) : Unit = calculatorProperty.set(rpnCalculator)

  @FXML var number1 : TextField = _
  @FXML var number2 : TextField = _
  @FXML var result : TextField = _

  override def initialize(location: URL, resources: ResourceBundle) = {

  }

  def sgn(): Unit = {
    getCalculator().push(Op(number1.getText)) match {
      case Success(c) => setCalculator(c)
      case Failure(e) => // show warning / error
    }
    getCalculator().stack foreach println
  }
  /**
  *  Number-Buttons
  */
  def zero(): Unit = {
    result.appendText("0")
  }

  def one(): Unit = {
    result.appendText("1")
  }

  def two(): Unit = {
    result.appendText("2")
  }

  def three(): Unit = {
    result.appendText("3")
  }

  def four(): Unit = {
    result.appendText("4")
  }

  def five(): Unit = {
    result.appendText("5")
  }

  def six(): Unit = {
    result.appendText("6")
  }

  def seven(): Unit = {
    result.appendText("7")
  }

  def eight(): Unit = {
    result.appendText("8")
  }

  def nine(): Unit = {
    result.appendText("9")
  }

  /**
  Functional-Buttons
    */

  def enter(): Unit = {
    if (number2.getText == "") {
      number2.setText(result.getText)
      result.setText("")
    }
    else if (number1.getText == "" && number2.getText != "") {
      number1.setText(number2.getText)
      number2.setText(result.getText)
      result.setText("")
    }

  }

  def comma(): Unit = {
    result.appendText(".")
  }

  def add(): Unit = {

  }

  def subtract(): Unit = {

  }

  def multiply(): Unit = {

  }

  def devide(): Unit = {

  }
  /**
  Special-Buttons
    */

  def percent(): Unit = {

  }

  def plusminus(): Unit = {

  }

  def clear(): Unit = {
    number1.setText("")
    number2.setText("")
    result.setText("")
  }


}