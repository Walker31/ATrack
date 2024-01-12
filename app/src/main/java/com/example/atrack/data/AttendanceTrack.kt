package com.example.atrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttendanceTrack (
    @PrimaryKey(autoGenerate=true)
    val id: Int,
    val subCode: String,
    val subName: String,
    val date: String,
    val attendance: Boolean
)