package edu.knoldus.cassandra.udt.model

/**
 * Models to be saved in cassandra
 */


object Models {
  case class User(name: String, movies: List[Movie])

  case class Movie(name: String, year: Int)
}
