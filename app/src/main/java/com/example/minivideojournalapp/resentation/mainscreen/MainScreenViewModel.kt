package com.example.minivideojournalapp.resentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minivideojournalapp.domain.usecases.UpdateVideoDescriptionUseCase
import com.example.minivideojournalapp.domain.usecases.GetAllVideosUseCase
import com.example.minivideojournalapp.domain.usecases.SaveVideoIntoDataBaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainScreenViewModel(
	private val getAllVideosUseCase: GetAllVideosUseCase,
	private val saveVideoIntoDataBaseUseCase: SaveVideoIntoDataBaseUseCase,
	private val updateVideoDescriptionUseCase: UpdateVideoDescriptionUseCase
) : ViewModel() {


	var videos = getAllVideosUseCase.invoke()
		private set


	fun addVideo(id: Long?, path: String, description: String, createdAt: Long) {
		viewModelScope.launch(Dispatchers.IO) {
			saveVideoIntoDataBaseUseCase(id, path, description, createdAt)
		}

	}

	fun updateDescription(id: Long, newDescription: String) {
		viewModelScope.launch(Dispatchers.IO) {
			updateVideoDescriptionUseCase(id, newDescription)
		}
	}

}