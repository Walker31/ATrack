package com.example.atrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val subName: String,
    val subCode: String,
    val nPresent: Int,
    val nTotal: Int
)