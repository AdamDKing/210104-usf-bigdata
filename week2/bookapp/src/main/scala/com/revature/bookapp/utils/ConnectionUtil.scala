package com.revature.bookapp.utils

import java.sql.Connection
import java.sql.DriverManager

object ConnectionUtil {

  var conn: Connection = null;

  /** utility for retrieving connection, with hardcorded credentials
    *
    * This should properly be a connection pool.  Instead, we'll use it
    * like a connection pool with a single connection, that gets returned
    * whenever any part of our application needs it
    *
    * @return Connection
    */
  def getConnection(): Connection = {

    // if conn is null or closed, initialize it
    if (conn == null || conn.isClosed()) {
      classOf[org.postgresql.Driver].newInstance() // manually load the Driver

      //missing a bit of documentation from the java code:
      // getConnection takes a url, username, password
      // hardcoding these at the moment, though this is v bad practice
      conn = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5026/adam",
        "adam",
        "wasspord"
      )
    }
    // return conn, potentially after initialization
    conn
  }

}
