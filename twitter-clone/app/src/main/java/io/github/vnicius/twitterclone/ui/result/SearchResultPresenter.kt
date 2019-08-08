package io.github.vnicius.twitterclone.ui.result

import io.github.vnicius.twitterclone.api.APIInterface
import io.github.vnicius.twitterclone.api.TwitterAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchResultPresenter(val view: SearchResultContract.View): SearchResultContract.Presenter {

    private val api: APIInterface = TwitterAPI()

    override fun searchTweets(query: String) {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            val result = api.search(query).await()
            view.showResult(result)
        }
    }
}