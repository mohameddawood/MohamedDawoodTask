package com.task.football

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.football.fixtures.data.db.DataStoreManager
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.presentation.TeamViewModel
import com.task.football.fixtures.presentation.ui.screens.TeamsScreen
import com.task.football.fixtures.presentation.ui.theme.MohamedDawoodTaskTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MyComposeTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var usecase: TeamUsecase
    @Inject
    lateinit var dataStoreManager: DataStoreManager


    @Before
    fun init() {
        hiltRule.inject()
    }
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Test
    fun myTest() {

        composeTestRule.setContent {
            MohamedDawoodTaskTheme() {
                TeamsScreen(TeamViewModel(usecase,dataStoreManager))
            }
        }


        composeTestRule.onNodeWithText("Show Favorites").assertIsDisplayed()
    }
}