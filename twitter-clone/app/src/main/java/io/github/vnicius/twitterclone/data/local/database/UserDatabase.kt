package io.github.vnicius.twitterclone.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.vnicius.twitterclone.data.local.dao.UserDao
import io.github.vnicius.twitterclone.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}