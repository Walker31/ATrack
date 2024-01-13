package com.example.atrack.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.data.Subject

data class Link(

    @Embedded val subject: Subject,
    @Relation(
        parentColumn="subName",
        entityColumn = "subName"
    )

    val attendanceTrack: AttendanceTrack
)
