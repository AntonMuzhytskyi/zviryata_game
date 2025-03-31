package com.am.zviryata.repository

import android.content.Context
import android.content.SharedPreferences
import com.am.zviryata.R
import com.am.zviryata.model.Animal
import com.am.zviryata.model.Level

/**
 * Repository class for managing game data and persistence.
 * Acts as the single source of truth for level data in the Model layer of MVVM.
 *
 * @param context Application context for accessing SharedPreferences
 */
class GameRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    // Predefined list of levels with their data
    private val levels: List<Level> = listOf(
        Level(
            id = 1,
            backgroundRes = R.drawable.ferma_backgropund,
            animals = listOf(
                Animal(R.drawable.pig, R.drawable.pig_done, R.raw.pigandvoice),
                Animal(R.drawable.duck, R.drawable.duck_done, R.raw.duckandvoice),
                Animal(R.drawable.lamb, R.drawable.lamb_done, R.raw.sheepandvoice),
                Animal(R.drawable.horse, R.drawable.horse_done, R.raw.horseandvoice),
                Animal(R.drawable.cow, R.drawable.cow_done, R.raw.cowandvoice),
                Animal(R.drawable.chicken, R.drawable.chicken_done, R.raw.chickenandvoice)
            ),
            titleTextRes = R.string.ferma
        ),

        Level(2, R.drawable.forest_backgropund_in, listOf(
            Animal(R.drawable.wolf, R.drawable.wolf_done, R.raw.wolf),
            Animal(R.drawable.owl, R.drawable.owl_done, R.raw.owl),
            Animal(R.drawable.bear, R.drawable.bear_done, R.raw.bear),
            Animal(R.drawable.boar, R.drawable.boar_done, R.raw.boar),
            Animal(R.drawable.fox, R.drawable.fox_done, R.raw.fox),
            Animal(R.drawable.squirel, R.drawable.squirel_done, R.raw.squirel)
        ), R.string.forest),

        Level(3, R.drawable.room_background_in, listOf(
            Animal(R.drawable.cat, R.drawable.cat_done, R.raw.cat),
            Animal(R.drawable.dog, R.drawable.dog_done, R.raw.dog),
            Animal(R.drawable.rabbit, R.drawable.rabbit_done, R.raw.rabbit),
            Animal(R.drawable.fish, R.drawable.fish_done, R.raw.fish),
            Animal(R.drawable.parrot, R.drawable.parrot_done, R.raw.parrot),
            Animal(R.drawable.hamster, R.drawable.hamster_done, R.raw.hamster)
        ), R.string.appartament),

        Level(4, R.drawable.jungle_backjground_in, listOf(
            Animal(R.drawable.lion, R.drawable.lion_done, R.raw.lion),
            Animal(R.drawable.elephante, R.drawable.elephante_done, R.raw.elephante),
            Animal(R.drawable.snake, R.drawable.snake_done, R.raw.snake),
            Animal(R.drawable.monkey, R.drawable.monkey_done, R.raw.monkey),
            Animal(R.drawable.frog, R.drawable.frog_done, R.raw.frog),
            Animal(R.drawable.tiger, R.drawable.tiger_done, R.raw.tiger)
        ), R.string.jungle),

        Level(5, R.drawable.sands_background_in, listOf(
            Animal(R.drawable.camel, R.drawable.camel_done, R.raw.camel),
            Animal(R.drawable.lizard, R.drawable.lizard_done, R.raw.varan),
            Animal(R.drawable.scorpion, R.drawable.scorpion_done, R.raw.scorpion),
            Animal(R.drawable.suslik, R.drawable.suslik_done, R.raw.surok),
            Animal(R.drawable.straus, R.drawable.straus_done, R.raw.octrich),
            Animal(R.drawable.hyena, R.drawable.hyena_done, R.raw.hyiena)
        ), R.string.sands),

        Level(
            id = 6,
            backgroundRes = R.drawable.ants_background_in,
            animals = listOf(
                Animal(R.drawable.bee, R.drawable.bee_done, R.raw.bee),
                Animal(R.drawable.mosquito, R.drawable.mosquito_done, R.raw.mosquito),
                Animal(R.drawable.fly, R.drawable.fly_done, R.raw.fly),
                Animal(R.drawable.cricket, R.drawable.cricket_done, R.raw.cricket),
                Animal(R.drawable.grasshopper, R.drawable.grasshopper_done, R.raw.grasshoop),
                Animal(R.drawable.bug, R.drawable.bug_done, R.raw.bug)
            ),
            titleTextRes = R.string.ants
        )
    )

    /**
     * Marks a level as completed in persistent storage.
     *
     * @param levelId The ID of the level to mark as completed
     */
    fun completeLevel(levelId: Int) {
        sharedPreferences.edit().putBoolean("level_$levelId", true).apply()
    }

    /**
     * Checks if a level has been completed.
     *
     * @param levelId The ID of the level to check
     * @return True if the level is completed, false otherwise
     */
    fun isLevelCompleted(levelId: Int): Boolean {
        return sharedPreferences.getBoolean("level_$levelId", false)
    }

    /**
     * Retrieves a level by its ID.
     *
     * @param levelId The ID of the level to fetch
     * @return The Level object or null if not found
     */
    fun getLevel(levelId: Int): Level? {
        return levels.firstOrNull { it.id == levelId }
    }

    /**
     * Gets the list of animals for a given level.
     *
     * @param levelId The ID of the level
     * @return List of animals for the level, or empty list if level not found
     */
    fun getLevelAnimals(levelId: Int): List<Animal> {
        return getLevel(levelId)?.animals ?: emptyList()
    }
}




