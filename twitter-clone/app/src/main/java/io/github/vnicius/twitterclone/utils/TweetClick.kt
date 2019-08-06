package io.github.vnicius.twitterclone.utils

import android.view.View

interface TweetClick {
    fun onClick(view: View, tweet: Int)
}