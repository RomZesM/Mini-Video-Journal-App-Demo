package com.example.minivideojournalapp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.minivideojournalapp.db.VideoDB
import com.example.minivideojournalapp.db.VideoEntity
import com.example.minivideojournalapp.domain.models.Video
import com.example.minivideojournalapp.domain.repositories.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VideoRepositoryImpl(
	database: VideoDB
) : VideoRepository {

	private val videoQueries = database.videoEntityQueries

	override suspend fun saveVideo(id: Long?, videoPath: String, description: String?, createdAt: Long) {
		withContext(Dispatchers.IO) {
			videoQueries.insertVideo(id, videoPath, description, createdAt)
		}
	}

	override fun getAllVideos(): Flow<List<Video>> {
		return videoQueries.selectAllVideos()
			.asFlow()
			.mapToList(Dispatchers.IO)
			.map { entityList ->
				entityList.map { entity ->
					Video(
						id = entity.id,
						videoPath = entity.videoPath,
						description = entity.description ?: "",
						createdAt = entity.createdAt
					)
				}
			}
	}

	override suspend fun getVideoById(id: Long): Video? {
		return videoQueries
			.selectVideoById(id)
			.executeAsOneOrNull()
			?.let { entity ->
				Video(
					id = entity.id,
					videoPath = entity.videoPath,
					description = entity.description ?: "",
					createdAt = entity.createdAt
				)
			}
	}


	override suspend fun updateDescription(id: Long, description: String) {
		withContext(Dispatchers.IO){
			videoQueries.updateDescription(description, id)
		}
	}
}