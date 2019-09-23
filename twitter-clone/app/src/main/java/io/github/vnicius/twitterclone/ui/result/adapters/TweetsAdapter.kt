package io.github.vnicius.twitterclone.ui.result.adapters

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.viewholder.TweetViewHolder
import twitter4j.Status

/**
 * Adapter to show the trends
 * @property tweets a list of Status objects
 * @property listener listener to handle the click in the item
 */
class TweetsAdapter(
    private val listener: ItemClickListener<UserStatus>
) : PagedListAdapter<UserStatus, TweetViewHolder>(tweetsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder.create(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(vh: TweetViewHolder, index: Int) {
        getItem(index)?.let { vh.bindView(it) }
    }

    companion object {
        val tweetsDiffCallback = object : DiffUtil.ItemCallback<UserStatus>() {
            override fun areItemsTheSame(oldItem: UserStatus, newItem: UserStatus): Boolean =
                oldItem.status.statusId == newItem.status.statusId

            override fun areContentsTheSame(oldItem: UserStatus, newItem: UserStatus): Boolean =
                (oldItem.status.retweetCount == newItem.status.retweetCount)
                        && (oldItem.status.favoriteCount == newItem.status.favoriteCount)
        }
    }
}
