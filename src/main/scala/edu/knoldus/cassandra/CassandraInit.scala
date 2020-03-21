package edu.knoldus.cassandra

import com.datastax.driver.core.{BoundStatement, Cluster, PreparedStatement, ResultSet, Session, UDTValue}
import edu.knoldus.cassandra.udt.model.Models
import edu.knoldus.cassandra.udt.model.Models.{Movie, User}

import scala.collection.JavaConverters._

object CassandraInit extends UserRepo {
  private val cluster: Cluster = Cluster.builder().addContactPoint("localhost").build()
  private val session: Session = cluster.newSession()
  private def createKeySpace: ResultSet = session.execute(
    """CREATE KEYSPACE IF NOT EXISTS user_movies
      |WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1}""".stripMargin)

  private def createMovieUdT: ResultSet = session.execute(s"""CREATE TYPE  IF NOT EXISTS user_movies.movie (
                                          | name text,
                                          | year int,
                                          |);""".stripMargin)

  private def createUserTable: ResultSet = session.execute(s"""CREATE TABLE IF NOT EXISTS user_movies.user
                                                   |(name text,
                                                   |movies list<frozen<movie>>,
                                                   |PRIMARY KEY (name));""".stripMargin)


  def init = {
    createKeySpace
    createMovieUdT
    createUserTable
    savePrepStatement
    fetchUsersPrepStatement
  }

  def shutdown: Unit = {
    session.close()
    cluster.close()
  }

  private def savePrepStatement: PreparedStatement = session.prepare(insertUserPrepareStatement)
  private def fetchUsersPrepStatement: PreparedStatement = session.prepare(getUsersStatements)

  override def save(data: Models.User): Unit = {
    val binded: BoundStatement = savePrepStatement.bind()
    val movieUdt = session.getCluster.getMetadata
      .getKeyspace("user_movies").getUserType("movie")
    val movieUdtDataType: List[UDTValue] = data.movies.map{ movie =>
      movieUdt.newValue()
        .setString("name", movie.name)
        .setInt("year", movie.year)
    }
    binded.setString("name", data.name)
    binded.setList("movies", movieUdtDataType.asJava)
    session.execute(binded)
  }

  override def fetch(name: String): Models.User = {
    val binded = fetchUsersPrepStatement
      .bind().setString("name", name)
    val row = session.execute(binded).one()
    User(name, row.getList("movies", classOf[UDTValue])
      .asScala.toList.map(udtValue => Movie(udtValue.getString("name"), udtValue.getInt("year"))))
  }
}
