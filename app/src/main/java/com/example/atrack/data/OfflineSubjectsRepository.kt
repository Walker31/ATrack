package com.example.atrack.data

import kotlinx.coroutines.flow.Flow

class OfflineSubjectsRepository(private val itemDao: SubjectDao): SubjectsRepository {
    override fun getAllItemsStream(): Flow<List<Subject>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Subject?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Subject) = itemDao.insert(item)

    override suspend fun deleteItem(item: Subject) = itemDao.delete(item)

    override suspend fun updateItem(item: Subject) = itemDao.update(item)
}