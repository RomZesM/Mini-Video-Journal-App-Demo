package com.example.minivideojournalapp.domain.usecases

import com.example.minivideojournalapp.domain.repositories.VideoRepository

class UpdateVideoDescriptionUseCase(
	private val videoRepository: VideoRepository

) {
	suspend operator fun invoke(id: Long, newDescription: String) {
		val video = videoRepository.getVideoById(id)
		videoRepository.updateDescription(id, newDescription)
	}

}