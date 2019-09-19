package io.github.vnicius.twitterclone.data.repository.user

import android.app.Application
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class UserRepositoryFactory private constructor(
    localRepository: UserRepository,
    remoteRepository: UserRepository
) : RepositoryFactory<UserRepository>() {

    private var local: UserRepository = localRepository
    private var remote: UserRepository = remoteRepository

    override fun getLocal(): UserRepository = local

    override fun getRemote(): UserRepository = remote

    companion object {
        fun create(myApp: Application): RepositoryFactory<UserRepository> {
            return UserRepositoryFactory(UserRepositoryLocal(myApp), UserRepositoryRemote())
        }
    }
}