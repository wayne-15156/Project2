package com.example.project2


data class tokenObject(val access_token: String,
                       val expires_in: Int,
                       val token_type: String)

data class THSRStationRes(val StationUID: String, val StationID: String, val StationCode: String, val StationName: stationName,
                          val StationAddress: String, val OperatorID: String, val UpdateTime: String, val VersionID: Int, val StationPosition: stationPosition,
                          val LocationCity: String, val LocationCityCode: String, val LocationTown: String, val LocationTownCode: String) {
    data class stationName(val Zh_tw: String, val En: String)
    data class stationPosition(val PositionLon: Double, val PositionLat: Double, val GeoHash: String)
}
