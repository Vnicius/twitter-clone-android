package io.github.vnicius.twitterclone.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.vnicius.twitterclone.data.local.dao.TrendDao
import io.github.vnicius.twitterclone.data.model.Trend

@Database(entities = [Trend::class], version = 1, exportSchema = false)
abstract class TrendDatabase : RoomDatabase() {
    abstract fun trendDao(): TrendDao
}