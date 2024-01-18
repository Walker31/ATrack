package com.example.atrack.data

import kotlinx.coroutines.flow.Flow

class OfflineSubjectsRepository(private val itemDao: SubjectDao): SubjectsRepository {
    override fun getAllItemsStream(): Flow<List<Subject>> = itemDao.getAllItems()

    override fun getHistory(subName: String): Flow<List<AttendanceTrack>> = itemDao.getHistory(subName)

    override fun getItemStream(id: Int): Flow<Subject?> = itemDao.getItem(id)

    override fun getAllItemsOnDate(date: String): List<AttendanceTrack> =itemDao.getAllItemsOnDate(date)

    override fun getAllItemsOnDateStream(date: String): Flow<List<AttendanceTrack>> =itemDao.getAllItemsOnDateStream(date)

    override fun getAttendanceCount(subName: String): Int = itemDao.getAttendanceCount(subName)

    override fun getDateCount(subName: String): Int = itemDao.getDateCount(subName)

    override suspend fun insertItem(item: Subject) = itemDao.insert(item)

    override suspend fun insertDate(item: AttendanceTrack) = itemDao.insertDate(item)

    override suspend fun deleteHistory(subName: String, date: String) =itemDao.deleteHistory(subName,date)
    override suspend fun delete(subName: String) =itemDao.delete(subName)

    override suspend fun deleteItem(item: Subject) = itemDao.deleteItem(item)

    override suspend fun updateItem(item: Subject) = itemDao.update(item)

    override suspend fun update(item: AttendanceTrack) = itemDao.updateItem(item)

    override suspend fun updateSubject( nTotal: Int, nPresent: Int,percent:Float, subName: String) = itemDao.updateSubject(nTotal,nPresent,percent, subName)
}