package io.github.vnicius.twitterclone.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.vnicius.twitterclone.data.model.MediaEntity

class GsonTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun mediasListToString(medias: List<MediaEntity>) = gson.toJson(medias)

    @TypeConverter
    fun stringToMediasList(json: String?): List<MediaEntity> {
        if (json.isNullOrBlank()) {
            return listOf()
        }

        return gson.fromJson(json, object : TypeToken<List<MediaEntity>>() {}.type)
    }

}