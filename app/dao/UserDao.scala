package dao

import tableDef.UserTableDef
import com.google.inject.Inject
import entity.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class UserDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                       )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  var usersTable = TableQuery[UserTableDef]

  def add(user: User): Future[String] = {
    dbConfig.db
      .run(usersTable += user)
      .map(res => "UserItem successfully added")
      .recover {
        case ex: Exception => {
          printf(ex.getMessage())
          ex.getMessage
        }
      }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(usersTable.filter(_.id === id).delete)
  }

  def update(user: User): Future[Int] = {
    dbConfig.db
      .run(usersTable.filter(_.id === user.id)
        .map(x => (x.name, x.password))
        .update(user.name, user.password)
      )
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(usersTable.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(usersTable.result)
  }

  def login(name: String, password: String): Future[Option[User]] = {
    dbConfig.db.run(usersTable.filter(_.name === name).filter(_.password === password).result.headOption)
  }
}