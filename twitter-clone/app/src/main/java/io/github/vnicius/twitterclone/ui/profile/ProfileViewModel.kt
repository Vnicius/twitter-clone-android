package io.github.vnicius.twitterclone.ui.profile

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.util.Log
import androidx.lifecycle.*
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Status
import twitter4j.User

private const val MAX_PAGES = 5
private const val MAX_ITEMS = 10

/**
 * Profile ViewModel
 * @property view instance of the view
 */
class ProfileViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepositoryRemote()
    private lateinit var homeTweetsDataSourceFactory: UserTweetsDataSourceFactory
    lateinit var homeTweetsList: LiveData<PagedList<Status>>
    lateinit var stateTweets: LiveData<State>
    var userData: MutableLiveData<User> = MutableLiveData()
    var stateUserData: MutableLiveData<State> = MutableLiveData()

    fun getUser(userId: Long) {
        viewModelScope.launch {
            try {
                userData.postValue(userRepository.getUserAsync(userId))

                stateUserData.postValue(State.DONE)
            } catch (e: Exception) {
                Log.e("debug error", "Unknown exception", e)

                stateUserData.postValue(State.ERROR)
            }
        }
    }

    fun buildTweets(userId: Long) {
        homeTweetsDataSourceFactory = UserTweetsDataSourceFactory(userId, MAX_ITEMS, userRepository)
        val config = PagedList.Config.Builder()
            .setPageSize(MAX_PAGES)
            .setInitialLoadSizeHint(MAX_PAGES * 2)
            .setEnablePlaceholders(false)
            .build()

        homeTweetsList = LivePagedListBuilder(homeTweetsDataSourceFactory, config).build()
        stateTweets = Transformations.switchMap(
            homeTweetsDataSourceFactory.userTweetsDataSourceLiveData,
            UserTweetsDataSource::state
        )
    }
}
