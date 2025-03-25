package com.am.zviryata

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import com.am.zviryata.ui.theme.ZviryataTheme

import android.content.res.Configuration
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        MobileAds.initialize(this) {}

        setContent {
            MainMenuScreen()
        }
    }
}

@Composable
fun MainMenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Звірята",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LevelGrid()
        Spacer(modifier = Modifier.weight(1f))
        //AdMobBanner()
    }
}
@Composable
fun LevelGrid() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.padding(8.dp)) {
            LevelButton("Уровень 1", R.drawable.level_room)
            LevelButton("Уровень 2", R.drawable.level_room)
            LevelButton("Уровень 3", R.drawable.level_room)
        }
        Row(modifier = Modifier.padding(8.dp)) {
            LevelButton("Уровень 4", R.drawable.level_room)
            LevelButton("Уровень 5", R.drawable.level_room)
        }
    }
}

@Composable
fun LevelButton(levelName: String, imageRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* TODO: Запуск уровня*/  }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = levelName,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = levelName, fontSize = 18.sp)
    }
}




@Composable
fun AdMobBanner() {
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // ca-app-pub-4687771932576156/6389942662    acc ca-app-pub-4687771932576156~3385371346
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}



/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZviryataTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZviryataTheme {
        Greeting("Android")
    }
}*/
