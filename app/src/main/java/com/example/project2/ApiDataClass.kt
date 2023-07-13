package com.example.project2


data class tokenObject(val access_token: String, val expires_in: Int, val token_type: String)

data class THSRStationRes(val StationUID: String, val StationID: String, val StationCode: String, val StationName: stationName,
                          val StationAddress: String, val OperatorID: String, val UpdateTime: String, val VersionID: Int, val StationPosition: stationPosition,
                          val LocationCity: String, val LocationCityCode: String, val LocationTown: String, val LocationTownCode: String) {
    data class stationName(val Zh_tw: String, val En: String)
    data class stationPosition(val PositionLon: Double, val PositionLat: Double, val GeoHash: String)
}

data class DailyTimeTable(val TrainDate: String, val DailyTrainInfo: dailyTrainInfo,
                          val OriginStopTime: originStopTime, val DestinationStopTime: destinationStopTime,
                          val UpdateTime: String, val VersionID: Int) {

    data class dailyTrainInfo(val TrainNo: String, val Direction: Int,
                              val StartingStationID: String, val StartingStationName: startingStationName,
                              val EndingStationID: String, val EndingStationName: endingStationName) {
        data class startingStationName(val Zh_tw: String, val En: String)
        data class endingStationName(val Zh_tw: String, val En: String)
    }
    data class originStopTime(val StopSequence: Int, val StationID: String,
                              val StationName: stationName,
                              val ArrivalTime: String, val DepartureTime: String) {
        data class stationName(val Zh_tw: String, val En: String)
    }

    data class destinationStopTime(val StopSequence: Int, val StationID: String,
                                   val StationName: stationName,
                                   val ArrivalTime: String, val DepartureTime: String) {
        data class stationName(val Zh_tw: String, val En: String)
    }

}

data class TrainTimeTable(val TrainDate: String, val DailyTrainInfo: dailyTrainInfo,
                          val StopTimes: ArrayList<stopTimes>, val UpdateTime: String,
                          val VersionID: Int) {
    data class dailyTrainInfo(val TrainNo: String, val Direction: Int, val StartingStationID: String,
                              val StartingStationName: startingStationName, val EndingStationID: String,
                              val EndingStationName: endingStationName) {
        data class startingStationName(val Zh_tw: String, val En: String)
        data class endingStationName(val Zh_tw: String, val En: String)
    }
    data class stopTimes(val StopSequence: Int, val StationID: String,
                         val StationName: stationName, val ArrivalTime: String,
                         val DepartureTime: String) {
        data class stationName(val Zh_tw: String, val En: String)
    }
}

data class RestRes(val results: Results) {
    data class Results(val content: ArrayList<Content>) {
        data class Content(val name: String, val vicinity: String,
                           val photo: String, val rating: String,
                           val reviewsNumber: String)
    }
}