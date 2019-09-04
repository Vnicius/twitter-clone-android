package io.github.vnicius.twitterclone.ui.profile

import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryRemote
import kotlinx.coroutines.*
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

const val TWEETS_COUNT = 50

/**
 * Profile Presenter
 * @property view instance of the view
 */
class ProfilePresenter(val view: ProfileContract.View) : ProfileContract.Presenter {

    // instance of the repository
    private val mRepository: UserRepository = UserRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun getUser(userId: Long) {
        presenterScope.launch {
            try {
                // get the user information
                val user = mRepository.getUserAsync(userId)

                view.showUser(user)
            } catch (e: Exception) {
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
                    mRepository.getUserTweetsAsync(userId, TWEETS_COUNT)

                view.showTweets(tweets.toMutableList())
            } catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }

}