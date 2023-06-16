package com.task.football.fixtures.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.football.fixtures.data.models.MatchesItem
import com.task.football.fixtures.domain.TeamUsecase
import com.task.football.fixtures.data.db.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class TeamViewModel @Inject constructor(
    private val usecase: TeamUsecase,
    private val db: DataStoreManager
) : ViewModel() {
    private var _fixtures = mutableStateListOf<MatchesItem>()
    private val tempList = mutableStateListOf<MatchesItem>()
    val fixtures: List<MatchesItem> get() = _fixtures

    fun filter(switchOn: Boolean) {
        usecase.filter(switchOn, _fixtures, tempList)
    }

    fun getEmptyState() = usecase.showEmptyFav

    fun getFixturesList() {
        if (_fixtures.isEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                usecase.loadFixtures().collect {
                    val items = it.matches?.sortedBy { it.utcDate }
                    withContext(Dispatchers.Main) {
                        _fixtures.addAll(items ?: listOf())
                        tempList.addAll(items ?: listOf())
                    }
                    items?.forEach {
                        db.saveMatches(it)
                    }

                }.runCatching {
                    this.toString()
                }
            }
    }

    fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
            usecase.getListFromDB().collect {
                when (it) {
                    is TeamUsecase.DataType.DBData -> {
                        _fixtures.addAll(it.list)
                        tempList.addAll(it.list)
                        cancel()
                    }

                    is TeamUsecase.DataType.NetworkData -> {
                        getFixturesList()
                        cancel()
                    }
                }
            }
        }
    }


    fun favClicked(item: MatchesItem, found: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            usecase.actionFav(db, found, _fixtures, item)
        }
    }
}