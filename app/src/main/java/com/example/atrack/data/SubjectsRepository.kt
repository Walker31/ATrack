package com.example.atrack.data

import kotlinx.coroutines.flow.Flow

interface SubjectsRepository{

    fun getAllItemsStream(): Flow<List<Subject>>

    fun getHistory(subName: String): Flow<List<AttendanceTrack>>

    fun getAttendanceCount(subName: String): Int

    fun getDateCount(subName: String): Int

    fun getAllItemsOnDateStream(date: String): Flow<List<AttendanceTrack>>

    fun getAllItemsOnDate(date: String): List<AttendanceTrack>

    fun getItemStream(id: Int): Flow<Subject?>

    suspend fun insertItem(item: Subject)

    suspend fun delete(subName: String)
    suspend fun deleteHistory(subName: String,date: String)

    suspend fun update(item:AttendanceTrack)

    suspend fun updateSubject(nPresent: Int,nTotal : Int,percent: Float,subName: String)
    suspend fun insertDate(item: AttendanceTrack)

    suspend fun deleteItem(item: Subject)

    suspend fun updateItem(item: Subject)
}