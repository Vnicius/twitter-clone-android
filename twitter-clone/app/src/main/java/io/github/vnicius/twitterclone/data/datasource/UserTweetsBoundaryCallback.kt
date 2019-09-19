package io.github.vnicius.twitterclone.data.datasource

import androidx.paging.PagedList
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.data.repository.Repository
import io.github.vnicius.twitterclone.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserTweetsBoundaryCallback(
    val userId: Long,
    val pageSize: Int,
    val scope: CoroutineScope,
    val userRepository: Repository<UserRepository>
) : PagedList.BoundaryCallback<UserStatus>() {

    private var page = 1
    private var hasRequest = true

    override fun onZeroItemsLoaded() {
        requestAndSave()
    }

    override fun onItemAtEndLoaded(itemAtEnd: UserStatus) {
        requestAndSave()
    }

    private fun requestAndSave() {
        if (!hasRequest) {
            return
        }

        scope.launch {
            val tweets = userRepository.remote.getUserTweetsAsync(userId, pageSize, page)

            if (tweets != null) {
                userRepository.local.saveUserTweetsAsync(tweets)
                page++
            } else {
                hasRequest = false
            }
        }
    }
}