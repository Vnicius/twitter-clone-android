package io.github.vnicius.twitterclone.adapters

import android.support.v4.content.res.ResourcesCompat
import android.support.v4.text.HtmlCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.adapters.click.AdapterClickHandler
import io.github.vnicius.twitterclone.utils.ParseUtils
import twitter4j.Status
import java.util.*

class TweetsAdapter(private val tweets: MutableList<Status>, val listener: AdapterClickHandler<Status>): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
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
            itemView.findViewById<TextView>(R.id.tv_tweet_text).text = ParseUtils.parseTweetText(item.text)
            itemView.findViewById<TextView>(R.id.tv_tweet_favs_count).text = ParseUtils.parseCountNumber(item.favoriteCount)
            itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text = ParseUtils.parseCountNumber(item.retweetCount)

            Picasso.get().load(item.user.profileImageURLHttps)
                .placeholder(R.drawable.deafult_avatar)
                .error(R.drawable.deafult_avatar)
                .into(itemView.findViewById<ImageView>(R.id.iv_user_avatar))

            itemView.findViewById<TextView>(R.id.tv_tweet_time).text = "• ${ParseUtils.parseTime(item.createdAt.time)}"
            itemView.findViewById<LinearLayout>(R.id.btn_favorite).setOnClickListener { view -> onFavClick(view, item) }
            itemView.findViewById<LinearLayout>(R.id.btn_retweet).setOnClickListener { view -> onRetweetClick(view, item) }
        }

        private fun onFavClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.icon_fav)
            val animation = view.findViewById<LottieAnimationView>(R.id.icon_fav_animation)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_favs_count)

            if(icon.visibility == View.VISIBLE) {
                icon.visibility = View.GONE

                animation.visibility = View.VISIBLE
                animation.setMinFrame(10)
                animation.speed = 2f
                animation.playAnimation()

                tvCount.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.red, null))
                tvCount.text = ParseUtils.parseCountNumber(item.favoriteCount + 1)
            } else {
                icon.visibility = View.VISIBLE

                animation.visibility = View.GONE

                tvCount.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.darkGray, null))
                tvCount.text = ParseUtils.parseCountNumber(item.favoriteCount)
            }
        }

        private fun onRetweetClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.icon_retweet)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)
            val green = ResourcesCompat.getColor(itemView.resources, R.color.green, null)
            val gray = ResourcesCompat.getColor(itemView.resources, R.color.darkGray, null)

            if(tvCount.currentTextColor == gray) {
                icon.setColorFilter(green)
                tvCount.setTextColor(green)

                tvCount.text = ParseUtils.parseCountNumber(item.retweetCount + 1)
            } else {
                icon.setColorFilter(gray)

                tvCount.setTextColor(gray)
                tvCount.text = ParseUtils.parseCountNumber(item.retweetCount)
            }
        }
    }
}