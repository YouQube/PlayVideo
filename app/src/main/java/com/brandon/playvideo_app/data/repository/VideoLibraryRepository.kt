package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.local.dao.VideoDAO
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoLibraryRepository @Inject constructor(private val videoDAO: VideoDAO) {

    val videos = videoDAO.getAllVideos()

    suspend fun getAllVideos() {
        videoDAO.getAllVideos()
    }

    suspend fun getVideo(id: Int) = withContext(Dispatchers.IO) {
        videoDAO.getSpecificVideo(id)
    }

    suspend fun insertVideo(video: VideoEntity) = withContext(Dispatchers.IO) {
        videoDAO.insertVideos(video)
    }

    suspend fun deleteVideo(video: VideoEntity) = withContext(Dispatchers.IO) {
        videoDAO.deleteVideos(video)
    }

    suspend fun deleteVideoById(id: Int) = withContext(Dispatchers.IO) {
        videoDAO.deleteSpecificVideo(id)
    }

    suspend fun updateVideos(video: VideoEntity) = withContext(Dispatchers.IO) {
        videoDAO.updateVideos(video)
    }
}