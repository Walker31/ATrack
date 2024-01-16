package com.example.atrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Subject::class,AttendanceTrack::class], version = 5, exportSchema = false)
abstract class SubjectDatabase : RoomDatabase() {

    abstract fun itemDao(): SubjectDao

    companion object {
        @Volatile
        private var Instance: SubjectDatabase? = null

        private val MIGRATION_2_3 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }


        fun getDatabase(context: Context): SubjectDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SubjectDatabase::class.java, "DataBase")

                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}