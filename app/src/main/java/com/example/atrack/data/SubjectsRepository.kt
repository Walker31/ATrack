package com.example.atrack.data

import kotlinx.coroutines.flow.Flow

interface SubjectsRepository{

    fun getAllItemsStream(): Flow<List<Subject>>

    fun getItemStream(id: Int): Flow<Subject?>

    suspend fun insertItem(item: Subject)

    suspend fun deleteItem(item: Subject)

    suspend fun updateItem(item: Subject)
}