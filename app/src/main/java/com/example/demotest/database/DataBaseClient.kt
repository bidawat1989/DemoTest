package com.example.demotest.database

import android.content.Context
import androidx.room.Room

class DataBaseClient {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "imagesDatabase"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}