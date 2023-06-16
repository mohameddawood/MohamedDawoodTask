package com.task.football.fixtures.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.utils.formatDate
import com.task.football.utils.getImageUrl
import java.text.SimpleDateFormat


@Composable
@Preview
fun Show() {
    TeamsItem(){

    }
}

@Composable
fun TeamsItem(teamItem: MatchesItem? = null,onFavClicked:()->Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(15.dp)
    ) {
        teamItem?.let {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                val (homeTeamLogo, homeTeamTxt,scoreTxt,matchStatusTxt,timeTxt, awayTeamLogo, awayTeamTxt,favIcon) = createRefs()
                AsyncImage(model = getImageUrl(it.homeTeam.crest),
                    contentDescription = "",
                    modifier = Modifier.constrainAs(homeTeamLogo) {
                        start.linkTo(parent.start, margin = 40.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                        width = Dimension.value(50.dp)
                    })
                Text(
                    text = teamItem.homeTeam.shortName,
                    modifier = Modifier.constrainAs(homeTeamTxt) {
                        top.linkTo(homeTeamLogo.bottom, margin = 10.dp)
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        centerHorizontallyTo(homeTeamLogo)
                    })


                Text(
                    text = "${teamItem.score.fullTime.home}:${teamItem.score.fullTime.away}",
                    fontWeight= FontWeight.Bold,
                    fontSize= 20.sp,
                    color= Color.Green,
                    modifier = Modifier.constrainAs(scoreTxt) {
                        start.linkTo(homeTeamLogo.end, margin = 20.dp)
                        end.linkTo(awayTeamLogo.start, margin = 20.dp)
                        top.linkTo(parent.top, margin = 15.dp)
                    })
                Text(
                    text = teamItem.status,
                    modifier = Modifier.constrainAs(matchStatusTxt) {
                        start.linkTo(homeTeamLogo.end, margin = 20.dp)
                        end.linkTo(awayTeamLogo.start, margin = 20.dp)
                        top.linkTo(scoreTxt.bottom, margin = 4.dp)
                        centerHorizontallyTo(parent)
                    })
                Text(
                    text = teamItem.utcDate.formatDate(),
                    modifier = Modifier.constrainAs(timeTxt) {
                        start.linkTo(homeTeamLogo.end, margin = 20.dp)
                        end.linkTo(awayTeamLogo.start, margin = 20.dp)
                        top.linkTo(matchStatusTxt.bottom, margin = 4.dp)
                        centerHorizontallyTo(parent)
                    })
                AsyncImage(model = getImageUrl(teamItem.awayTeam.crest),
                    contentDescription = "",
                    modifier = Modifier.constrainAs(awayTeamLogo) {
                        end.linkTo(parent.end, margin = 40.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                        width = Dimension.value(50.dp)
                    })
                Text(text = teamItem.awayTeam.shortName ?: "",
                    modifier = Modifier.constrainAs(awayTeamTxt) {
                        top.linkTo(awayTeamLogo.bottom, margin = 10.dp)
                        centerHorizontallyTo(awayTeamLogo)
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    })

                Box(modifier = Modifier.padding(0.dp).constrainAs(favIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }){
                    FavoriteButton(isFavorite = it.isFavorite){
                        onFavClicked()
                    }
                }

            }
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    isFavorite:Boolean,
    onFavClicked: () -> Unit
) {

    var isFavorite by remember { mutableStateOf(isFavorite) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            onFavClicked()
        },
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.1f
                scaleY = 1.1f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}