package io.github.vnicius.twitterclone.data.model

import androidx.room.Entity
import twitter4j.MediaEntity

data class MediaEntity(
    var url: String = "",
    var mediaURLHttps: String = "",
    var type: String = ""
) {
    object ModelMapper {
        fun from(media: MediaEntity) =
            MediaEntity(url = media.url, mediaURLHttps = media.mediaURLHttps, type = media.type)
    }
}