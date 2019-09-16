package io.github.vnicius.twitterclone.data.repository.tweet

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class TweetRepositoryFactory : RepositoryFactory() {
    override fun create(myApp: Application): Repository<Any> {
        return Repository(TweetRepositoryLocal(myApp), TweetRepositoryRemote())
    }
}