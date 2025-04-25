package com.example.minivideojournalapp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.minivideojournalapp.db.VideoDB
import com.example.minivideojournalapp.db.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VideoDataSourceImpl(
	database: VideoDB
) : VideoDataSource {

	private val videoQueries = database.videoEntityQueries

	override suspend fun saveVideo(id: Long?, videoPath: String, description: String?, createdAt: Long) {
		withContext(Dispatchers.IO) {
			videoQueries.insertVideo(id, videoPath, description, createdAt)
		}
	}

	override fun getAllVideos(): Flow<List<VideoEntity>> {
		return videoQueries.selectAllVideos().asFlow().mapToList(Dispatchers.IO)
	}

	override suspend fun getVideoById(id: Long): VideoEntity? {
		return videoQueries.selectVideoById(id).executeAsOneOrNull()
	}

	override suspend fun deleteVideoById(id: Long) {
		withContext(Dispatchers.IO){
			videoQueries.deleteVideo(id)
		}
	}

	override suspend fun updateDescription(id: Long, description: String) {
		withContext(Dispatchers.IO){
			videoQueries.updateDescription(description, id)
		}
	}
}