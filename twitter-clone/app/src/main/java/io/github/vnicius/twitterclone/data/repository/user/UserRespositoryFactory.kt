package io.github.vnicius.twitterclone.data.repository.user

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class UserRespositoryFactory : RepositoryFactory() {
    override fun create(myApp: Application): Repository<Any> {
        return Repository(UserRepositoryLocal(myApp), UserRepositoryRemote())
    }
}