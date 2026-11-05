package com.novara.levelup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novara.levelup.data.*
import java.time.LocalDate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LevelUpUiState(
    val player: PlayerEntity = PlayerEntity(),
    val quests: List<QuestWithStatus> = emptyList(),
    val habits: List<HabitWithStatus> = emptyList()
)

class LevelUpViewModel(private val repository: LevelUpRepository) : ViewModel() {
    private val today = LocalDate.now().toString()
    val state = combine(repository.player, repository.quests(today), repository.habits(today)) { player, quests, habits ->
        LevelUpUiState(player ?: PlayerEntity(), quests, habits)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LevelUpUiState())

    init {
        viewModelScope.launch { repository.initialize() }
    }

    fun claimQuest(id: Int) {
        viewModelScope.launch { repository.claimQuest(id, today) }
    }

    fun setHabit(id: Int, completed: Boolean) {
        viewModelScope.launch { repository.setHabitCompleted(id, completed, today) }
    }
}
