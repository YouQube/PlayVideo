package com.brandon.playvideo_app.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDAO {

//    @Query("SELECT * FROM videos")
//    fun getAll(): LiveData<List<VideoEntity>>

    @Query("SELECT * FROM videos ORDER BY id DESC")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getSpecificVideo(id: Int): VideoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(video: VideoEntity)

    @Delete
    suspend fun deleteVideos(video: VideoEntity)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteSpecificVideo(id: Int)

    @Update
    suspend fun updateVideos(video: VideoEntity)
}