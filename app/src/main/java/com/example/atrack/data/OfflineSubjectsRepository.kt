package com.example.atrack.data

import kotlinx.coroutines.flow.Flow

class OfflineSubjectsRepository(private val itemDao: SubjectDao): SubjectsRepository {
    override fun getAllItemsStream(): Flow<List<Subject>> = itemDao.getAllItems()

    override fun getHistory(): Flow<List<AttendanceTrack>> = itemDao.getHistory()

    override fun getItemStream(id: Int): Flow<Subject?> = itemDao.getItem(id)

    override fun getAllItemsOnDate(date: String): List<AttendanceTrack> =itemDao.getAllItemsOnDate(date)

    override fun getAllItemsOnDateStream(date: String): Flow<List<AttendanceTrack>> =itemDao.getAllItemsOnDateStream(date)

    override fun getAttendanceCount(subName: String): Int = itemDao.getAttendanceCount(subName)

    override fun getDateCount(subName: String): Int = itemDao.getDateCount(subName)

    override suspend fun insertItem(item: Subject) = itemDao.insert(item)

    override suspend fun insertDate(item: AttendanceTrack) = itemDao.insertDate(item)

    override suspend fun deleteItem(item: Subject) = itemDao.delete(item)

    override suspend fun updateItem(item: Subject) = itemDao.update(item)

    override suspend fun update(item: AttendanceTrack) = itemDao.updateItem(item)
}