package com.example.minivideojournalapp.domain.models

data class Video (
	val id: Long,
	val videoPath: String,
	val description: String,
	val createdAt: Long
)

