package com.am.zviryata.ui.theme

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.am.zviryata.R
import com.am.zviryata.viewmodel.GameViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.FullScreenContentCallback
import kotlinx.coroutines.delay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.zviryata.repository.GameRepository
import com.am.zviryata.services.AdManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.am.zviryata.viewmodel.LevelState

/**
 * Composable function for displaying the level screen UI.
 * Observes ViewModel state and renders UI accordingly, following MVVM.
 *
 * @param viewModel The GameViewModel instance managing game state
 * @param levelId The current level ID
 * @param onBack Callback to navigate back
 * @param onNext Callback to proceed to the next level or menu
 */
@Composable
fun LevelScreen(
    viewModel: GameViewModel,
    levelId: Int,
    onBack: () -> Unit,
    onNext: (Int) -> Unit
) {
    val context = LocalContext.current
    val levelState by viewModel.levelState.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()
    val clickedAnimals by viewModel.animalsClicked.collectAsState()
    val levelData = viewModel.gameRepository.getLevel(currentLevel)
    val adManager = remember { AdManager(context) }

    // Set the current level when levelId changes
    LaunchedEffect(levelId) {
        viewModel.setCurrentLevel(levelId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = levelData?.backgroundRes?.let { painterResource(id = it) }
                ?: painterResource(id = R.drawable.background_main),
            contentDescription = "Level Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top navigation bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.resetLevel(); onBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Back to Menu",
                        tint = colorResource(id = R.color.violet),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Text(
                    text = stringResource(id = levelData?.titleTextRes ?: R.string.jungle),
                    color = colorResource(id = R.color.violet),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                if (levelState is LevelState.Completed) {
                    IconButton(onClick = { viewModel.proceedToNextLevel() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next Level",
                            tint = colorResource(id = R.color.violet),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            // Animal grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(levelData?.animals?.indices?.toList() ?: emptyList()) { index ->
                    val animal = levelData?.animals?.get(index)
                    val clicked = clickedAnimals.contains(index)

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Transparent)
                            .clickable {
                                viewModel.onAnimalClicked(index)
                                if (animal != null) {
                                    MediaPlayer.create(context, animal.soundRes)?.apply {
                                        setOnCompletionListener { release() }
                                        start()
                                    }
                                }
                            }
                    ) {
                        if (animal != null) {
                            Image(
                                painter = painterResource(
                                    id = if (clicked) animal.clickedImageRes else animal.defaultImageRes
                                ),
                                contentDescription = "Animal Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        // Celebration animation
        if (levelState is LevelState.Celebrating) {
            CelebrationAnimation(visible = true, modifier = Modifier.fillMaxSize())
        }

        // Banner ad at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { adManager.createBannerAdView() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }

        // Handle navigation based on state
        LaunchedEffect(levelState) {
            when (val state = levelState) {
                is LevelState.GameCompleted -> onNext(0) // Back to menu
                is LevelState.Completed -> Unit // Wait for user to click next
                is LevelState.ShowingAd -> onNext(state.nextLevelId) // Proceed after ad
                else -> Unit
            }
        }
    }
}

/**
 * Placeholder for celebration animation (to be implemented).
 */

@Composable
fun CelebrationAnimation(visible: Boolean, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val progress by animateLottieCompositionAsState(composition)

    AnimatedVisibility(visible = visible) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = modifier
        )
    }
}









/**
 * Composable function that displays the level screen for the game.
 * Handles game logic, UI rendering, advertisements, and navigation between levels.
 *
 * @param viewModel The GameViewModel instance for managing game state
 * @param onBack Callback to navigate back to previous screen
 * @param onNext Callback to navigate to next level with the given level ID
 * @param levelId The current level identifier
 *//*
@Composable
fun LevelScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit,
    onNext: (Int) -> Unit,
    levelId: Int
) {
    // Core context and state management
    val context = LocalContext.current
    val interstitialAd = remember { mutableStateOf<InterstitialAd?>(null) }
    val sharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    // UI state variables
    var showCelebration by remember { mutableStateOf(false) }
    var levelCompleted by remember { mutableStateOf(false) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    // Load interstitial ad when the screen is first composed
    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712", // Test ad unit ID
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd.value = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd.value = null
                    Log.w("LevelScreen", "Failed to load interstitial ad: ${error.message}")
                }
            }
        )
    }

    // Update ViewModel with current level when levelId changes
    LaunchedEffect(levelId) {
        viewModel.setCurrentLevel(levelId)
    }

    // Collect reactive state from ViewModel
    val level by viewModel.currentLevel.collectAsState()
    val levelData = viewModel.gameRepository.getLevel(level)
    val clickedAnimals by viewModel.animalsClicked.collectAsState()

    // Handle level completion logic
    LaunchedEffect(clickedAnimals.size) {
        if (clickedAnimals.size == (levelData?.animals?.size ?: 0)) {
            delay(3000) // Wait 3 seconds before showing celebration
            showCelebration = true
            mediaPlayer.value?.release()
            mediaPlayer.value = MediaPlayer.create(context, R.raw.win).apply {
                setOnCompletionListener {
                    release()
                    showCelebration = false
                    levelCompleted = true
                    // Save level completion status
                    with(sharedPreferences.edit()) {
                        putBoolean("level_$levelId", true)
                        apply()
                    }
                }
                start()
            }
        }
    }

    // Main UI layout
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = levelData?.backgroundRes?.let { painterResource(id = it) }
                ?: painterResource(id = R.drawable.background_main),
            contentDescription = "Level Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar with navigation and level title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button
                IconButton(onClick = {
                    viewModel.resetLevel()
                    onBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu Button",
                        tint = colorResource(id = R.color.violet),
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Level title
                Text(
                    text = stringResource(id = levelData?.titleTextRes ?: R.string.jungle),
                    color = colorResource(id = R.color.violet),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Next level button (shown only when level is completed)
                if (levelCompleted) {
                    IconButton(onClick = {
                        viewModel.resetLevel()
                        interstitialAd.value?.let { ad ->
                            ad.setFullScreenContentCallback(object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    onNext(levelId + 1)
                                    interstitialAd.value = null
                                }

                                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                                    onNext(levelId + 1)
                                    interstitialAd.value = null
                                }
                            })
                            ad.show(context as Activity)
                        } ?: onNext(levelId + 1)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next Level",
                            tint = colorResource(id = R.color.violet),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            // Animal grid display
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(levelData?.animals?.indices?.toList() ?: emptyList()) { index ->
                    val animal = levelData?.animals?.getOrNull(index) ?: return@items
                    val clicked = clickedAnimals.contains(index)

                    // Animal clickable item
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Transparent)
                            .clickable {
                                viewModel.onAnimalClicked(index)
                                MediaPlayer.create(context, animal.soundRes)?.apply {
                                    setOnCompletionListener { it.release() }
                                    start()
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = if (clicked) animal.clickedImageRes else animal.defaultImageRes),
                            contentDescription = "Animal Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Celebration animation when level is completed
        if (showCelebration) {
            CelebrationAnimation(
                visible = true,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Bottom banner ad
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                factory = { ctx ->
                    AdView(ctx).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test banner ad unit ID
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}*/



//1000
/*
@Composable
fun LevelScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit,
    onNext: (Int) -> Unit,
    levelId: Int
) {
    val context = LocalContext.current
    val interstitialAd = remember { mutableStateOf<InterstitialAd?>(null) }
    val sharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    var showCelebration by remember { mutableStateOf(false) }
    var levelCompleted by remember { mutableStateOf(false) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd.value = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd.value = null
                    Log.w("LevelScreen", "Ad failed to load: ${error.message}")
                }
            }
        )
    }

    LaunchedEffect(levelId) {
        viewModel.setCurrentLevel(levelId)
    }

    val level by viewModel.currentLevel.collectAsState()
    val levelData = viewModel.gameRepository.getLevel(level)
    val clickedAnimals by viewModel.animalsClicked.collectAsState()

    LaunchedEffect(clickedAnimals.size) {
        if (clickedAnimals.size == (levelData?.animals?.size ?: 0)) {
            delay(3000)
            showCelebration = true
            mediaPlayer.value?.release()
            mediaPlayer.value = MediaPlayer.create(context, R.raw.win).apply {
                setOnCompletionListener {
                    release()
                    showCelebration = false
                    levelCompleted = true
                    with(sharedPreferences.edit()) {
                        putBoolean("level_$levelId", true)
                        apply()
                    }
                }
                start()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = levelData?.backgroundRes?.let { painterResource(id = it) }
                ?: painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    viewModel.resetLevel()
                    onBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "menu",
                        tint = colorResource(id = R.color.violet),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Text(
                    text = stringResource(id = levelData?.titleTextRes ?: R.string.jungle),
                    color = colorResource(id = R.color.violet),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                if (levelCompleted) {
                    IconButton(onClick = {
                        viewModel.resetLevel()
                        interstitialAd.value?.let { ad ->
                            ad.setFullScreenContentCallback(object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    onNext(levelId + 1)
                                    interstitialAd.value = null
                                }

                                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                                    onNext(levelId + 1)
                                    interstitialAd.value = null
                                }
                            })
                            ad.show(context as Activity)
                        } ?: run {
                            onNext(levelId + 1)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next level",
                            tint = colorResource(id = R.color.violet),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(levelData?.animals?.indices?.toList() ?: emptyList()) { index ->
                    val animal = levelData?.animals?.getOrNull(index) ?: return@items
                    val clicked = clickedAnimals.contains(index)

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Transparent)
                            .clickable {
                                viewModel.onAnimalClicked(index)
                                MediaPlayer.create(context, animal.soundRes)?.apply {
                                    setOnCompletionListener { it.release() }
                                    start()
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = if (clicked) animal.clickedImageRes else animal.defaultImageRes),
                            contentDescription = "Animal Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        if (showCelebration) {
            CelebrationAnimation(visible = true, modifier = Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}
*/
