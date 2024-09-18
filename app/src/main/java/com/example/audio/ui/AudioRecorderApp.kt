package com.example.audio.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.audio.AudioRecorderService
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AudioRecorderApp() {
    var isRecording by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                isRecording = !isRecording
                val intent = Intent(context, AudioRecorderService::class.java).apply {
                    action = AudioRecorderService.ACTION_START_STOP_RECORDING
                }
                if (isRecording) {
                    context.startForegroundService(intent)
                } else {
                    context.stopService(intent)
                }
            }
        ) {
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }

        AudioFileList()

    }
}


@Composable
fun AudioFileList() {
    val context = LocalContext.current
    var audioFiles by remember { mutableStateOf(listOf<File>()) }
    var currentlyPlaying by remember { mutableStateOf<File?>(null) }
    val mediaPlayer = remember { MediaPlayer() }

    LaunchedEffect(Unit) {
        audioFiles = context.getExternalFilesDir(null)
            ?.listFiles { file -> file.extension == "mp4" }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    Column {
        Text("Recorded Audio Files:", style = MaterialTheme.typography.headlineSmall)
        LazyColumn {
            items(audioFiles) { file ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = file.name,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (currentlyPlaying == file) {
                                mediaPlayer.stop()
                                currentlyPlaying = null
                            } else {
                                mediaPlayer.reset()
                                mediaPlayer.setDataSource(file.path)
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                                currentlyPlaying = file
                            }
                        }
                    ) {
                        Text(if (currentlyPlaying == file) "Stop" else "Play")
                    }
                }
            }
        }
    }
}