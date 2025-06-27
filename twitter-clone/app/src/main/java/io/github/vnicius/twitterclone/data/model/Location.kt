package io.github.vnicius.twitterclone.data.model

import twitter4j.Location

data class Location(
    val woeid: Int,
    val countryName: String,
    val countryCode: String?,
    val placeName: String,
    val placeCode: Int,
    val name: String,
    val url: String
) {
    object ModelMapper {
        fun from(location: Location) = Location(
            woeid = location.woeid,
            countryCode = location.countryCode,
            countryName = location.countryName,
            placeName = location.placeName,
            placeCode = location.placeCode,
            name = location.name,
            url = location.url
        )
    }
}