package com.example.atrack.data

import android.content.Context

interface AppContainer {
    val itemsRepository: SubjectsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val itemsRepository: SubjectsRepository by lazy {
        OfflineSubjectsRepository(SubjectDatabase.getDatabase(context).itemDao())
    }
}