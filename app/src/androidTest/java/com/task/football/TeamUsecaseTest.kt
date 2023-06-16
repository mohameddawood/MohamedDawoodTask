package com.task.football

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
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
class TeamUsecaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    fun getItems(): SnapshotStateList<MatchesItem> {
        val items = SnapshotStateList<MatchesItem>()
        items.add(MatchesItem(isFavorite = true, id = 5, utcDate = "2022-02-10T19:48:37Z"))
        items.add(MatchesItem(isFavorite = false, id = 6, utcDate = "2022-02-10T19:48:37Z"))
        items.add(MatchesItem(isFavorite = true, id = 7, utcDate = "2022-02-10T19:48:37Z"))
        items.add(MatchesItem(isFavorite = false, id = 8, utcDate = "2022-02-10T19:48:37Z"))
        items.add(MatchesItem(isFavorite = true, id = 9, utcDate = "2022-02-10T19:48:37Z"))
        return items
    }


    @Inject
    lateinit var usecase: TeamUsecase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testLoadFixtures() = runBlocking {
        val item = usecase.loadFixtures().first()
        Assert.assertNotEquals(null, item)
    }

    @Test
    fun testFilterWithFavs() = runBlocking {
        val item = getItems()
        usecase.filter(true, item, getItems())
        Assert.assertEquals(3, item.size)
    }


    @Test
    fun testFilterAll() = runBlocking {
        val item = getItems()
        usecase.filter(false, item, getItems())
        Assert.assertEquals(5, item.size)
    }
}