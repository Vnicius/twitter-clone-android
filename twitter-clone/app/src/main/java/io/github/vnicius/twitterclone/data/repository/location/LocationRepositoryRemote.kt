package io.github.vnicius.twitterclone.data.repository.location

import io.github.vnicius.twitterclone.data.model.Location
import io.github.vnicius.twitterclone.data.remote.api.APIInterface
import io.github.vnicius.twitterclone.data.remote.api.TwitterAPI

class LocationRepositoryRemote : LocationRepostiory {

    private val apiInstance: APIInterface = TwitterAPI.instance

    override suspend fun getLocations(): List<Location> =
        apiInstance.getLocations().map { Location.ModelMapper.from(it) }
}