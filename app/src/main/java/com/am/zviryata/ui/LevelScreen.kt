package com.am.zviryata.ui

import android.media.MediaPlayer
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
import com.am.zviryata.services.AdManager
import com.am.zviryata.viewmodel.LevelState
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.delay

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

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
    val adView = remember { AdManager(context).createBannerAdView() }
    var localLevelState by remember { mutableStateOf<LevelState>(LevelState.Initial) }
    var showCelebration by remember { mutableStateOf(false) }
    var levelCompleted by remember { mutableStateOf(false) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(Unit) {
        adView.loadAd(AdRequest.Builder().build())
    }

    LaunchedEffect(clickedAnimals.size) {
        if (clickedAnimals.size == (levelData?.animals?.size ?: 0) && !showCelebration) {
            delay(3000)
            showCelebration = true
            mediaPlayer.value?.release()
            mediaPlayer.value = MediaPlayer.create(context, R.raw.win).apply {
                setOnCompletionListener {
                    release()
                    showCelebration = false
                    levelCompleted = true
                    viewModel.gameRepository.completeLevel(currentLevel)
                    localLevelState = LevelState.Completed
                }
                start()
            }
        }
    }

    LaunchedEffect(levelId) {
        viewModel.setCurrentLevel(levelId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = levelData?.backgroundRes?.let { painterResource(id = it) }
                ?: painterResource(id = R.drawable.background_main),
            contentDescription = "Level Background",
            contentScale = ContentScale.FillBounds,
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

                if (levelCompleted) {
                    IconButton(onClick = {
                        levelCompleted = false
                        viewModel.proceedToNextLevel()
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

        if (showCelebration) {
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
                factory = { adView },
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
