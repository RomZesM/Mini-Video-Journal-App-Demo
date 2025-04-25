package com.example.minivideojournalapp.resentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minivideojournalapp.data.VideoDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainScreenViewModel(
	private val videoDataSource: VideoDataSource
) : ViewModel() {


	var _videos = videoDataSource.getAllVideos()
		private set


	fun addVideo(id: Long?, path: String, description: String, createdAt: Long) {
		viewModelScope.launch(Dispatchers.IO) {
			videoDataSource.saveVideo(id, path, description, createdAt)
		}

	}

}