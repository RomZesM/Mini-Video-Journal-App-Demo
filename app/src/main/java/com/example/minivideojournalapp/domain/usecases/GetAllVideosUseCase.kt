package com.example.minivideojournalapp.domain.usecases

import com.example.minivideojournalapp.domain.models.Video
import com.example.minivideojournalapp.domain.repositories.VideoRepository
import kotlinx.coroutines.flow.Flow

class GetAllVideosUseCase(
	private val videoRepository: VideoRepository
) {

	fun invoke(): Flow<List<Video>> {
		return videoRepository.getAllVideos()
	}

}