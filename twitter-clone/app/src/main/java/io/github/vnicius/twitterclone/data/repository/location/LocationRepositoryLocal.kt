package io.github.vnicius.twitterclone.data.repository.location

import io.github.vnicius.twitterclone.data.model.Location

class LocationRepositoryLocal : LocationRepostiory {
    override suspend fun getLocations(): List<Location> = listOf()
}