package com.task.football.fixtures.data.models

data class MatchesItem(val area: Area,
                       val matchday: Int = 0,
                       val awayTeam: AwayTeam,
                       val competition: Competition,
                       val utcDate: String = "",
                       val lastUpdated: String = "",
                       val score: Score,
                       val stage: String = "",
                       val odds: Odds,
                       val season: Season,
                       val homeTeam: HomeTeam,
                       val id: Int = 0,
                       val status: String = "",
                       var isFavorite:Boolean = false,
                       val group: Any? = null){


}