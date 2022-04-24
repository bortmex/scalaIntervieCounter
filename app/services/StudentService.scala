package services

import com.google.inject.Inject
import dao.StudentDao
import entity.Student

import scala.concurrent.Future

class StudentService @Inject() (students: StudentDao) {

  def addItem(student: Student): Future[String] = {
    students.add(student)
  }

  def deleteItem(id: Long): Future[Int] = {
    students.delete(id)
  }

  def updateItem(student: Student): Future[Int] = {
    students.update(student)
  }

  def getItem(id: Long): Future[Option[Student]] = {
    students.get(id)
  }

  def listAllItems: Future[Seq[Student]] = {
    students.listAll
  }
}
