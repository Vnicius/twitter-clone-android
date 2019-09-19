package io.github.vnicius.twitterclone.data.repository.trends

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class TrendRepositoryFactory private constructor(
    localRepository: TrendRepository,
    remoteRepository: TrendRepository
) : RepositoryFactory<TrendRepository>() {

    private var local: TrendRepository = localRepository
    private var remote: TrendRepository = remoteRepository

    override fun getLocal(): TrendRepository = local

    override fun getRemote(): TrendRepository = remote

    companion object {
        fun create(myApp: Application): RepositoryFactory<TrendRepository> =
            TrendRepositoryFactory(TrendRepositoryLocal(myApp), TrendRepositoryRemote())
    }
}