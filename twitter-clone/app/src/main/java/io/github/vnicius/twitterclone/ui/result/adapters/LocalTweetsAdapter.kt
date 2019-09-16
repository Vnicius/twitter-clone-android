package io.github.vnicius.twitterclone.ui.result.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.viewholder.TweetViewHolder
import twitter4j.Status

class LocalTweetsAdapter(var tweets: List<Status>, val listener: ItemClickListener<Status>) :
    RecyclerView.Adapter<TweetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder.create(parent, listener)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bindView(tweets[position])
    }
}