package io.github.vnicius.twitterclone.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import twitter4j.User

@Entity
data class User(
    @PrimaryKey var id: Long,
    var name: String,
    var email: String?,
    var screenName: String,
    var location: String,
    var description: String,
    var profileImageURLHttps: String?,
    var friendsCount: Int,
    var followersCount: Int,
    var profileBackgroundColor: String?,
    var profileTextColor: String,
    var statusesCount: Int,
    var profileBanner600x200URL: String
) {
    object ModelMapper {
        fun from(user: User) = User(
            id = user.id,
            name = user.name,
            email = user.email,
            screenName = user.screenName,
            location = user.location,
            description = user.description,
            profileImageURLHttps = user.profileImageURLHttps,
            friendsCount = user.friendsCount,
            followersCount = user.followersCount,
            profileBackgroundColor = user.profileBackgroundColor,
            profileTextColor = user.profileTextColor,
            statusesCount = user.statusesCount,
            profileBanner600x200URL = user.profileBanner600x200URL
        )
    }
}