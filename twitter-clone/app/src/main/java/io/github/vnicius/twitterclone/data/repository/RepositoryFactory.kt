package io.github.vnicius.twitterclone.data.repository

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepository
import io.github.vnicius.twitterclone.data.repository.trends.TrendRepositoryFactory
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepository
import io.github.vnicius.twitterclone.data.repository.tweet.TweetRepositoryFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryFactory

abstract class RepositoryFactory<T> {
    abstract fun getLocal(): T
    abstract fun getRemote(): T

    companion object {
        inline fun <reified T> createRepository(myApp: Application): RepositoryFactory<*>? {
            return when (T::class) {
                TweetRepository::class -> TweetRepositoryFactory.create(myApp)
                TrendRepository::class -> TrendRepositoryFactory.create(myApp)
                UserRepository::class -> UserRepositoryFactory.create(myApp)
                else -> null
            }
        }
    }
}