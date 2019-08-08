package io.github.vnicius.twitterclone.utils

import android.view.View
import twitter4j.Status

interface TweetClick {
    fun onClick(view: View, tweet: Status)
}