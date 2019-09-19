package io.github.vnicius.twitterclone.ui.profile

import android.app.Application
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.util.Log
import androidx.lifecycle.*
import io.github.vnicius.twitterclone.data.datasource.UserTweetsBoundaryCallback
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.TwitterException

private const val MAX_PAGES = 10
private const val MAX_ITEMS = 10

/**
 * Profile ViewModel
 * @property view instance of the view
 */
class ProfileViewModel(val myApp: Application) : AndroidViewModel(myApp) {

    private val userRepository: Repository<UserRepository> =
        RepositoryFactory.createRepository<UserRepository>()?.create(myApp) as Repository<UserRepository>
    lateinit var homeTweetsList: LiveData<PagedList<UserStatus>>
    lateinit var userData: LiveData<User>
    var state: MutableLiveData<State> = MutableLiveData()

    fun getUser(userId: Long): Job {
        return viewModelScope.launch {
            userData = userRepository.local.getUserLiveDataAsync(userId)
        }
    }

    fun updateUser(userId: Long) {
        viewModelScope.launch {
            try {
                val user = userRepository.remote.getUserAsync(userId)

                if (user != null) {
                    userRepository.local.saveUserAsync(user)
                }

                state.postValue(State.DONE)
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                state.postValue(State.CONNECTION_ERROR)
            } catch (e: Exception) {
                Log.e("debug error", "Unknown exception", e)

                state.postValue(State.ERROR)
            }
        }
    }

    fun buildTweets(userId: Long) {
        val factory = userRepository.local.getUserTweetsPaged(userId)
        val config = PagedList.Config.Builder()
            .setPageSize(MAX_PAGES)
            .setPrefetchDistance(MAX_PAGES * 3)
            .setInitialLoadSizeHint(MAX_PAGES * 2)
            .setEnablePlaceholders(false)
            .build()


        homeTweetsList = factory?.let {
            LivePagedListBuilder<Int, UserStatus>(
                it,
                config
            ).setBoundaryCallback(
                UserTweetsBoundaryCallback(
                    userId,
                    MAX_PAGES,
                    viewModelScope,
                    userRepository
                )
            ).build()
        } as LiveData<PagedList<UserStatus>>
        homeTweetsList
    }

    fun updateTweets(userId: Long) {
        viewModelScope.launch {
            try {
                val tweets = userRepository.remote.getUserTweetsAsync(userId, MAX_PAGES, 1)

                if (tweets != null) {
                    userRepository.local.saveUserTweetsAsync(tweets)
                }

                state.postValue(State.DONE)
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                state.postValue(State.CONNECTION_ERROR)
            } catch (e: Exception) {
                Log.e("debug error", "Unknown exception", e)

                state.postValue(State.ERROR)
            }
        }
    }
}
