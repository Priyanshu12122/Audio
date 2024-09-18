package com.example.audio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.audio.ui.AudioRecorderApp
import com.example.audio.ui.theme.AudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.FOREGROUND_SERVICE_MICROPHONE,
                android.Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        enableEdgeToEdge()
        setContent {
            AudioTheme {
                AudioRecorderApp()
            }
        }
    }
}
