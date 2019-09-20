package io.github.vnicius.twitterclone.data.repository.location

import io.github.vnicius.twitterclone.data.repository.RepositoryFactory

class LocationRepositoryFactory private constructor(
    localRepostiory: LocationRepostiory,
    remoteRepostiory: LocationRepostiory
) : RepositoryFactory<LocationRepostiory>() {

    private var local: LocationRepostiory = localRepostiory
    private var remote: LocationRepostiory = remoteRepostiory

    override fun getLocal(): LocationRepostiory = local

    override fun getRemote(): LocationRepostiory = remote

    companion object {
        fun create(): RepositoryFactory<LocationRepostiory> {
            return LocationRepositoryFactory(LocationRepositoryLocal(), LocationRepositoryRemote())
        }
    }
}