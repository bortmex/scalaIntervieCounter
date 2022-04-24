package tableDef

import entity.Student
import slick.jdbc.PostgresProfile.api._

class StudentTableDef(tag: Tag) extends Table[Student](tag, "student") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def intervieCounter = column[Int]("interviecounter")

  def javaRushLevel = column[Int]("javarushlevel")

  def chanceToComplete = column[Int]("chancetocomplete")

  override def * = (id, name, intervieCounter,  javaRushLevel, chanceToComplete) <> (Student.tupled, Student.unapply)
}
