package com.task.football.fixtures.data.models

data class Score(val duration: String = "",
                 val winner: String = "",
                 val halfTime: HalfTime,
                 val fullTime: FullTime
)