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

    @Query("SELECT * from AttendanceTrack ORDER BY date ASC")
    fun getHistory(): Flow<List<AttendanceTrack>>

    @Query("SELECT count(attendance) from AttendanceTrack where attendance is 1 and subName = :subName")
    fun getAttendanceCount(subName: String): Int

    @Query("SELECT count(date) from AttendanceTrack where subName= :subName")
    fun getDateCount(subName: String):Int

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Subject>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDate(item: AttendanceTrack)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Subject)

    @Insert
    suspend fun insertAllIntoTable2(table2Entities: List<AttendanceTrack>)

    @Update
    suspend fun updateItem(item: AttendanceTrack)

    @Update
    suspend fun update(item: Subject)

    @Delete
    suspend fun delete(item: Subject)
}