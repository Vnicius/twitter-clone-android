package io.github.vnicius.twitterclone.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: Long, var name: String,
    var email: String,
    var screenName: String,
    var location: String,
    var description: String,
    var profileImageURLHttps: String,
    var friendsCount: Int,
    var followersCount: Int,
    var profileBackgroundColor: String,
    var profileTextColor: String,
    var statusesCount: Int,
    var profileBanner600x200URL: String
)