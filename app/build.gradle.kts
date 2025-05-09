plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.ksp)
	alias(libs.plugins.sqldelight)
}

android {
	namespace = "com.example.minivideojournalapp"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.example.minivideojournalapp"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	// For permissions
	implementation(libs.accompanist.permissions)

	// CameraX
	implementation(libs.androidx.camera.camera2)
	implementation(libs.androidx.camera.lifecycle)
	implementation(libs.androidx.camera.view)
	implementation(libs.androidx.camera.video)

	//Compose navigation
	implementation(libs.androidx.navigation.compose)

	//SQL delight
	implementation(libs.android.driver)
	implementation(libs.coroutines.extensions)

	//Coroutines
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.android)

	//ExoPlayer
	implementation(libs.androidx.media3.exoplayer)
	implementation(libs.androidx.media3.ui)

	// Koin for Android
	implementation(libs.koin.android)
	implementation(libs.koin.androidx.compose)
	testImplementation(libs.koin.test.junit4)

}

sqldelight {
	databases {
		create("VideoDB") {
			packageName.set("com.example.minivideojournalapp.db")
			srcDirs.setFrom("src/main/sqldelight")
		}
	}
}

