package com.am.zviryata.model

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