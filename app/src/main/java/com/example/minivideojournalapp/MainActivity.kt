package com.example.minivideojournalapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.minivideojournalapp.resentation.common.MediaPermissionRequestAlert
import com.example.minivideojournalapp.resentation.mainscreen.MainScreen
import com.example.minivideojournalapp.ui.theme.MiniVideoJournalAppTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		requestPermissions(
			arrayOf(
				Manifest.permission.CAMERA,
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.READ_EXTERNAL_STORAGE
			),
			0
		)



		setContent {
			MiniVideoJournalAppTheme {
				Surface(modifier = Modifier.fillMaxSize()) {
					CheckPermission()
				}
			}
		}
	}
}

@Composable
fun CheckPermission() {
	var hasPermission by remember { mutableStateOf(false) }

	MediaPermissionRequestAlert(
		onGranted = {
			hasPermission = true
		}
	)

	if (hasPermission) {
		MainScreen()
	}
}
