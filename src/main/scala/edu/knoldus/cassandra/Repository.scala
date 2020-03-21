package edu.knoldus.cassandra

import edu.knoldus.cassandra.udt.model.Models.User

sealed trait Repository[T] {

  def save(data: T): Unit

  def fetch(name: String): T
}

trait UserRepo extends Repository[User] {
  val keyspaceName = "user_movies"
  val tableName = "user"
  val insertUserPrepareStatement = s"""INSERT INTO $keyspaceName.$tableName (name, movies) VALUES (?, ?);"""
  val getUsersStatements = s"""SELECT * FROM $keyspaceName.$tableName WHERE name = ?;"""
}