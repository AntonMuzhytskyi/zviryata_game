package com.am.zviryata.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.am.zviryata.R
import com.am.zviryata.viewmodel.GameViewModel


@Composable
fun MenuScreen(gameViewModel: GameViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        for (level in 1..10) {
            val isCompleted = gameViewModel.isLevelCompleted(level)
            //val imageRes = if (isCompleted) R.drawable.level_completed else R.drawable.level_locked

            val imageRes = if (isCompleted) R.drawable.level_room else R.drawable.level_room


            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clickable { /* TODO: Открыть уровень */ }
            )
        }
    }
}