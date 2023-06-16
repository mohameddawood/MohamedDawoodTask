package com.task.football.fixtures.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.data.db.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TeamViewModel @Inject constructor(
    private val usecase: TeamUsecase,
    private val db: DataStoreManager
) : ViewModel() {
    private var _fixtures = mutableStateListOf<MatchesItem>()
    val fixtures: List<MatchesItem> get() = _fixtures
    private val tempList = mutableStateListOf<MatchesItem>()
    var favList = mutableStateListOf<MatchesItem>()
    var showToggle = mutableStateOf<Boolean>(false)
    var hashSet = hashSetOf<MatchesItem>()

    fun filter(switchOn: Boolean) {
        usecase.filter(switchOn, _fixtures, tempList)
    }

    private fun getFixturesList() {
        if (_fixtures.isEmpty())
            viewModelScope.launch {
                usecase.loadFixtures().collect {
                    val items = it.matches?.sortedBy { it.utcDate }
                    items?.forEach {
                        db.saveMatches(it)
                    }
                    _fixtures.addAll(items ?: listOf())
                    tempList.addAll(items?: listOf())
                }.runCatching {
                    this.toString()
                }
            }
    }

    fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
            usecase.getListFromDB().collect {
                if (it.isEmpty()) {
                    getFixturesList()
                    cancel()
                } else {
                    _fixtures.addAll(it)
                    tempList.addAll(it)
                    cancel()
                }
            }
        }
    }

    private fun saveToFave(matchesItem: MatchesItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.saveMatchToFavorites(matchesItem)
        }
    }

    private fun removeFromFave(matchesItem: MatchesItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.removeMatchToFavorites(matchesItem)
        }
    }

    fun favClicked(item: MatchesItem, found: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!found) {
                val c = item.copy(isFavorite = false)
                removeFromFave(c)
                _fixtures.find { it.id == item.id }?.isFavorite = false
            } else {
                val c = item.copy(isFavorite = true)
                saveToFave(c)
                _fixtures.find { it.id == item.id }?.isFavorite = true
            }
        }
    }
}