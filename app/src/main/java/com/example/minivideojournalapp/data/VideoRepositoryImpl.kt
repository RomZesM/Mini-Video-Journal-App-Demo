package com.example.minivideojournalapp.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.minivideojournalapp.db.VideoDB
import com.example.minivideojournalapp.domain.models.Video
import com.example.minivideojournalapp.domain.repositories.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class VideoRepositoryImpl(
	database: VideoDB,
	private val context: Context
) : VideoRepository {

	private val videoQueries = database.videoEntityQueries

	override suspend fun saveVideo(id: Long?, videoPath: String, description: String?, createdAt: Long) {
		withContext(Dispatchers.IO) {
			val thumbnailBitmap = generateThumbnail(videoPath)

			val thumbnailPath : String = saveThumbnailToStorage(thumbnailBitmap, videoPath) ?: ""

			videoQueries.insertVideo(id, videoPath, description, thumbnailPath, createdAt)
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
						thumbnailPath = entity.thumbnailPath,
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
					thumbnailPath = entity.thumbnailPath,
					createdAt = entity.createdAt
				)
			}
	}


	override suspend fun updateDescription(id: Long, description: String) {
		withContext(Dispatchers.IO){
			videoQueries.updateDescription(description, id)
		}
	}

	private fun generateThumbnail(videoPath: String): Bitmap? {
		val retriever = MediaMetadataRetriever()
		return try {
			retriever.setDataSource(videoPath)
			retriever.getFrameAtTime(0)
		} catch (e: Exception) {
			null
		} finally {
			retriever.release()
		}
	}

	private fun saveThumbnailToStorage(bitmap: Bitmap?, videoPath: String): String? {
		if (bitmap == null) return null

		return try {
		val fileName = "thumb_${File(videoPath).nameWithoutExtension}.jpg"
		val directory = File(context.filesDir, "thumbnails")
		if (!directory.exists()) directory.mkdirs()

		val file = File(directory, fileName)
		FileOutputStream(file).use {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
		}

		file.absolutePath
		}
		catch (e: Exception){
			e.printStackTrace()
			null
		}
	}
}