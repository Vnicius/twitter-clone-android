package io.github.vnicius.twitterclone.data.repository.location

import io.github.vnicius.twitterclone.data.model.Location

interface LocationRepostiory {

    suspend fun getLocations(): List<Location>
}