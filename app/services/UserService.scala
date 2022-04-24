package services
import com.google.inject.Inject
import dao.UserDao
import entity.User
import formz.UserFormData

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class UserService @Inject() (items: UserDao) {

  def addItem(item: User): Future[String] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }

  def updateItem(user: User): Future[Int] = {
    items.update(user)
  }

  def getItem(id: Long): Future[Option[User]] = {
    items.get(id)
  }

  def listAllItems: Future[Seq[User]] = {
    items.listAll
  }

  def lookupUser(u: UserFormData): Boolean = {
    //if (u.name == "foo" && u.password == "foo") true else false
    val user: Future[Option[User]] = items.login(u.name, u.password)
    val maybeUser = Await.result(user, 10.seconds)
    maybeUser.isDefined
  }
}