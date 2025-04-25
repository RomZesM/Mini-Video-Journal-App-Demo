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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.minivideojournalapp.domain.models.Video
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
	Text("Main Screen")

	val viewModel = koinViewModel<MainScreenViewModel>()

	val context = LocalContext.current

	val videos = viewModel.videos.collectAsState(initial = emptyList())

	var currentlyPlayingId by remember { mutableStateOf<Long?>(null) }

	var videosIndexForEditing by remember { mutableIntStateOf(0) }

	var isEditingDescription by remember { mutableStateOf(false) }

	val exoPlayer = remember() {
		ExoPlayer.Builder(context).build().apply {
			prepare()
			playWhenReady = true
		}
	}

	DisposableEffect(Unit) {
		onDispose {
			exoPlayer.release()
		}
	}

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
		floatButtonOnClick = { videoIntent -> launcher.launch(videoIntent) },
		onEditButtonClick = { videoIndex ->
			isEditingDescription = true
			videosIndexForEditing = videoIndex
		},
		exoPlayer = exoPlayer,
		onPlayClicked = { video ->
			currentlyPlayingId = video.id
			val mediaItem = MediaItem.fromUri(Uri.parse(video.videoPath))
			exoPlayer.setMediaItem(mediaItem)
			exoPlayer.prepare()
			exoPlayer.playWhenReady = true
		},
		currentlyPlayingId = currentlyPlayingId
	)

	if (isEditingDescription) {
		EditDescriptionDialog(
			currentDescription = videos.value[videosIndexForEditing].description ?: "",
			onConfirm = { newDescription ->
				viewModel.updateDescription(videos.value[videosIndexForEditing].id, newDescription)
				isEditingDescription = false
			},
			onDismiss = { isEditingDescription = false }
		)
	}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraScreenContent(
	videos: List<Video>,
	floatButtonOnClick: (Intent) -> Unit,
	onEditButtonClick: (Int) -> Unit,
	exoPlayer: ExoPlayer,
	onPlayClicked: (Video) -> Unit,
	currentlyPlayingId: Long?
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

						Card(
							modifier = Modifier
								.padding(8.dp)
								.fillMaxWidth(),
							elevation = CardDefaults.cardElevation(4.dp),
							shape = RoundedCornerShape(12.dp)
						) {
							Column(modifier = Modifier.padding(8.dp)) {

								if (currentlyPlayingId == video.id) {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.aspectRatio(16f / 9f)
											.clip(RoundedCornerShape(12.dp))
									) {
										AndroidView(
											modifier = Modifier.align(Alignment.Center),
											factory = {
												PlayerView(it).apply {
													useController = true
													player = exoPlayer
												}
											},
										)
									}

								} else {
									Box(
										modifier = Modifier
											.fillMaxSize()
											.height(100.dp)
											.background(Color.Black)
											.clickable { onPlayClicked(video) }
									) {
										Text("Thumbnails")
									}
								}

								Spacer(Modifier.height(8.dp))

								Row(
									modifier = Modifier.fillMaxWidth(),
									horizontalArrangement = Arrangement.SpaceBetween,
									verticalAlignment = Alignment.CenterVertically
								) {
									Text(
										text = video.description,
										fontWeight = FontWeight.Bold
									)
									IconButton(
										onClick = {
											onEditButtonClick(index)
										},
										modifier = Modifier.size(24.dp)
									) {
										Icon(
											Icons.Default.Create,
											contentDescription = "Edit description"
										)
									}
								}

							}
						}
					}
				}
			}
		}
	}
}


fun getPathFromUri(context: Context, uri: Uri): String? {
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
			Video(123, "path1", "description1", 1234567890),
			Video(456, "path2", "description2", 9876543210),
			Video(789, "path3", "description3", 5432109876)
		),
		floatButtonOnClick = {},
		onEditButtonClick = {},
		exoPlayer = ExoPlayer.Builder(LocalContext.current).build(),
		onPlayClicked = {},
		null
	)
}

