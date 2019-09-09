package io.github.vnicius.twitterclone.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSource
import io.github.vnicius.twitterclone.data.datasource.usertweets.UserTweetsDataSourceFactory
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import io.github.vnicius.twitterclone.utils.State
import kotlinx.coroutines.*
import twitter4j.Status

private const val MAX_PAGES = 5
private const val MAX_ITEMS = 10

/**
 * Profile Presenter
 * @property view instance of the view
 */
class ProfilePresenter(val view: ProfileContract.View) : ProfileContract.Presenter {

    private val userRepository: UserRepository = UserRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)
    private lateinit var homeTweetsList: LiveData<PagedList<Status>>
    private lateinit var homeTweetsDataSourceFactory: UserTweetsDataSourceFactory

    override fun getUser(userId: Long) {
        presenterScope.launch {
            try {
                val user = userRepository.getUserAsync(userId)

                view.showUser(user)
            } catch (e: Exception) {
                Log.e("debug error", "Unknown exception", e)

                view.showError("Connection Error")
            }
        }
    }

    override fun buildTweets(userId: Long) {
        homeTweetsDataSourceFactory = UserTweetsDataSourceFactory(userId, MAX_ITEMS, userRepository)
        val config = PagedList.Config.Builder()
            .setPageSize(MAX_PAGES)
            .setInitialLoadSizeHint(MAX_PAGES * 2)
            .setEnablePlaceholders(false)
            .build()
        homeTweetsList = LivePagedListBuilder(homeTweetsDataSourceFactory, config).build()
    }

    override fun getTweetsValue() = homeTweetsList

    override fun getTweetsState(): LiveData<State> = Transformations.switchMap(
        homeTweetsDataSourceFactory.userTweetsDataSourceLiveData,
        UserTweetsDataSource::state
    )

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}
