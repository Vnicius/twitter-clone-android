package io.github.vnicius.twitterclone.ui.profile

import android.util.Log
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryRemote
import io.github.vnicius.twitterclone.utils.LogTagsUtils
import kotlinx.coroutines.*

const val TWEETS_COUNT = 50

/**
 * Profile Presenter
 * @property view instance of the view
 */
class ProfilePresenter(val view: ProfileContract.View) : ProfileContract.Presenter {

    private val userRepository: UserRepository = UserRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

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

    override fun getHomeTweets(userId: Long) {
        view.showLoader()

        presenterScope.launch {
            try {
                // get the user tweets
                val tweets =
                    userRepository.getUserTweetsAsync(userId, TWEETS_COUNT)

                view.showTweets(tweets.toMutableList())
            } catch (e: Exception) {
                Log.e(LogTagsUtils.DEBUG_EXCEPTION, "Unknown exception", e)

                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}
