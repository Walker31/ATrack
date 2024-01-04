package com.example.atrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subject::class], version = 1, exportSchema = false)
abstract class SubjectDatabase : RoomDatabase() {

    abstract fun itemDao(): SubjectDao

    companion object {
        @Volatile
        private var Instance: SubjectDatabase? = null

        fun getDatabase(context: Context): SubjectDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SubjectDatabase::class.java, "subject_database")

                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}