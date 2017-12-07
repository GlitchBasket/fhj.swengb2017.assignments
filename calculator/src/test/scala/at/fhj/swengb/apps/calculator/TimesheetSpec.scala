package at.fhj.swengb.apps.calculator

import java.nio.file.{Files, Path, Paths}

import org.scalatest.WordSpecLike

import scala.collection.JavaConverters._

class TimesheetSpec extends WordSpecLike {

  val p: Path = Paths.get("C:\\Workspace\\fhj.swengb2017.assignments\\calculator\\timesheet-calculator.adoc")

  "A test" should {
    "work" in {
      //skeleton of a test

      val result = Files.readAllLines(p).asScala.mkString("\n")
      println(result)

      assert(expected == result)
    }
    "work2" in {
      //skeleton of a test

      println(p)
    }
    "work3" in {
      //skeleton of a test

      Files.readAllLines(p)
    }
  }

  val expected =
    """== Time expenditure: Calculator assignment
      |
      |[cols="1,1,4", options="header"]
      |.Time expenditure
      ||===
      || Date
      || Hours
      || Description
      |
      || 29.11.17
      || 1
      || Review of this and that
      |
      || 30.11.17
      || 5
      || implemented css
      |
      || 11.07.17
      || 2
      || fixed bugs
      |
      ||===
      |""".stripMargin
}
