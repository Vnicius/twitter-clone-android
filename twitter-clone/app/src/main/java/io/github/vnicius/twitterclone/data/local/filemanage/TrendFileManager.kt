package io.github.vnicius.twitterclone.data.local.filemanage

import android.app.Application
import twitter4j.Trend

class TrendFileManager(private val myApp: Application) {

    private val fileManager = FileManager(myApp)

    suspend fun getTrendsAsync(woeid: Int): Array<Trend>? {
        return fileManager.readData("${FILE_BASE_NAME}$woeid") as Array<Trend>?
    }

    suspend fun saveTrendsAsync(woeid: Int, trends: Array<Trend>): Boolean {
        return fileManager.saveData("${FILE_BASE_NAME}$woeid", trends)
    }

    companion object {
        const val FILE_BASE_NAME = "trends_"
    }
}