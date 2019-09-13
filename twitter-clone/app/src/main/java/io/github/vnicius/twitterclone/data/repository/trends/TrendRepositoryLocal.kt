package io.github.vnicius.twitterclone.data.repository.trends

import android.app.Application
import io.github.vnicius.twitterclone.data.local.filemanage.TrendFileManager
import twitter4j.Trend

class TrendRepositoryLocal(myApp: Application) : TrendRepository {

    private val trendFileManager = TrendFileManager(myApp)

    override suspend fun saveTrendsAsync(woeid: Int, trends: Array<Trend>): Boolean =
        trendFileManager.saveTrendsAsync(woeid, trends)

    override suspend fun getTrendsAsync(woeid: Int): Array<Trend>? =
        trendFileManager.getTrendsAsync(woeid)
}