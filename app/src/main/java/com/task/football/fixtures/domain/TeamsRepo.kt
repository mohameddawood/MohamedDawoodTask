package com.task.football.fixtures.domain

import com.task.football.fixtures.data.models.ResponseData
import com.task.football.base.network.AppAPis
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamsRepo @Inject constructor(
    private val appAPis: AppAPis
) {

    fun loadFixturesList() = flow<ResponseData> {
        emit(appAPis.getFixturesList())
    }
}