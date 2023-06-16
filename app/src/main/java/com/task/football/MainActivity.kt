package com.task.football

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.task.football.fixtures.presentation.TeamViewModel
import com.task.football.fixtures.presentation.ui.screens.TeamsScreen
import com.task.football.fixtures.presentation.ui.theme.MohamedDawoodTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<TeamViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MohamedDawoodTaskTheme {
                // A surface container using the 'background' color from the theme
                TeamsScreen(viewModel)
            }
        }
    }
}
