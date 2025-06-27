package io.github.vnicius.twitterclone.ui.trendslocations

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.vnicius.twitterclone.data.model.Location
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.location.LocationRepostiory
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys
import kotlinx.coroutines.launch

class TrendsLocationsViewModel(myApplication: Application) : AndroidViewModel(myApplication) {

    private val locationRepostiory: RepositoryFactory<LocationRepostiory>? =
        RepositoryFactory.createRepository<LocationRepostiory>(myApplication) as RepositoryFactory<LocationRepostiory>?
    var sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(myApplication.applicationContext)
    var locationsList: MutableLiveData<List<Location>> = MutableLiveData()

    fun saveLocation(woeid: Int, name: String) {
        sharedPreferences.edit().apply {
            putInt(SharedPreferencesKeys.WOEID, woeid)
            putString(SharedPreferencesKeys.LOCATION_NAME, name)
        }.apply()
    }

    fun getLocations() {
        viewModelScope.launch {
            if (locationRepostiory != null) {
                locationsList.postValue(locationRepostiory.getRemote().getLocations())
            }
        }
    }
}