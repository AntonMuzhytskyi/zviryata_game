package com.am.zviryata.model

/**
 * Data class representing a game level.
 *
 * @param id Unique identifier for the level
 * @param backgroundRes Resource ID for the level background
 * @param animals List of animals in the level
 * @param titleTextRes Resource ID for the level title string
 */
data class Level(
    val id: Int,
    val backgroundRes: Int,
    val animals: List<Animal>,
    val titleTextRes: Int
)