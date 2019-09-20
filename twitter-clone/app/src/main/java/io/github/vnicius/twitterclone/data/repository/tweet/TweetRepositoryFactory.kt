package io.github.vnicius.twitterclone.data.repository.tweet

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class TweetRepositoryFactory private constructor(
    localRepository: TweetRepository,
    remoteRepository: TweetRepository
) : RepositoryFactory<TweetRepository>() {

    private var local: TweetRepository = localRepository
    private var remote: TweetRepository = remoteRepository

    override fun getLocal(): TweetRepository = local

    override fun getRemote(): TweetRepository = remote

    companion object {
        fun create(myApp: Application): RepositoryFactory<TweetRepository> {
            return TweetRepositoryFactory(TweetRepositoryLocal(myApp), TweetRepositoryRemote())
        }
    }

}