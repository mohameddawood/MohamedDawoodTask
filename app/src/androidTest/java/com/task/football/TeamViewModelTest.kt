package com.task.football

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.task.football.fixtures.data.db.DataStoreManager
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.presentation.TeamViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TeamViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var usecase: TeamUsecase
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    lateinit var viewModel: TeamViewModel
    @Before
    fun init() {
        hiltRule.inject()
        viewModel = TeamViewModel(usecase,dataStoreManager)
    }

    @Test
    fun testLoadItems() = runBlocking{
        viewModel.loadItems()
        delay(4000)
        Assert.assertNotEquals(0,viewModel.fixtures.size)
    }

    @Test
    fun testFixturesList() = runBlocking{
        viewModel.getFixturesList()
        delay(4000)
        Assert.assertNotEquals(0,viewModel.fixtures.size)
    }
}