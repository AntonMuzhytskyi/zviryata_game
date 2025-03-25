package com.am.shared.shared.repository

class GameRepository {
    private val completedLevels = mutableSetOf<Int>()

    fun isLevelCompleted(level: Int) = completedLevels.contains(level)

    fun completeLevel(level: Int) {
        completedLevels.add(level)
    }
}