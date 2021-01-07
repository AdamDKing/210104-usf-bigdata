package com.revature.wccli

import scala.io.BufferedSource
import scala.io.Source
import java.io.File

/**
  * Provides basic file functionality to other parts of our application
  */
object FileUtil {

  /**
    * Gets the text content from a file
    * 
    * We'll introduce a tool in Scala that's good for dealing with content that may or may
    * not be successfully retrieved
    *
    * @param filename
    * @param sep the separator used between lines in returned text content of the file
    * @return the text content of a file, or the empty string
    */
  def getTextContent(filename: String, sep: String = " "): String = {
    //We open files using Source.fromFile, this will get us a BufferedSource.
    // Whenever we open files, we want to be sure to close them
    // When dealing with files, we may run into Exceptions, so we use a try-finally to close them
    var openedFile : BufferedSource = null // declare our openedFile var
    // we need to declare openedFile outside of the try block, otherwise it will only exist in the
    // scope of the try block and won't be accessible in the finally block

    try {
      openedFile = Source.fromFile(filename)  
      // Scala returns the last line of this try block, since there is nothing below the try-finally
      openedFile.getLines().mkString(sep)
    } finally {
      //if the file was opened, close it
      if (openedFile != null) openedFile.close()
    }
  }

  /**
    * Returns an array containing all top level filename
    *
    * @return
    */
  def getTopLevelFiles(): Array[String] = {
    val currentDir = new File(".")
    //we're going to filter the list of java.io.Files, only including files that are files, not directories
    //This will leave us with an array of java.io.Files
    //We then map that array, turning each java.io.File into a String -- its filename
    currentDir.listFiles()
      .filter((f: File) => {f.isFile()})
      .map((f: File) => {f.getName()})

    //shorthand version of the above:
    //currentDir.listFiles().filter(_.isFile()).map(_.getName())

    //longform version of the above, without lambdas
    // def checkIfFile(file: File) : Boolean = {
    //   file.isFile()
    // }

    // def fileToStringFilename(file: File) : String = {
    //   file.getName()
    // }
    // currentDir.listFiles().filter(checkIfFile).map(fileToStringFilename)
  }

}