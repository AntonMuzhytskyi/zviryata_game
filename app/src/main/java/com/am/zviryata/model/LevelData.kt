package com.am.zviryata.model

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

/**
 * Data class representing level data for the menu screen.
 *
 * @param id Unique identifier for the level
 * @param imageRes Resource ID for the completed level image
 * @param lockedImageRes Resource ID for the locked level image
 * @param title Display title of the level
 */
data class LevelData(
    val id: Int,
    val imageRes: Int,
    val lockedImageRes: Int,
    val title: String
)