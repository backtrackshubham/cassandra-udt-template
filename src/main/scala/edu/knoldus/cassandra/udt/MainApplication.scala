package edu.knoldus.cassandra.udt

import edu.knoldus.cassandra.CassandraInit
import edu.knoldus.cassandra.udt.model.Models.{Movie, User}

/**
 * Main app triggers the process
 */

object MainApplication extends App {
  //initializing connection with cassandra
  CassandraInit.init

  val movies = List(
    Movie("Intersteller", 2004),
    Movie("Mahabhart", 2000)
  )
  val user = User(
    "Shubham",
    movies
  )

  CassandraInit.save(user)

  println(CassandraInit.fetch(user.name))

  CassandraInit.shutdown

}
