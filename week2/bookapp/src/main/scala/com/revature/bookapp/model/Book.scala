package com.revature.bookapp.model

import java.sql.ResultSet

case class Book(bookId : Int, title: String, isbn : String) {

}

object Book {
  /**
    * Produces a Book from a record in a ResultSet.  Note that this method does *not* call next()!
    *
    * @param rs
    * @return
    */
  def fromResultSet(rs : ResultSet) : Book = {
    apply(
      rs.getInt("book_id"),
      rs.getString("title"),
      rs.getString("isbn")
    )
  }
}