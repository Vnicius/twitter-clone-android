package io.github.vnicius.twitterclone.ui.common.adapters

import android.arch.paging.PagedListAdapter
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.text.HtmlCompat
import android.support.v7.util.DiffUtil
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
import io.github.vnicius.twitterclone.utils.highlightClickable
import io.github.vnicius.twitterclone.utils.parseTweetTime
import io.github.vnicius.twitterclone.utils.summarizeCountNumber
import twitter4j.Status

/**
 * Adapter to show the trends
 * @property tweets a list of Status objects
 * @property listener listener to handle the click in the item
 */
class TweetsAdapter(
    private val listener: ItemClickListener<Status>
) : PagedListAdapter<Status, TweetsAdapter.ViewHolder>(tweetsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)

        // add the view and the listener to the ViewHolder
        return ViewHolder(
            view,
            listener
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, index: Int) {
        vh.bindView(getItem(index)!!)
    }

    /**
     * Class to bind the object items with the view items
     * @property itemView view with the elements
     * @property listener to handle the clicks
     */
    class ViewHolder(itemView: View, private val listener: ItemClickListener<Status>) :
        RecyclerView.ViewHolder(itemView) {

        /**
         * Bind the item with the view
         */
        fun bindView(item: Status) {

            itemView.setOnClickListener {
                listener.onClick(it, item)
            }

            // user name and user screen name
            itemView.findViewById<TextView>(R.id.tv_tweet_user_name).text = HtmlCompat.fromHtml(
                "<b>${item.user.name}</b> @${item.user.screenName}",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )

            // tweet text
            itemView.findViewById<TextView>(R.id.tv_tweet_text).text =
                item.text.highlightClickable()

            // number of favourites
            itemView.findViewById<TextView>(R.id.tv_tweet_fav_count).text =
                item.favoriteCount.summarizeCountNumber()

            // number of retweets
            itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text =
                item.retweetCount.summarizeCountNumber()

            // time of the tweet
            itemView.findViewById<TextView>(R.id.tv_tweet_time).text =
                "• ${item.createdAt.time.parseTweetTime()}"

            // set the click listener of the fav button
            itemView.findViewById<LinearLayout>(R.id.ll_tweet_favorite)
                .setOnClickListener { view -> onFavClick(view, item) }

            // set the click listener of the retweet button
            itemView.findViewById<LinearLayout>(R.id.ll_tweet_retweet)
                .setOnClickListener { view -> onRetweetClick(view, item) }

            // get the user profile image and set in the view
            Picasso.get().load(item.user.profileImageURLHttps)
                .placeholder(R.drawable.img_default_avatar)
                .error(R.drawable.img_default_avatar)
                .into(itemView.findViewById<ImageView>(R.id.iv_tweet_user_avatar))
        }

        /**
         * Handle the favourite button
         *
         * @param view the item view
         * @param item the object item
         */
        private fun onFavClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.iv_tweet_fav_ic)
            val animation = view.findViewById<LottieAnimationView>(R.id.iv_tweet_fav_ic_animation)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_fav_count)

            // check if the fav button is active
            if (icon.visibility == View.VISIBLE) {

                // hide the static icon
                icon.visibility = View.GONE

                // set and play the animation
                animation.apply {
                    visibility = View.VISIBLE
                    setMinFrame(10)
                    speed = 2f
                    playAnimation()
                }

                // change the count color and value
                tvCount.apply {
                    setTextColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.red,
                            null
                        )
                    )
                    text = (item.favoriteCount + 1).summarizeCountNumber()
                }

            } else {
                // show static icon
                icon.visibility = View.VISIBLE

                // hide the animation
                animation.visibility = View.GONE

                // change the count color and value
                tvCount.apply {
                    setTextColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.gray_dark,
                            null
                        )
                    )
                    text = item.favoriteCount.summarizeCountNumber()
                }
            }
        }

        /**
         * Handle the retweet button
         *
         * @param view the item view
         * @param item the object item
         */
        private fun onRetweetClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.iv_tweet_retweet_ic)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)
            val green = ResourcesCompat.getColor(itemView.resources, R.color.green, null)
            val gray = ResourcesCompat.getColor(itemView.resources, R.color.gray_dark, null)

            // check if the retweet button is active
            if (tvCount.currentTextColor == gray) {
                // change the colors
                icon.setColorFilter(green)

                tvCount.apply {
                    setTextColor(green)
                    text = (item.retweetCount + 1).summarizeCountNumber()
                }
            } else {
                // change the colors
                icon.setColorFilter(gray)

                tvCount.apply {
                    setTextColor(gray)
                    text = item.retweetCount.summarizeCountNumber()
                }
            }
        }
    }

    companion object {
        val tweetsDiffCallback = object : DiffUtil.ItemCallback<Status>() {
            override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean =
                oldItem.id == newItem.id
        }
    }
}
