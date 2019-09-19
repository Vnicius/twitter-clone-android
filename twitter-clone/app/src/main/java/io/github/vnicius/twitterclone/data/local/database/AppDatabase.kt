package io.github.vnicius.twitterclone.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.vnicius.twitterclone.data.local.dao.StatusDao
import io.github.vnicius.twitterclone.data.local.dao.TrendDao
import io.github.vnicius.twitterclone.data.local.dao.UserDao
import io.github.vnicius.twitterclone.data.local.dao.UserStatusDao
import io.github.vnicius.twitterclone.data.model.Status
import io.github.vnicius.twitterclone.data.model.Trend
import io.github.vnicius.twitterclone.data.model.User

@Database(entities = [Trend::class, User::class, Status::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trendDao(): TrendDao
    abstract fun userDao(): UserDao
    abstract fun statusDao(): StatusDao
    abstract fun userStatusDao(): UserStatusDao

    companion object {
        const val DB_NAME = "tw_clone_database"
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: builder(context).also { instance = it }
            }
        }

        private fun builder(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
        }
    }
}