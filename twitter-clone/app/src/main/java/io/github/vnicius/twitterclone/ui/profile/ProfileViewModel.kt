package io.github.vnicius.twitterclone.ui.profile

import android.app.Application
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.util.Log
import androidx.lifecycle.*
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.RepositoryFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Status
import twitter4j.TwitterException

private const val MAX_PAGES = 5
private const val MAX_ITEMS = 10

/**
 * Profile ViewModel
 * @property view instance of the view
 */
class ProfileViewModel(val myApp: Application) : AndroidViewModel(myApp) {

    private val userRepository: Repository<UserRepository> =
        RepositoryFactory.createRepository<UserRepository>()?.create(myApp) as Repository<UserRepository>
    private lateinit var homeTweetsDataSourceFactory: UserTweetsDataSourceFactory
    lateinit var homeTweetsList: LiveData<PagedList<Status>>
    lateinit var stateTweets: LiveData<State>
    var userData: MutableLiveData<User> = MutableLiveData()
    var localHomeTweetsList: MutableLiveData<List<Status>?> = MutableLiveData()
    var stateUserData: MutableLiveData<State> = MutableLiveData()

    fun getUser(userId: Long) {
        viewModelScope.launch {
            try {
                var user = userRepository.local.getUserAsync(userId)

                if (user != null) {
                    userData.postValue(user)
                }

                user = userRepository.remote.getUserAsync(userId)
                userData.postValue(user)

                stateUserData.postValue(State.DONE)
                user?.let { userRepository.local.saveUserAsync(it) }
            } catch (e: TwitterException) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Twitter connection exception", e)

                stateUserData.postValue(State.CONNECTION_ERROR)
            } catch (e: Exception) {
                Log.e("debug error", "Unknown exception", e)

                stateUserData.postValue(State.ERROR)
            }
        }
    }

    fun buildTweets(userId: Long) {

        getLocalUserTweets(userId)

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

    fun getTweetsDataSource() = homeTweetsDataSourceFactory.userTweetsDataSourceLiveData.value

    private fun getLocalUserTweets(userId: Long) {
        viewModelScope.launch {
            localHomeTweetsList.postValue(userRepository.local.getUserTweetsAsync(userId, 10, 1))
        }
    }
}