//1000
/*
data class Animal(
    val defaultImageRes: Int,
    val clickedImageRes: Int,
    val soundRes: Int
)

data class Level(val id: Int, val backgroundRes: Int, val animals: List<Animal>, val titleTextRes: Int)

class GameRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    private val levels = listOf(
        Level(1, R.drawable.ferma_backgropund, listOf(
            Animal(R.drawable.pig, R.drawable.pig_done, R.raw.pigandvoice),
            Animal(R.drawable.duck, R.drawable.duck_done, R.raw.duckandvoice),
            Animal(R.drawable.lamb, R.drawable.lamb_done, R.raw.sheepandvoice),
            Animal(R.drawable.horse, R.drawable.horse_done, R.raw.horseandvoice),
            Animal(R.drawable.cow, R.drawable.cow_done, R.raw.cowandvoice),
            Animal(R.drawable.chicken, R.drawable.chicken_done, R.raw.chickenandvoice)
        ), R.string.ferma),

        Level(2, R.drawable.forest_backgropund_in, listOf(
            Animal(R.drawable.wolf, R.drawable.wolf_done, R.raw.wolf),
            Animal(R.drawable.owl, R.drawable.owl_done, R.raw.owl),
            Animal(R.drawable.bear, R.drawable.bear_done, R.raw.bear),
            Animal(R.drawable.boar, R.drawable.boar_done, R.raw.boar),
            Animal(R.drawable.fox, R.drawable.fox_done, R.raw.fox),
            Animal(R.drawable.squirel, R.drawable.squirel_done, R.raw.squirel)
        ), R.string.forest),

        Level(3, R.drawable.room_background_in, listOf(
            Animal(R.drawable.cat, R.drawable.cat_done, R.raw.cat),
            Animal(R.drawable.dog, R.drawable.dog_done, R.raw.dog),
            Animal(R.drawable.rabbit, R.drawable.rabbit_done, R.raw.rabbit),
            Animal(R.drawable.fish, R.drawable.fish_done, R.raw.fish),
            Animal(R.drawable.parrot, R.drawable.parrot_done, R.raw.parrot),
            Animal(R.drawable.hamster, R.drawable.hamster_done, R.raw.hamster)
        ), R.string.appartament),

        Level(4, R.drawable.jungle_backjground_in, listOf(
            Animal(R.drawable.lion, R.drawable.lion_done, R.raw.lion),
            Animal(R.drawable.elephante, R.drawable.elephante_done, R.raw.elephante),
            Animal(R.drawable.snake, R.drawable.snake_done, R.raw.snake),
            Animal(R.drawable.monkey, R.drawable.monkey_done, R.raw.monkey),
            Animal(R.drawable.frog, R.drawable.frog_done, R.raw.frog),
            Animal(R.drawable.tiger, R.drawable.tiger_done, R.raw.tiger)
        ), R.string.jungle),

        Level(5, R.drawable.sands_background_in, listOf(
            Animal(R.drawable.camel, R.drawable.camel_done, R.raw.camel),
            Animal(R.drawable.lizard, R.drawable.lizard_done, R.raw.varan),
            Animal(R.drawable.scorpion, R.drawable.scorpion_done, R.raw.scorpion),
            Animal(R.drawable.suslik, R.drawable.suslik_done, R.raw.surok),
            Animal(R.drawable.straus, R.drawable.straus_done, R.raw.octrich),
            Animal(R.drawable.hyena, R.drawable.hyena_done, R.raw.hyiena)
        ), R.string.sands),

        Level(6, R.drawable.ants_background_in, listOf(
            Animal(R.drawable.bee, R.drawable.bee_done, R.raw.bee),
            Animal(R.drawable.mosquito, R.drawable.mosquito_done, R.raw.mosquito),
            Animal(R.drawable.fly, R.drawable.fly_done, R.raw.fly),
            Animal(R.drawable.cricket, R.drawable.cricket_done, R.raw.cricket),
            Animal(R.drawable.grasshopper, R.drawable.grasshopper_done, R.raw.grasshoop),
            Animal(R.drawable.bug, R.drawable.bug_done, R.raw.bug)
        ), R.string.ants),
    )

    fun completeLevel(level: Int) {
        sharedPreferences.edit().putBoolean("level_$level", true).apply()
    }
    fun isLevelCompleted(level: Int): Boolean {
        return sharedPreferences.getBoolean("level_$level", false)
    }

   fun getLevel(levelId: Int): Level? {
       return levels.firstOrNull { it.id == levelId }
   }

    fun getLevelAnimals(levelId: Int): List<Animal> {
        return getLevel(levelId)?.animals ?: emptyList()
    }
}
*/
