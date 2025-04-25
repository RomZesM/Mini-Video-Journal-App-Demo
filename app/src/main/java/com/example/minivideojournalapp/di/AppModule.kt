package com.example.minivideojournalapp.di


import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.minivideojournalapp.data.VideoDataSource
import com.example.minivideojournalapp.data.VideoDataSourceImpl
import com.example.minivideojournalapp.db.VideoDB
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

	single<VideoDataSource> {
		VideoDataSourceImpl(VideoDB(get()))
	}

	viewModel {
		MainScreenViewModel(get())
	}
}