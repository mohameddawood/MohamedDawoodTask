package com.task.football.fixtures.data.models

data class Season(val winner: Winner,
                  val currentMatchday: Int = 0,
                  val endDate: String = "",
                  val id: Int = 0,
                  val startDate: String = "")