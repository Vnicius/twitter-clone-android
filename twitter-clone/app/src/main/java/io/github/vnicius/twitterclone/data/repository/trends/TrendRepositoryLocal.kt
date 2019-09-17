package io.github.vnicius.twitterclone.data.repository.trends

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.github.vnicius.twitterclone.data.local.database.TrendDatabase
import io.github.vnicius.twitterclone.data.model.Trend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrendRepositoryLocal(myApp: Application) : TrendRepository {

    private val trendDao =
        Room.databaseBuilder(myApp.applicationContext, TrendDatabase::class.java, DATABASE_NAME)
            .build().trendDao()

    override suspend fun saveTrendsAsync(woeid: Int, trends: List<Trend>) =
        withContext(Dispatchers.IO) {
            trendDao.insertAll(trends)
        }

    override suspend fun getTrendsAsync(woeid: Int): List<Trend>? = withContext(Dispatchers.IO) {
        trendDao.getAll().value
    }

    override suspend fun getTrendsLiveDataAsync(woeid: Int): LiveData<List<Trend>> =
        withContext(Dispatchers.IO) {
            trendDao.getAll()
        }

    companion object {
        const val DATABASE_NAME = "trends-database"
    }
}