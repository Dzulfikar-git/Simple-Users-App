package com.dzulfikar.usersapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Tell the app to Hilt load Dependency Injection
@HiltAndroidApp
class UsersApplication : Application() {}