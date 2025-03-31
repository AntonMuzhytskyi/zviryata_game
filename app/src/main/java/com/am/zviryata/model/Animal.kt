package com.am.zviryata.model

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