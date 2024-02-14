package com.brandon.playvideo_app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.brandon.playvideo_app.data.local.entities.ChannelEntity
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDAO {

//    @Query("SELECT * FROM channels")
//    fun getAll(): List<ChannelEntity>

    @Query("SELECT * FROM channels ORDER BY id DESC")
    fun getAllChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE id = :id")
    suspend fun getSpecificChannel(id: Int): ChannelEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channel: ChannelEntity)

    @Delete
    suspend fun deleteChannels(channel: ChannelEntity)

    @Query("DELETE FROM channels WHERE id = :id")
    suspend fun deleteSpecificChannel(id: Int)

    @Update
    suspend fun updateChannels(channel: ChannelEntity)
}