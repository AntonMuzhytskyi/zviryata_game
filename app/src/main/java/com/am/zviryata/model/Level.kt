package com.am.zviryata.model

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

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