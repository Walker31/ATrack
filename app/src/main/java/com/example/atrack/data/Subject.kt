package com.example.atrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Subject(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var subName: String,
    var subCode: String,
    var nPresent: Int,
    var nTotal: Int,
    var percent: Float
)