package io.github.vnicius.twitterclone.ui.trendsinfo

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys

class TrendsInfoViewModel(private val myApplication: Application) :
    AndroidViewModel(myApplication) {

    val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(myApplication.applicationContext)

    fun getLocationName(): String? = sharedPreferences.getString(
        SharedPreferencesKeys.LOCATION_NAME,
        myApplication.getString(R.string.label_worldwide)
    )
}