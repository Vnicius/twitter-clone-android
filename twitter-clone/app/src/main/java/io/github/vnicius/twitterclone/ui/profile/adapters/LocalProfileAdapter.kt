package io.github.vnicius.twitterclone.ui.profile.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.viewholder.TweetViewHolder
import io.github.vnicius.twitterclone.ui.profile.viewholder.ProfileContentViewHolder
import twitter4j.Status
import twitter4j.User

class LocalProfileAdapter(
    var user: User?,
    var tweets: List<Status>,
    val listener: ItemClickListener<Status>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ProfileContentViewHolder.create(parent) else TweetViewHolder.create(
            parent,
            listener
        )
    }

    override fun getItemCount(): Int {
        return tweets.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            user?.let { (holder as ProfileContentViewHolder).bindView(it) }
        } else {
            tweets[position].let { (holder as TweetViewHolder).bindView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }
}