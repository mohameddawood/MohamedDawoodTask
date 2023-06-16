package com.task.football.fixtures.domain

import com.google.gson.Gson
import com.task.football.fixtures.data.models.ResponseData
import com.task.football.base.network.AppAPis
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.data.db.DataStoreManager
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamsRepo @Inject constructor(
    private val appAPis: AppAPis,
    private val db: DataStoreManager
) {

    fun loadFixturesList() = flow<ResponseData> {
        emit(appAPis.getFixturesList())
    }

    fun getMatchesFromDB() = flow{
        val arr = arrayListOf<MatchesItem>()
        db.getFavs().collect {
            if (it.asMap().isNotEmpty()) {
                it.asMap().map {
                    val obj = Gson().fromJson(it.value.toString(), MatchesItem::class.java)
                    if (arr.find { i -> i.id == obj.id } == null)
                        arr.add(obj)
                }
                emit(arr)
            }else emit(arrayListOf<MatchesItem>())
        }
    }
}