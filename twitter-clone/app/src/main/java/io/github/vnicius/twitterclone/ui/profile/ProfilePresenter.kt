package io.github.vnicius.twitterclone.ui.profile

import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepositoryRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

const val TWEETS_COUNT = 50

/**
 * Profile Presenter
 * @property view instance of the view
 */
class ProfilePresenter(val view: ProfileContract.View): ProfileContract.Presenter {

    // instance of the repository
    private val mRepository: UserRepository = UserRepositoryRemote()

    override fun getUser(userId: Long) {
        // get the main scope
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            // get the user information
            var user: User

            coroutineScope{
                try {
                    user = mRepository.getUser(userId).await()
                    view.showUser(user)
                }catch (e: Exception) {
                    view.showError("Connection Error")
                }
            }
        }
    }

    override fun getHomeTweets(userId: Long) {
        // get the main scope
        val scope = CoroutineScope(Dispatchers.Main)

        view.showLoader()
        scope.launch {
            // get the user tweets
            var tweets: ResponseList<Status>

            try {
                coroutineScope {
                    tweets = mRepository.getUserTweets(userId, TWEETS_COUNT).await()
                    view.showTweets(tweets.toMutableList())
                }
            }catch (e: Exception) {
                view.showError("Connection Error")
            }
        }
    }
}