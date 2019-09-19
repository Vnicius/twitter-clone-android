package io.github.vnicius.twitterclone.data.model

import androidx.room.Embedded

data class UserStatus(
    @Embedded
    var status: Status,
    @Embedded
    var user: User
)