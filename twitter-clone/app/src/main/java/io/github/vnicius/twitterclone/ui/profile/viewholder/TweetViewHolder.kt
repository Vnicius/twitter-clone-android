package io.github.vnicius.twitterclone.ui.profile.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Status
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.utils.highlightClickable
import io.github.vnicius.twitterclone.utils.parseTweetTime
import io.github.vnicius.twitterclone.utils.summarizeCountNumber

class TweetViewHolder(itemView: View, private val listener: ItemClickListener<UserStatus>) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private val activeRetweetColor =
        ResourcesCompat.getColor(itemView.resources, R.color.green, null)
    private val inactiveItemColor =
        ResourcesCompat.getColor(itemView.resources, R.color.gray_dark, null)
    private val activeFavColor = ResourcesCompat.getColor(itemView.resources, R.color.red, null)

    /**
     * Bind the item with the view
     */
    fun bindView(item: UserStatus) {

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
            item.status.text.highlightClickable()

        // number of favourites
        itemView.findViewById<TextView>(R.id.tv_tweet_fav_count).text =
            item.status.favoriteCount.summarizeCountNumber()

        // number of retweets
        itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text =
            item.status.retweetCount.summarizeCountNumber()

        // time of the tweet
        itemView.findViewById<TextView>(R.id.tv_tweet_time).text =
            "• ${item.status.createdAt.parseTweetTime()}"

        // set the click listener of the fav button
        itemView.findViewById<LinearLayout>(R.id.ll_tweet_favorite)
            .setOnClickListener { view -> onFavClick(view, item.status) }
        setFavInactive(itemView)

        // set the click listener of the retweet button
        itemView.findViewById<LinearLayout>(R.id.ll_tweet_retweet)
            .setOnClickListener { view -> onRetweetClick(view, item.status) }
        setRetweetInactive(itemView)

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
        val tvCount = view.findViewById<TextView>(R.id.tv_tweet_fav_count)

        // check if the fav button is active
        if (tvCount.currentTextColor == inactiveItemColor) {
            setFavActive(view)

            tvCount.text = (item.favoriteCount + 1).summarizeCountNumber()
        } else {
            setFavInactive(view)

            tvCount.text = item.favoriteCount.summarizeCountNumber()
        }
    }

    private fun changeFavColor(view: View, color: Int) {
        val tvCount = view.findViewById<TextView>(R.id.tv_tweet_fav_count)

        tvCount.setTextColor(color)
    }

    private fun setFavActive(view: View) {
        val icon = view.findViewById<ImageView>(R.id.iv_tweet_fav_ic)
        val animation = view.findViewById<LottieAnimationView>(R.id.iv_tweet_fav_ic_animation)

        // hide the static icon
        icon.visibility = View.GONE

        // set and play the animation
        animation.apply {
            visibility = View.VISIBLE
            setMinFrame(10)
            speed = 2f
            playAnimation()
        }

        changeFavColor(view, activeFavColor)
    }

    private fun setFavInactive(view: View) {
        val icon = view.findViewById<ImageView>(R.id.iv_tweet_fav_ic)
        val animation = view.findViewById<LottieAnimationView>(R.id.iv_tweet_fav_ic_animation)

        // show static icon
        icon.visibility = View.VISIBLE

        // hide the animation
        animation.visibility = View.GONE

        changeFavColor(view, inactiveItemColor)
    }

    /**
     * Handle the retweet button
     *
     * @param view the item view
     * @param item the object item
     */
    private fun onRetweetClick(view: View, item: Status) {
        val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)

        // check if the retweet button is active
        if (tvCount.currentTextColor == inactiveItemColor) {
            // change the colors
            setRetweetActive(view)

            tvCount.text = (item.retweetCount + 1).summarizeCountNumber()
        } else {
            // change the colors
            setRetweetInactive(view)

            tvCount.text = item.retweetCount.summarizeCountNumber()
        }
    }

    private fun changeRetweetColor(view: View, color: Int) {
        val icon = view.findViewById<ImageView>(R.id.iv_tweet_retweet_ic)
        val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)

        icon.setColorFilter(color)
        tvCount.setTextColor(color)
    }

    private fun setRetweetActive(view: View) {
        changeRetweetColor(view, activeRetweetColor)
    }

    private fun setRetweetInactive(view: View) {
        changeRetweetColor(view, inactiveItemColor)
    }

    companion object {
        fun create(parent: ViewGroup, listener: ItemClickListener<UserStatus>): TweetViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
            return TweetViewHolder(view, listener)
        }
    }
}