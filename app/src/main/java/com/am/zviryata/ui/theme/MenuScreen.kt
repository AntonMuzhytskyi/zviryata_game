package com.am.zviryata.ui.theme


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.am.zviryata.R
import com.am.zviryata.model.LevelData
import com.am.zviryata.repository.GameRepository
import com.am.zviryata.services.AdManager
import com.am.zviryata.viewmodel.GameViewModel
import com.am.zviryata.viewmodel.GameViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Composable function for displaying the main menu screen.
 * Renders a grid of levels and handles navigation, adhering to MVVM.
 *
 * @param navController Navigation controller for routing between screens
 * @param gameViewModel ViewModel for accessing game state
 */
@Composable
fun MenuScreen(
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)
    val adManager = remember { AdManager(context) }

    // Background music management
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.menu_sound).apply {
            isLooping = true
            setVolume(0.5f, 0.5f)
        }
    }

    DisposableEffect(Unit) {
        mediaPlayer.start()
        onDispose { mediaPlayer.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Menu Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.zviryata),
                    contentDescription = "Zviryata Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(800.dp, 70.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.close_btn),
                    contentDescription = "Close Game",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                        .size(40.dp)
                        .clickable { mediaPlayer.stop(); (context as? Activity)?.finish() }
                )
            }

            // Level grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(getMenuLevels()) { levelData ->
                    val isCompleted = sharedPreferences.getBoolean("level_${levelData.id}", false)
                    val imageRes = if (isCompleted) levelData.imageRes else levelData.lockedImageRes

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                mediaPlayer.stop()
                                navController.navigate("level/${levelData.id}")
                            }
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Gray)
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = levelData.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = levelData.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = FontFamily.Cursive,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Banner ad at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { adManager.createBannerAdView() },
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp)
                )
            }
        }
    }
}

// Helper function to define menu levels
private fun getMenuLevels() = listOf(
    LevelData(1, R.drawable.level_farm_done, R.drawable.level_farm, "Ферма"),
    LevelData(2, R.drawable.level_forest_done, R.drawable.level_forest, "Ліс"),
    LevelData(3, R.drawable.level_room_done, R.drawable.level_room, "Кімната"),
    LevelData(4, R.drawable.level_jungle_done, R.drawable.level_jungle, "Джунглі"),
    LevelData(5, R.drawable.level_sends_done, R.drawable.level_sends, "Пустеля"),
    LevelData(6, R.drawable.level_ants_done, R.drawable.level_ants, "Комахи")
)

//1000
/*
@Composable
fun MenuScreen(
    navController: NavController,
    gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(GameRepository(LocalContext.current)))
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    val activity = (LocalContext.current as? Activity)

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.menu_sound).apply {
            isLooping = true // Зациклить музыку
            setVolume(0.5f, 0.5f) // Установить громкость
        }
    }

    // Воспроизведение музыки при входе и остановка при выходе
    DisposableEffect(Unit) {
        mediaPlayer.start()
        onDispose {
            mediaPlayer.release() // Освобождаем ресурсы
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.zviryata),
                            contentDescription = "Zviryata Logo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(800.dp, 70.dp),
                            contentScale = ContentScale.Fit
                        )

                        IconButton(
                            onClick = {
                                mediaPlayer.stop()
                                activity?.finish()
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp, top = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.close_btn),
                                contentDescription = "Close Game",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listOf(
                            LevelData(1, R.drawable.level_farm_done, R.drawable.level_farm, "Ферма"),
                            LevelData(2, R.drawable.level_forest_done, R.drawable.level_forest, "Ліс"),
                            LevelData(3, R.drawable.level_room_done, R.drawable.level_room, "Кімната"),
                            LevelData(4, R.drawable.level_jungle_done, R.drawable.level_jungle, "Джунглі"),
                            LevelData(5, R.drawable.level_sends_done, R.drawable.level_sends, "Пустеля"),
                            LevelData(6, R.drawable.level_ants_done, R.drawable.level_ants, "Комахи")
                        )) { levelData ->
                            val isCompleted = sharedPreferences.getBoolean("level_${levelData.id}", false)
                            val imageRes = if (isCompleted) levelData.imageRes else levelData.lockedImageRes

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        mediaPlayer.stop()
                                        if (isCompleted) {
                                            showInterstitialAd {
                                                navController.navigate("level/${levelData.id}")
                                            }
                                        } else {
                                            navController.navigate("level/${levelData.id}")
                                        }
                                    }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.Gray)
                                ) {
                                    Image(
                                        painter = painterResource(id = imageRes),
                                        contentDescription = levelData.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    text = levelData.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    fontFamily = FontFamily.Cursive,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 320.dp, height = 50.dp)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { context ->
                            AdView(context).apply {
                                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                setAdSize(AdSize.BANNER)
                                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                                loadAd(AdRequest.Builder().build())
                            }
                        },
                        modifier = Modifier.size(width = 320.dp, height = 50.dp)
                    )
                }
            }
        }
    }
}

fun showInterstitialAd(onAdFinished: () -> Unit) {
    onAdFinished()
}
*/
