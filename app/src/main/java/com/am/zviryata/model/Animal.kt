package com.am.zviryata.model

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

/**
 * Data class representing an animal in the game.
 *
 * @param defaultImageRes Resource ID for the default animal image
 * @param clickedImageRes Resource ID for the clicked animal image
 * @param soundRes Resource ID for the animal's sound
 */
data class Animal(
    val defaultImageRes: Int,
    val clickedImageRes: Int,
    val soundRes: Int
)