package com.task.football.fixtures.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.task.football.fixtures.data.models.MatchesItem
import javax.inject.Inject

class TeamUsecase @Inject constructor(
private val teamsRepo: TeamsRepo
) {

    fun loadFixtures() = (teamsRepo.loadFixturesList())
    fun filter(
        switchOn: Boolean,
        _fixtures: SnapshotStateList<MatchesItem>,
        tempList: SnapshotStateList<MatchesItem>
    ) {
        if (switchOn) {
            _fixtures.clear()
            val nList = tempList.filter { it.isFavorite }.distinct()
            _fixtures.addAll(nList.ifEmpty { listOf() })
        } else {
            _fixtures.clear()
            _fixtures.addAll(tempList.distinct())
        }
    }

    fun getListFromDB() = teamsRepo.getMatchesFromDB()

}