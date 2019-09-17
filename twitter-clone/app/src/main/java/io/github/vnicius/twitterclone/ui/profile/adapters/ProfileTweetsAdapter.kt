package io.github.vnicius.twitterclone.ui.profile.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.viewholder.TweetViewHolder
import io.github.vnicius.twitterclone.ui.profile.viewholder.ProfileContentViewHolder
import twitter4j.Status

class ProfileTweetsAdapter(
    var user: User?,
    private val listener: ItemClickListener<Status>
) : PagedListAdapter<Status, RecyclerView.ViewHolder>(tweetsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ProfileContentViewHolder.create(parent) else TweetViewHolder.create(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, index: Int) {
        if (getItemViewType(index) == 0) {
            user?.let { (vh as ProfileContentViewHolder).bindView(it) }
        } else {
            getItem(index - 1)?.let { (vh as TweetViewHolder).bindView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    companion object {
        val tweetsDiffCallback = object : DiffUtil.ItemCallback<Status>() {
            override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean =
                (oldItem.retweetCount == newItem.retweetCount) && (oldItem.favoriteCount == newItem.favoriteCount)
        }
    }
}