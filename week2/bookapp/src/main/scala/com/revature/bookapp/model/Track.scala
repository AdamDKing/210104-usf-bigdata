package com.revature.bookapp.model

import java.sql.ResultSet

case class Track(
    trackId: Int,
    name: String,
    mediaTypeId: Int,
    milliseconds: Int,
    unitPrice: Double
) {}

object Track {
  def produceFromResultSet(resultSet: ResultSet) = {
    apply(
      resultSet.getInt("track_id"),
      resultSet.getString("name"),
      resultSet.getInt("media_type_id"),
      resultSet.getInt("milliseconds"),
      resultSet.getDouble("unit_price")
    )
  }
}
