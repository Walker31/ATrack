package com.example.atrack

import android.app.Application
import com.example.atrack.data.AppContainer
import com.example.atrack.data.AppDataContainer

class TrackApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}