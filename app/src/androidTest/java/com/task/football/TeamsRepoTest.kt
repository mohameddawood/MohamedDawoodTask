package com.task.football

import com.task.football.fixtures.domain.TeamsRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TeamsRepoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var teamsRepo: TeamsRepo

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testLoadFixturesList() = runBlocking{
        val item = teamsRepo.loadFixturesList().first()
        Assert.assertNotEquals(null,item)
    }

    @Test
    fun testLoadMatchesFromDB() = runBlocking{
        val item = teamsRepo.getMatchesFromDB().first()
        Assert.assertNotEquals(null,item)
    }
}