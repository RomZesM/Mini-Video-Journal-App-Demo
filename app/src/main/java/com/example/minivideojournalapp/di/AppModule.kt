package com.example.minivideojournalapp.di


import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.minivideojournalapp.data.VideoRepositoryImpl
import com.example.minivideojournalapp.db.VideoDB
import com.example.minivideojournalapp.domain.repositories.VideoRepository
import com.example.minivideojournalapp.domain.usecases.UpdateVideoDescriptionUseCase
import com.example.minivideojournalapp.domain.usecases.GetAllVideosUseCase
import com.example.minivideojournalapp.domain.usecases.SaveVideoIntoDataBaseUseCase
import com.example.minivideojournalapp.resentation.mainscreen.MainScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

	single<SqlDriver> {
		AndroidSqliteDriver(
			schema = VideoDB.Schema,
			context = androidContext(),
			name = "video.db"
		)
	}

	single<VideoRepository> {
		VideoRepositoryImpl(VideoDB(get()))
	}

	single<GetAllVideosUseCase>{
		GetAllVideosUseCase(get())
	}

	single<SaveVideoIntoDataBaseUseCase>{
		SaveVideoIntoDataBaseUseCase(get())
	}

	single<UpdateVideoDescriptionUseCase>{
		UpdateVideoDescriptionUseCase(get())
	}

	viewModel {
		MainScreenViewModel(get(), get(), get())
	}
}