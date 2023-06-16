package com.task.football

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.task.football.fixtures.data.db.DataStoreManager
import com.task.football.fixtures.data.models.MatchesItem
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
class DataStoreManagerTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Before
    fun init() {
        hiltRule.inject()
    }


    @Test
    fun testInsert() = runBlocking {
        dataStoreManager.saveMatches(MatchesItem(id = 5, utcDate = "2022-02-10T19:48:37Z"))
        var s: MatchesItem? = null
        s = Gson().fromJson<MatchesItem>(
            dataStoreManager.getFavs().first().toPreferences()[stringPreferencesKey("5")],
            MatchesItem::class.java
        )
        Assert.assertEquals(s, MatchesItem(id = 5, utcDate = "2022-02-10T19:48:37Z"))
    }


    @Test
    fun testLoadAll() = runBlocking {

        var s = dataStoreManager.getFavs().first().toPreferences()
        Assert.assertNotEquals(null, s)
    }

}