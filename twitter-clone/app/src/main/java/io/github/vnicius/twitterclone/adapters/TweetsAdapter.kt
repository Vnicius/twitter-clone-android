package io.github.vnicius.twitterclone.adapters

import android.support.v4.text.HtmlCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.utils.TweetClick
import twitter4j.Status

class TweetsAdapter(private val tweets: MutableList<Status>, val listener: TweetClick): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
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

        fun bindView(item: Status) {
            itemView.findViewById<TextView>(R.id.tv_user_name).text = HtmlCompat.fromHtml("<b>${item.user.name}</b> @${item.user.screenName}", HtmlCompat.FROM_HTML_MODE_COMPACT)
            itemView.findViewById<TextView>(R.id.tv_tweet_text).text = item.text
            itemView.findViewById<TextView>(R.id.tv_tweet_favs_count).text = if(item.favoriteCount == 0) "" else item.favoriteCount.toString()
            itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text = if(item.retweetCount == 0) "" else item.retweetCount.toString()
            Picasso.get().load(item.user.profileImageURLHttps)
                .placeholder(R.drawable.deafult_avatar)
                .error(R.drawable.deafult_avatar)
                .into(itemView.findViewById<ImageView>(R.id.iv_user_avatar))
        }
    }
}