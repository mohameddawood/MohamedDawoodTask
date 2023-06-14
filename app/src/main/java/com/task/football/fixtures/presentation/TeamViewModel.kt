package com.task.football.fixtures.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.data.models.MatchesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val usecase: TeamUsecase
) : ViewModel() {
    private var _fixtures = mutableStateListOf<MatchesItem>()
    val fixtures: List<MatchesItem>
        get() = _fixtures
    private val tempList = mutableStateListOf<MatchesItem>()
    private val filterList = mutableStateListOf<MatchesItem>()
    fun filter(arr: MutableList<MatchesItem>, switchOn: Boolean) {

        if (switchOn) {
            filterList.clear()
            tempList.forEach {match->
                val item = arr.find { it.id == match.id }
                item?.let {
                    match.apply {
                        isFavorite = true
                    }
                    filterList.add(it)
                }
            }
            _fixtures.clear()
            _fixtures.addAll(filterList)
        } else {
            _fixtures.clear()
            _fixtures.addAll(tempList)
        }
    }

    fun getFixturesList() {
        viewModelScope.launch {
            usecase.loadFixtures().collect {
                val items = it.matches?.sortedBy { it.utcDate }
                _fixtures.clear()
                _fixtures.addAll(items ?: listOf())
                tempList.addAll(items ?: listOf())
            }.runCatching {
                this.toString()
            }
        }
    }
}