package com.am.zviryata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.am.zviryata.repository.GameRepository
import com.am.zviryata.services.AdManager

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

/**
 * Factory class for creating instances of GameViewModel.
 * Provides dependency injection for the repository.
 *
 * @param gameRepository The repository instance to inject into the ViewModel
 */
class GameViewModelFactory(
    private val gameRepository: GameRepository,
    private val adManager: AdManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(gameRepository, adManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

