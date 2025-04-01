package com.am.zviryata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.zviryata.repository.GameRepository
import com.am.zviryata.services.AdManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

/**
 * ViewModel responsible for managing game state and business logic for levels.
 * Follows MVVM pattern by serving as the intermediary between the UI and repository.
 *
 * @param gameRepository Repository for accessing game data and persistence
 */
class GameViewModel(
    val gameRepository: GameRepository,
    private val adManager: AdManager
) : ViewModel() {

    // Private mutable state flows for level and clicked animals
    private val _currentLevel = MutableStateFlow(1)
    private val _animalsClicked = MutableStateFlow<Set<Int>>(emptySet())
    private val _levelState = MutableStateFlow<LevelState>(LevelState.Initial)
    private val _interstitialAd = MutableStateFlow<InterstitialAd?>(null)

    // Public immutable state flows for UI observation
    val currentLevel: StateFlow<Int> = _currentLevel.asStateFlow()
    val animalsClicked: StateFlow<Set<Int>> = _animalsClicked.asStateFlow()
    val levelState: StateFlow<LevelState> = _levelState.asStateFlow()

    init {
        // Initialize level completion monitoring
        viewModelScope.launch {
            animalsClicked.collect { clicked ->
                val levelData = gameRepository.getLevel(currentLevel.value)
                if (levelData != null && clicked.size == levelData.animals.size) {
                    handleLevelCompletion()
                }
            }
        }
        loadInterstitialAd()
    }

    /**
     * Sets the current level and resets clicked animals.
     *
     * @param levelId The ID of the level to load
     */
    fun setCurrentLevel(levelId: Int) {
        _currentLevel.value = levelId
        resetLevel()
        _levelState.value = LevelState.Initial
    }

    /**
     * Proceeds to the next level if available, otherwise signals game completion.
     */
    fun proceedToNextLevel() {
        val nextLevelId = _currentLevel.value + 1
        val ad = _interstitialAd.value
        if (ad != null) {
            _levelState.value = LevelState.ShowingAd(nextLevelId)
            adManager.showInterstitialAd(
                ad,
                onAdDismissed = { proceedAfterAd(nextLevelId) },
                onAdFailed = { proceedAfterAd(nextLevelId) }
            )
            _interstitialAd.value = null // Clear ad after showing
            loadInterstitialAd() // Preload next ad
        } else {
            proceedAfterAd(nextLevelId)
        }
    }

    /**
     * Handles animal click events by updating the clicked animals set.
     *
     * @param index Index of the clicked animal
     */
    fun onAnimalClicked(index: Int) {
        _animalsClicked.value = _animalsClicked.value + index
    }

    /**
     * Resets the clicked animals for the current level.
     */
    fun resetLevel() {
        _animalsClicked.value = emptySet()
    }

    /**
     * Checks if the current level is completed.
     *
     * @return True if all animals have been clicked, false otherwise
     */
    fun isLevelCompleted(): Boolean {
        val animals = gameRepository.getLevelAnimals(_currentLevel.value)
        return _animalsClicked.value.size == animals.size
    }

    private fun loadInterstitialAd() {
        adManager.loadInterstitialAd { ad ->
            _interstitialAd.value = ad
        }
    }

    private fun proceedAfterAd(nextLevelId: Int) {
        if (gameRepository.getLevel(nextLevelId) != null) {
            setCurrentLevel(nextLevelId)
        } else {
            _levelState.value = LevelState.GameCompleted
        }
    }

    // Private helper to handle level completion logic
    private suspend fun handleLevelCompletion() {
        _levelState.value = LevelState.Celebrating
        delay(3000) // Wait 3 seconds for celebration animation
        gameRepository.completeLevel(_currentLevel.value)
        _levelState.value = LevelState.Completed
    }

    fun startLevelCompletion() {
        viewModelScope.launch {
            handleLevelCompletion()
        }
    }

}

/**
 * Sealed class representing the state of the level screen.
 */
sealed class LevelState {
    object Initial : LevelState()
    object Celebrating : LevelState()
    object Completed : LevelState()
    object GameCompleted : LevelState()
    data class ShowingAd(val nextLevelId: Int) : LevelState()
}
