package com.am.zviryata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.shared.shared.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> get() = _currentLevel

    fun nextLevel() {
        viewModelScope.launch {
            val newLevel = _currentLevel.value + 1
            _currentLevel.value = newLevel
            gameRepository.completeLevel(newLevel)
        }
    }

    fun isLevelCompleted(level: Int) = gameRepository.isLevelCompleted(level)
}