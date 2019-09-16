package io.github.vnicius.twitterclone.data.local.filemanage

import android.app.Application
import twitter4j.Status

class SearchTweetsFileManager(val myApp: Application) {

    private val fileManager = FileManager(myApp)

    suspend fun saveTweetsAsync(queryText: String, tweets: List<Status>): Boolean {
        return fileManager.saveData(getFileName(queryText), tweets)
    }

    suspend fun getTweetsAsync(queryText: String): List<Status>? {
        return fileManager.readData(getFileName(queryText)) as List<Status>?
    }

    private fun getFileName(queryText: String) = "${FILE_NAME_BASE}$queryText"

    companion object {
        const val FILE_NAME_BASE = "tweets_"
    }
}