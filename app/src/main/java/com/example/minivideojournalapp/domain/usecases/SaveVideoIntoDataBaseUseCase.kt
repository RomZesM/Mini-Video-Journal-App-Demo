package com.example.minivideojournalapp.domain.usecases

import com.example.minivideojournalapp.domain.repositories.VideoRepository

class SaveVideoIntoDataBaseUseCase (
	private val videoRepository: VideoRepository
) {
	suspend operator fun invoke(id: Long?, path: String, description: String, createdAt: Long) {
		videoRepository.saveVideo(id, path, description, createdAt)
	}

}