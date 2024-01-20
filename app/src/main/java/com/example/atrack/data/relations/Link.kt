package com.example.atrack.data.relations

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.atrack.data.AttendanceTrack



data class Link(
    var id: Int=0,
    var subName: String
)