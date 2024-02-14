package com.brandon.playvideo_app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "videos")
data class VideoEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "channelTitle")
    var channelTitle: String? = null,

    @ColumnInfo(name = "views")
    var views: Int? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = true
):Serializable{
    override fun toString(): String {
        return "$title : $channelTitle"
    }
}