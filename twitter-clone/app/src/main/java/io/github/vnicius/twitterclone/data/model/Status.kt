package io.github.vnicius.twitterclone.data.model

import androidx.room.*
import io.github.vnicius.twitterclone.utils.GsonTypeConverter
import twitter4j.Status

@Entity
data class Status(
    @PrimaryKey var statusId: Long = 0,
    @ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userOwnerId"])
    var userOwnerId: Long = 0,
    var text: String = "",
    var favoriteCount: Int = 0,
    var retweetCount: Int = 0,
    var createdAt: Long = 0,
    var mediaEntities: List<MediaEntity> = listOf()
) {
    object ModelMapper {
        fun from(status: Status) =
            Status(
                statusId = status.id,
                userOwnerId = status.user.id,
                text = status.text,
                favoriteCount = status.favoriteCount,
                retweetCount = status.retweetCount,
                createdAt = status.createdAt.time,
                mediaEntities = status.mediaEntities.map { MediaEntity.ModelMapper.from(it) }
            )
    }
}