package io.github.vnicius.twitterclone.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.utils.TweetClick

class TweetsAdapter(private val tweets: Array<Int>, val listener: TweetClick): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tweet_item, parent, false)

        return ViewHolder(
            view,
            object : OnClickTweetListener {
                override fun onClick(view: View, position: Int) {
                    listener.onClick(view, tweets[position])
                }
            })
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    override fun onBindViewHolder(vh: ViewHolder, index: Int) {
        vh.bindView(tweets[index])
    }

    interface OnClickTweetListener{
        fun onClick(view: View, position: Int)
    }

    class ViewHolder(itemView: View, private val listener: OnClickTweetListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener.onClick(view!!, adapterPosition)
        }

        fun bindView(item: Int) {
            itemView.findViewById<TextView>(R.id.tv_tweet_time).text = item.toString()
        }
    }
}