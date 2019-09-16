package io.github.vnicius.twitterclone.data.repository.trends

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class TrendRespositoryFactory : RepositoryFactory() {
    override fun create(myApp: Application): Repository<Any> {
        return Repository(TrendRepositoryLocal(myApp), TrendRepositoryRemote())
    }
}