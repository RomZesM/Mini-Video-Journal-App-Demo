package com.example.minivideojournalapp.domain.repositories

import com.example.minivideojournalapp.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

	suspend fun saveVideo( id: Long? = null, videoPath: String, description: String?, createdAt: Long)
	fun getAllVideos(): Flow<List<Video>>
	suspend fun getVideoById(id: Long): Video?
	suspend fun updateDescription(id: Long, description: String)

}