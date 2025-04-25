package com.example.minivideojournalapp.resentation.common

import android.Manifest
import android.os.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.minivideojournalapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPermissionRequestAlert(
	onGranted: () -> Unit
) {

	val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		Manifest.permission.READ_MEDIA_VIDEO
	} else {
		Manifest.permission.READ_EXTERNAL_STORAGE
	}

	val permissionState = rememberPermissionState(permission)
	var showDialog by remember { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		if (!permissionState.status.isGranted) {
			showDialog = true
		} else {
			onGranted()
		}
	}

	if (showDialog) {
		AlertDialog(
			onDismissRequest = { },
			title = { Text(stringResource(R.string.permission_alert_permission_needed_title)) },
			text = { Text(stringResource(R.string.permission_alert_permission_needed_message)) },
			confirmButton = {
				TextButton(onClick = {
					permissionState.launchPermissionRequest()
					showDialog = false
				}) {
					Text(stringResource(R.string.permission_alert_button_title_allow))
				}
			},
			dismissButton = {
				TextButton(onClick = {
					showDialog = false
				}) {
					Text(stringResource(R.string.permission_alert_button_title_cancel))
				}
			}
		)
	}

	LaunchedEffect(permissionState.status) {
		if (permissionState.status.isGranted) {
			onGranted()
		}
	}
}

@Preview
@Composable
fun PermissionDialogPreview() {
	MediaPermissionRequestAlert { }
}