package com.example.demotest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demotest.database.dao.ImagesDao
import com.example.demotest.models.Images

@Database(entities = [Images::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImagesDao
}