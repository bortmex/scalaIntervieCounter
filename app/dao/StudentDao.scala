package dao

import com.google.inject.Inject
import entity.Student
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import tableDef.StudentTableDef

import scala.concurrent.{ExecutionContext, Future}

class StudentDao @Inject()(
                             protected val dbConfigProvider: DatabaseConfigProvider
                           )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  var studentTable = TableQuery[StudentTableDef]

  def add(student: Student): Future[String] = {
    dbConfig.db
      .run(studentTable += student)
      .map(res => "Student successfully added")
      .recover {
        case ex: Exception => {
          printf(ex.getMessage())
          ex.getMessage
        }
      }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(studentTable.filter(_.id === id).delete)
  }

  def update(student: Student): Future[Int] = {
    dbConfig.db
      .run(studentTable.filter(_.id === student.id)
        .map(x => (x.name, x.intervieCounter, x.javaRushLevel))
        .update(student.name, student.intervieCounter, student.javaRushLevel)
      )
  }

  def get(id: Long): Future[Option[Student]] = {
    dbConfig.db.run(studentTable.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Student]] = {
    dbConfig.db.run(studentTable.sortBy(_.id.asc.nullsFirst).result)
  }
}
