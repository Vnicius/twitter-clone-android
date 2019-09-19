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
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PAGE_SIZE * 3)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()


        homeTweetsList = factory?.let {
            LivePagedListBuilder<Int, UserStatus>(
                it,
                config
            ).setBoundaryCallback(
                UserTweetsBoundaryCallback(
                    userId,
                    NETWORK_PAGE_SIZE,
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
                val tweets = userRepository.remote.getUserTweetsAsync(userId, NETWORK_PAGE_SIZE, 1)

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

    companion object {
        const val PAGE_SIZE = 10
        const val NETWORK_PAGE_SIZE = 30
    }
}
