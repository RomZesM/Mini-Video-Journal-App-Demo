package com.example.minivideojournalapp.data

import com.example.minivideojournalapp.db.VideoEntity
import kotlinx.coroutines.flow.Flow

interface VideoDataSource  {

	suspend fun saveVideo( id: Long? = null, videoPath: String, description: String?, createdAt: Long)
	fun getAllVideos(): Flow<List<VideoEntity>>
	suspend fun getVideoById(id: Long): VideoEntity?
	suspend fun deleteVideoById(id: Long)
	suspend fun updateDescription(id: Long, description: String)

}