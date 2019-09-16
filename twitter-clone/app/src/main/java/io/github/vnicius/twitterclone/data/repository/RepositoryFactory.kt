package io.github.vnicius.twitterclone.data.repository

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRespositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRespositoryFactory

abstract class RepositoryFactory {
    abstract fun create(myApp: Application): Repository<Any>

    companion object {
        inline fun <reified T> createRepository(): RepositoryFactory? {
            return when (T::class) {
                TweetRepository::class -> TweetRepositoryFactory()
                TrendRepository::class -> TrendRespositoryFactory()
                UserRepository::class -> UserRespositoryFactory()
                else -> null
            }
        }
    }
}