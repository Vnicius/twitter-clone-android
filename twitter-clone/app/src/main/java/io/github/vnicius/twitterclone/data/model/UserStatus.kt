package io.github.vnicius.twitterclone.data.model

import androidx.room.Embedded

data class UserStatus(
    @Embedded
    var status: Status,
    @Embedded
    var user: User
) {
    object ModelMapper {
        fun from(user: twitter4j.User, status: twitter4j.Status) =
            UserStatus(Status.ModelMapper.from(status), User.ModelMapper.from(user))
    }
}