package com.example.atrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao{
    @Query("SELECT * from items ORDER BY subName ASC")
    fun getAllItems(): Flow<List<Subject>>

    @Query("SELECT * from AttendanceTrack where subName=:subName ORDER BY date ASC")
    fun getHistory(subName: String): Flow<List<AttendanceTrack>>

    @Query("SELECT * from AttendanceTrack where date = :date")
    fun getAllItemsOnDate(date: String): List<AttendanceTrack>

    @Query("SELECT * from AttendanceTrack where date = :date")
    fun getAllItemsOnDateStream(date: String): Flow<List<AttendanceTrack>>

    @Query("SELECT count(attendance) from AttendanceTrack where attendance is 1 and subName = :subName")
    fun getAttendanceCount(subName: String): Int

    @Query("SELECT count(date) from AttendanceTrack where subName= :subName")
    fun getDateCount(subName: String):Int

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Subject>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDate(item: AttendanceTrack)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Subject)

    @Update
    suspend fun updateItem(item: AttendanceTrack)

    @Update
    suspend fun update(item: Subject)
    @Delete
    suspend fun deleteItem(item: Subject)

    @Query (" Delete from AttendanceTrack where subName= :subName")
    suspend fun delete(subName: String)

    @Query (" Delete from AttendanceTrack where subName= :subName and date= :date")
    suspend fun deleteHistory(subName: String,date: String)

    @Query("Update ITEMS set nTotal=:nTotal,nPresent=:nPresent,percent=:percent where subName=:subName")
    suspend fun updateSubject(nTotal: Int,nPresent:Int, percent: Float,subName: String)
}