package com.task.football.fixtures.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.domain.db.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject


@HiltViewModel
class TeamViewModel @Inject constructor(
    private val usecase: TeamUsecase,
    private val db: DataStoreManager
) : ViewModel() {
    private var _fixtures = mutableStateListOf<MatchesItem>()
    val fixtures: List<MatchesItem>
        get() = _fixtures
    private val tempList = mutableStateListOf<MatchesItem>()
    private val filterList = mutableStateListOf<MatchesItem>()
    var favList = mutableStateListOf<MatchesItem>()
    var hashSet = hashSetOf<MatchesItem>()

    init {
        getAllFavs()
    }

    fun filter(switchOn: Boolean) {
        if (switchOn) {
            filterList.clear()
            _fixtures.clear()
            _fixtures.addAll(favList)
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
                favList.forEach { it.isFavorite = true }
                hashSet.addAll(favList)
                hashSet.addAll(items ?: listOf())
                _fixtures.addAll(hashSet.toList().sortedBy { it.utcDate })
                tempList.addAll(hashSet.toList().sortedBy { it.utcDate })
            }.runCatching {
                this.toString()
            }
        }
    }

    fun getAllFavs() {
        viewModelScope.launch {
            db.getFavs().collect {
                it.asMap().map {
                    val obj = Gson().fromJson(it.value.toString(), MatchesItem::class.java)
                    if (favList.find { i -> i.id == obj.id } == null)
                        favList.add(
                            obj
                        )
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

    fun favClicked(item: MatchesItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val found = db.isKeyStored(stringPreferencesKey(item.id.toString())).first()
            if (found) {
                removeFromFave(item)
                favList.remove(item)
            } else {
                saveToFave(item)
                favList.add(item)
                _fixtures.find { it.id == item.id }?.isFavorite = true
            }
        }
    }
}