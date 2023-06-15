package com.task.football.fixtures.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.domain.db.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    var favList = mutableStateListOf<MatchesItem>()
    var showToggle = mutableStateOf<Boolean>(false)
    var hashSet = hashSetOf<MatchesItem>()


    fun filter(switchOn: Boolean) {
        if (switchOn) {
            _fixtures.clear()
            val nList = tempList.filter { it.isFavorite }.distinct()
            _fixtures.addAll(nList.ifEmpty { listOf() })
        } else {
            _fixtures.clear()
            _fixtures.addAll(tempList.distinct())
        }
    }

    fun getFixturesList() {
        if (_fixtures.isEmpty())
            viewModelScope.launch {
                usecase.loadFixtures().collect {
                    val items = it.matches?.sortedBy { it.utcDate }
                    items?.forEach {
                        db.saveMatches(it)
                    }
                    _fixtures.addAll(items?: listOf())
                }.runCatching {
                    this.toString()
                }
            }
    }

    fun loadItems() {
        viewModelScope.launch {
            db.getFavs().collect {
                if (it.asMap().isNotEmpty()){
                    it.asMap().map {
                        val obj = Gson().fromJson(it.value.toString(), MatchesItem::class.java)
                        if (_fixtures.find { i -> i.id == obj.id } == null)
                            _fixtures.add(obj)
                    }
                    tempList.addAll(_fixtures)
                }else getFixturesList()
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
            showToggle.value = !favList.isEmpty()
            /* if (favList.isEmpty()) {
                 tempList.forEach { it.isFavorite = false }
                 _fixtures.addAll(tempList)
             }*/
        }
    }
}