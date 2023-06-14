package com.task.football.fixtures.domain

import javax.inject.Inject

class TeamUsecase @Inject constructor(
private val teamsRepo: TeamsRepo
) {

    fun loadFixtures() = (teamsRepo.loadFixturesList())

}