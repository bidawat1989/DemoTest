package com.example.demotest.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demotest.models.Images
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image:Images)

    @Query("SELECT * FROM imagesList")
    fun getList(): Flow<List<Images>>


    @Query("DELETE FROM imagesList")
    suspend fun deleteAll()
}