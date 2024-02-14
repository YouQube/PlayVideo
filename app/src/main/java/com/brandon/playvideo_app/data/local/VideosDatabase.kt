package com.brandon.playvideo_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brandon.playvideo_app.data.local.dao.ChannelDAO
import com.brandon.playvideo_app.data.local.dao.VideoDAO
import com.brandon.playvideo_app.data.local.entities.ChannelEntity
import com.brandon.playvideo_app.data.local.entities.VideoEntity

@Database(entities = [VideoEntity::class, ChannelEntity::class], version = 1)
abstract class VideosDatabase: RoomDatabase() {
    abstract fun videoDAO(): VideoDAO

    abstract fun channelDAO(): ChannelDAO

}

