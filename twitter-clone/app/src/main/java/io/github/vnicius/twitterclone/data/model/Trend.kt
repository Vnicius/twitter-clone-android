package io.github.vnicius.twitterclone.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Trend(
    @PrimaryKey var id: Int,
    var name: String,
    var query: String,
    var tweetVolume: Int
) : Serializable