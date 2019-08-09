package io.github.vnicius.twitterclone.ui.profile

import io.github.vnicius.twitterclone.data.repository.user.IUserRepository
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

const val TWEETS_COUNT = 5

class ProfilePresenter(val view: ProfileContract.View): ProfileContract.Presenter {
    private val mRepository: IUserRepository = UserRepository()

    override fun getUser(userId: Long) {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            val user = mRepository.getUser(userId).await()
            view.showUser(user)
        }
    }

    override fun getHomeTweets(userId: Long) {
        val scope = CoroutineScope(Dispatchers.Main)

        view.showLoader()
        scope.launch {
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