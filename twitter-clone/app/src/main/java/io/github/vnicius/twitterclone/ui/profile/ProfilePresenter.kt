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
            // get the user information
            var user: User

            coroutineScope {
                try {
                    user = mRepository.getUserAsync(userId).await()
                    view.showUser(user)
                } catch (e: Exception) {
                    view.showError("Connection Error")
                }
            }
        }
    }

    override fun getHomeTweets(userId: Long) {

        view.showLoader()
        presenterScope.launch {
            // get the user tweets
            var tweets: ResponseList<Status>

            try {
                coroutineScope {
                    tweets = mRepository.getUserTweetsAsync(userId, TWEETS_COUNT).await()
                    view.showTweets(tweets.toMutableList())
                }
            } catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }

}