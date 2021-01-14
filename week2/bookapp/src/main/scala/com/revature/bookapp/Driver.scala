package com.revature.bookapp

import java.sql.DriverManager
import java.sql.ResultSet
import com.revature.bookapp.utils.ConnectionUtil
import com.revature.bookapp.model.Book
import com.revature.bookapp.model.Track
import scala.util.Using
import java.sql.Connection


object Driver {
  def main(args: Array[String]): Unit = {
    runDemo()
  }

  def runDemo() = {

    var conn : Connection = null;

    Using.Manager { use =>
      conn = use(ConnectionUtil.getConnection())
      //use a prepared statement to try to get tracks
      val demoStatement = use(conn.prepareStatement("SELECT * FROM track WHERE length(name) > ? AND name LIKE ?;"))
      demoStatement.setInt(1, 10)
      demoStatement.setString(2, "B%")
      demoStatement.execute()
      val resultSet = use(demoStatement.getResultSet())
      while (resultSet.next()) {
        println(
          Track.produceFromResultSet(resultSet)
        )
      }
    }


    // we're using a prepared statement here.  A prepared statement
    // lets us use parameters in the query we sent to postgres.
    // this protects us from SQL injection
    val bookStatement = conn.prepareStatement("SELECT * FROM book WHERE title = ?;")
    // our prepared statement uses ? to mark where a parameter is used
    // we then set the value of the question mark using its 1-based index
    // so the first ? is 1, the 2nd is 2, the 3rd is 3, ... and so on
    bookStatement.setString(1, "The Dark Tower")
    bookStatement.execute()
    val rs = bookStatement.getResultSet()
    while (rs.next()) {
      println(Book.fromResultSet(rs))
    }


    val insertBookStatement = conn.prepareStatement("INSERT INTO book VALUES (DEFAULT, ?, ?);")
    insertBookStatement.setString(1, "New Book Title")
    insertBookStatement.setString(2, "1313121212124")
    insertBookStatement.execute()

    conn.close() //good practice to close resources once you're done with them

  }

}
