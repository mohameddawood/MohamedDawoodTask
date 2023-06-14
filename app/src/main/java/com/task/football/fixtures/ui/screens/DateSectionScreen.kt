package com.task.football.fixtures.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.football.utils.formatDateToCompare

@Preview
@Composable
fun showDate(){
    DateSection(date = "2022-02-10T19:48:37Z")
}

@Composable
fun DateSection(date: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.formatDateToCompare(),
            fontSize=18.sp,
            fontWeight= FontWeight.Bold,
            color= Color.Magenta,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
    }
}