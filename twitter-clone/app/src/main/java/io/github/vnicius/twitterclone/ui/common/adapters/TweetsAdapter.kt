package io.github.vnicius.twitterclone.ui.common.adapters

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
import io.github.vnicius.twitterclone.utils.ParseUtils
import twitter4j.Status

/**
 * Adapter to show the trends
 * @property tweets a list of Status objects
 * @property listener listener to handle the click in the item
 */
class TweetsAdapter(private val tweets: MutableList<Status>, val listener: AdapterClickHandler<Status>): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tweet_item, parent, false)

        // add the view and the listener to the ViewHolder
        return ViewHolder(
            view,
            object :
                OnClickTweetListener {
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

    /**
     * Interface to handle click with the item position
     */
    interface OnClickTweetListener{
        fun onClick(view: View, position: Int)
    }

    /**
     * Class to bind the object items with the view items
     * @property itemView view with the elements
     * @property listener to handle the clicks
     */
    class ViewHolder(itemView: View, private val listener: OnClickTweetListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener.onClick(view!!, adapterPosition)
        }

        /**
         * Bind the item with the view
         */
        fun bindView(item: Status) {

            // user name and user screen name
            itemView.findViewById<TextView>(R.id.tv_user_name).text = HtmlCompat.fromHtml("<b>${item.user.name}</b> @${item.user.screenName}", HtmlCompat.FROM_HTML_MODE_COMPACT)

            // tweet text
            itemView.findViewById<TextView>(R.id.tv_tweet_text).text = ParseUtils.parseTweetText(item.text)

            // number of favourites
            itemView.findViewById<TextView>(R.id.tv_tweet_favs_count).text = ParseUtils.parseCountNumber(item.favoriteCount)

            // number of retweets
            itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text = ParseUtils.parseCountNumber(item.retweetCount)

            // time of the tweet
            itemView.findViewById<TextView>(R.id.tv_tweet_time).text = "• ${ParseUtils.parseTime(item.createdAt.time)}"

            // set the click listener of the fav button
            itemView.findViewById<LinearLayout>(R.id.btn_favorite).setOnClickListener { view -> onFavClick(view, item) }

            // set the click listener of the retweet button
            itemView.findViewById<LinearLayout>(R.id.btn_retweet).setOnClickListener { view -> onRetweetClick(view, item) }

            // get the user profile image and set in the view
            Picasso.get().load(item.user.profileImageURLHttps)
                .placeholder(R.drawable.deafult_avatar)
                .error(R.drawable.deafult_avatar)
                .into(itemView.findViewById<ImageView>(R.id.iv_user_avatar))
        }

        /**
         * Handle the favourite button
         *
         * @param view the item view
         * @param item the object item
         */
        private fun onFavClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.icon_fav)
            val animation = view.findViewById<LottieAnimationView>(R.id.icon_fav_animation)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_favs_count)

            // check if the fav button is active
            if(icon.visibility == View.VISIBLE) {

                // hide the static icon
                icon.visibility = View.GONE

                // set and play the animation
                animation.visibility = View.VISIBLE
                animation.setMinFrame(10)
                animation.speed = 2f
                animation.playAnimation()

                // change the count color and value
                tvCount.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.red, null))
                tvCount.text = ParseUtils.parseCountNumber(item.favoriteCount + 1)
            } else {
                // show static icon
                icon.visibility = View.VISIBLE

                // hide the animation
                animation.visibility = View.GONE

                // change the count color and value
                tvCount.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.darkGray, null))
                tvCount.text = ParseUtils.parseCountNumber(item.favoriteCount)
            }
        }

        /**
         * Handle the retweet button
         *
         * @param view the item view
         * @param item the object item
         */
        private fun onRetweetClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.icon_retweet)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)
            val green = ResourcesCompat.getColor(itemView.resources, R.color.green, null)
            val gray = ResourcesCompat.getColor(itemView.resources, R.color.darkGray, null)

            // check if the retweet button is active
            if(tvCount.currentTextColor == gray) {
                // change the colors
                icon.setColorFilter(green)
                tvCount.setTextColor(green)

                // change the count value
                tvCount.text = ParseUtils.parseCountNumber(item.retweetCount + 1)
            } else {
                // change the colors
                icon.setColorFilter(gray)
                tvCount.setTextColor(gray)

                // change the count value
                tvCount.text = ParseUtils.parseCountNumber(item.retweetCount)
            }
        }
    }
}