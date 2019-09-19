package io.github.vnicius.twitterclone.data.repository.trends

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.github.vnicius.twitterclone.data.local.database.AppDatabase
import io.github.vnicius.twitterclone.data.model.Trend

class TrendRepositoryLocal(myApp: Application) : TrendRepository {

    private val trendDao = AppDatabase.getInstance(myApp.applicationContext).trendDao()

    override suspend fun saveTrendsAsync(woeid: Int, trends: List<Trend>) =
        trendDao.insertAll(trends)

    override suspend fun getTrendsAsync(woeid: Int): List<Trend>? = trendDao.getAll().value

    override suspend fun getTrendsLiveDataAsync(woeid: Int): LiveData<List<Trend>> =
        trendDao.getAll()
}