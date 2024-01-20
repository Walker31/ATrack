package com.example.atrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity()
data class AttendanceTrack (
    @PrimaryKey(autoGenerate=true)
    var id: Int,
    var subCode: String,
    var subName: String,
    var date: String,
    var attendance: Boolean
)





