package com.task.football.base.network

import com.task.football.BuildConfig
import com.task.football.fixtures.data.models.ResponseData
import retrofit2.http.GET
import retrofit2.http.Header

interface AppAPis  {

    @GET("matches")
    suspend fun getFixturesList(
        @Header("X-Auth-Token") key:String = BuildConfig.API_KEY,
    ): ResponseData

    companion object{ operator fun invoke() {} }
}