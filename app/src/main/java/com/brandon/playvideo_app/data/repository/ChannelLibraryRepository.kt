package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.local.dao.ChannelDAO
import com.brandon.playvideo_app.data.local.entities.ChannelEntity
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChannelLibraryRepository @Inject constructor(private val channelDAO: ChannelDAO) {

    val channels = channelDAO.getAllChannels()

    suspend fun getChannel(id: Int) = withContext(Dispatchers.IO) {
        channelDAO.getSpecificChannel(id)
    }

    suspend fun insertChannel(channel: ChannelEntity) = withContext(Dispatchers.IO) {
        channelDAO.insertChannels(channel)
    }

    suspend fun deleteChannel(channel: ChannelEntity) = withContext(Dispatchers.IO) {
        channelDAO.deleteChannels(channel)
    }

    suspend fun deleteChannelById(id: Int) = withContext(Dispatchers.IO) {
        channelDAO.deleteSpecificChannel(id)
    }

    suspend fun updateChannels(channel: ChannelEntity) = withContext(Dispatchers.IO) {
        channelDAO.updateChannels(channel)
    }
}