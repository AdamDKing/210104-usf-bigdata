package com.revature.bookapp.utils

import java.sql.Connection
import java.sql.DriverManager

object ConnectionUtil {

  /**
    * utility for retrieving connection, with hardcorded credentials
    *
    * @return Connection
    */
  def getConnection() : Connection = {
    
    classOf[org.postgresql.Driver].newInstance() // manually load the Driver

    //missing a bit of documentation from the java code:
    // getConnection takes a url, username, password
    // hardcoding these at the moment, though this is v bad practice
    DriverManager.getConnection("jdbc:postgresql://localhost:5026/adam", "adam", "wasspord")

  }

}