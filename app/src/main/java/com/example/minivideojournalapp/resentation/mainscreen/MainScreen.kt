package com.example.minivideojournalapp.resentation.mainscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minivideojournalapp.db.VideoEntity
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
	Text("Main Screen")

	val viewModel = koinViewModel<MainScreenViewModel>()

	val context = LocalContext.current

	val videos = viewModel._videos.collectAsState(initial = emptyList())


	val launcher = rememberLauncherForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			val uri = result.data?.data ?: return@rememberLauncherForActivityResult
			val path = getPathFromUri(context, uri)
			val createdAt = System.currentTimeMillis()
			if (path != null) {
				viewModel.addVideo(
					null,
					path,
					"Recorded at $createdAt",
					createdAt
				)
			}
		}
	}

	CameraScreenContent(
		videos = videos.value,
		floatButtonOnClick = { videoIntent -> launcher.launch(videoIntent) }
	)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraScreenContent(
	videos: List<VideoEntity>,
	floatButtonOnClick: (Intent) -> Unit,
) {
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(onClick = {
				val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
				floatButtonOnClick(videoIntent)
			}) {
				Icon(Icons.Default.ThumbUp, contentDescription = "Record Video")
			}
		}
	) {
		LazyColumn(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxSize()
				.background(Color(0xFFF9F9F9))
		) {
			items(videos.size, key = { index -> videos[index].id }) { index ->
				val video = videos[index]

				Card(
					modifier = Modifier
						.padding(8.dp)
						.fillMaxWidth(),
					elevation = CardDefaults.cardElevation(4.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Column(modifier = Modifier.padding(8.dp)) {

						Box(
							modifier = Modifier
								.fillMaxSize()
								.background(Color.Black)
						) {
							//VideoThumbnail(video.videoPath) { }
							Text("Place for video")
						}


						Spacer(Modifier.height(8.dp))

						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								text = video.description ?: "No description",
								fontWeight = FontWeight.Bold
							)
						}

					}
				}
			}
		}
	}
}


private fun getPathFromUri(context: Context, uri: Uri): String? {
	val projection = arrayOf(MediaStore.Video.Media.DATA)
	context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
		val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
		cursor.moveToFirst()
		return cursor.getString(columnIndex)
	}
	return null
}


@Preview
@Composable
fun CameraScreenPreview() {
	CameraScreenContent(
		videos = listOf(
			VideoEntity(1, "video1.mp4", "Description 1", 11125L),
			VideoEntity(2, "video2.mp4", "Description 2", 121321L)
		),
		floatButtonOnClick = {},
	)

}