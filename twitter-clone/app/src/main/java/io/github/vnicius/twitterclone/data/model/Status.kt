package io.github.vnicius.twitterclone.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity
data class Status(
    @PrimaryKey var statusId: Long,
    @ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userOwnerId"])
    var userOwnerId: Long,
    var text: String,
    var favoriteCount: Int,
    var retweetCount: Int,
    var createdAt: Long
) {
    object ModelMapper {
        fun from(status: Status) =
            Status(
                statusId = status.id,
                userOwnerId = status.user.id,
                text = status.text,
                favoriteCount = status.favoriteCount,
                retweetCount = status.retweetCount,
                createdAt = status.createdAt.time
            )
    }
}