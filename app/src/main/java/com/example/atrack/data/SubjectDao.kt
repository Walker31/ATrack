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

    @Query("SELECT count(attendance) from AttendanceTrack where attendance is true and subName = :subName")
    fun getAttendanceCount(subName: String): Int

    @Query("SELECT count(date) from AttendanceTrack where subName= :subName")
    fun getDateCount(subName: String):Int

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Subject>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDate(item: AttendanceTrack)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Subject)

    @Update
    suspend fun update(item: Subject)

    @Delete
    suspend fun delete(item: Subject)
}