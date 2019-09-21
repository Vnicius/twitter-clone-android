package io.github.vnicius.twitterclone.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.utils.highlightClickable
import io.github.vnicius.twitterclone.utils.parseTweetTime
import io.github.vnicius.twitterclone.utils.summarizeCountNumber
import twitter4j.MediaEntity
import twitter4j.Status

class TweetViewHolder(itemView: View, private val listener: ItemClickListener<Status>) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private val activeRetweetColor =
        ResourcesCompat.getColor(itemView.resources, R.color.green, null)
    private val inactiveItemColor =
        ResourcesCompat.getColor(itemView.resources, R.color.gray_dark, null)
    private val activeFavColor = ResourcesCompat.getColor(itemView.resources, R.color.red, null)
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_tweet_user_name)
    private val tvText = itemView.findViewById<TextView>(R.id.tv_tweet_text)
    private val tvFavCount = itemView.findViewById<TextView>(R.id.tv_tweet_fav_count)
    private val tvRetweetCount = itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count)
    private val tvTweetTime = itemView.findViewById<TextView>(R.id.tv_tweet_time)
    private val llFav = itemView.findViewById<LinearLayout>(R.id.ll_tweet_favorite)
    private val llRetweet = itemView.findViewById<LinearLayout>(R.id.ll_tweet_retweet)
    private val ivUserAvatar = itemView.findViewById<ImageView>(R.id.iv_tweet_user_avatar)
    private val icFav = itemView.findViewById<ImageView>(R.id.iv_tweet_fav_ic)
    private val icFavAnimation =
        itemView.findViewById<LottieAnimationView>(R.id.iv_tweet_fav_ic_animation)
    private val icRetweet = itemView.findViewById<ImageView>(R.id.iv_tweet_retweet_ic)
    private val ivTweetBodyImages =
        listOf<ImageView>(
            itemView.findViewById(R.id.iv_tweet_body_0),
            itemView.findViewById(R.id.iv_tweet_body_1),
            itemView.findViewById(R.id.iv_tweet_body_2),
            itemView.findViewById(R.id.iv_tweet_body_3)
        )
    private val cvTweetBodyImages = listOf<CardView>(
        itemView.findViewById(R.id.cv_tweet_body_0),
        itemView.findViewById(R.id.cv_tweet_body_1),
        itemView.findViewById(R.id.cv_tweet_body_2),
        itemView.findViewById(R.id.cv_tweet_body_3)
    )
    private val clImages = itemView.findViewById<ConstraintLayout>(R.id.cl_tweet_images)

    /**
     * Bind the item with the view
     */
    fun bindView(item: Status) {

        var text = item.text

        itemView.setOnClickListener {
            listener.onClick(it, item)
        }

        // user name and user screen name
        tvUserName.text = HtmlCompat.fromHtml(
            "<b>${item.user.name}</b> @${item.user.screenName}",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )

        if (item.mediaEntities.isNotEmpty()) {
            showImageViews(item.mediaEntities.size)
            showImages(item)

            text = text.replace(item.mediaEntities[0].url, "")
        } else {
            clImages.visibility = View.GONE
        }

        if (text.isEmpty()) {
            tvText.visibility = View.GONE
        } else {
            tvText.visibility = View.VISIBLE
            tvText.text = text.highlightClickable()
        }

        tvFavCount.text = item.favoriteCount.summarizeCountNumber()
        tvRetweetCount.text = item.retweetCount.summarizeCountNumber()
        tvTweetTime.text = "• ${item.createdAt.time.parseTweetTime()}"

        llFav.setOnClickListener { view -> onFavClick(view, item) }
        setFavInactive(itemView)

        llRetweet.setOnClickListener { view -> onRetweetClick(view, item) }
        setRetweetInactive(itemView)

        Picasso.get().load(item.user.profileImageURLHttps)
            .placeholder(R.drawable.img_default_avatar)
            .error(R.drawable.img_default_avatar)
            .into(ivUserAvatar)
    }

    /**
     * Handle the favourite button
     *
     * @param view the item view
     * @param item the object item
     */
    private fun onFavClick(view: View, item: Status) {

        // check if the fav button is active
        if (tvFavCount.currentTextColor == inactiveItemColor) {
            setFavActive(view)

            tvFavCount.text = (item.favoriteCount + 1).summarizeCountNumber()
        } else {
            setFavInactive(view)

            tvFavCount.text = item.favoriteCount.summarizeCountNumber()
        }
    }

    private fun changeFavColor(view: View, color: Int) {
        tvFavCount.setTextColor(color)
    }

    private fun setFavActive(view: View) {
        // hide the static icon
        icFav.visibility = View.GONE

        // set and play the animation
        icFavAnimation.apply {
            visibility = View.VISIBLE
            setMinFrame(10)
            speed = 2f
            playAnimation()
        }

        changeFavColor(view, activeFavColor)
    }

    private fun setFavInactive(view: View) {
        // show static icon
        icFav.visibility = View.VISIBLE

        // hide the animation
        icFavAnimation.visibility = View.GONE

        changeFavColor(view, inactiveItemColor)
    }

    /**
     * Handle the retweet button
     *
     * @param view the item view
     * @param item the object item
     */
    private fun onRetweetClick(view: View, item: Status) {
        // check if the retweet button is active
        if (tvRetweetCount.currentTextColor == inactiveItemColor) {
            // change the colors
            setRetweetActive(view)

            tvRetweetCount.text = (item.retweetCount + 1).summarizeCountNumber()
        } else {
            // change the colors
            setRetweetInactive(view)

            tvRetweetCount.text = item.retweetCount.summarizeCountNumber()
        }
    }

    private fun changeRetweetColor(view: View, color: Int) {
        icRetweet.setColorFilter(color)
        tvRetweetCount.setTextColor(color)
    }

    private fun setRetweetActive(view: View) {
        changeRetweetColor(view, activeRetweetColor)
    }

    private fun setRetweetInactive(view: View) {
        changeRetweetColor(view, inactiveItemColor)
    }

    private fun showImages(item: Status) {
        val constraintSet = ConstraintSet()
        val defaultSpace = itemView.resources.getDimensionPixelSize(R.dimen.spacing_tiny)
        constraintSet.clone(clImages)

        clImages.visibility = View.VISIBLE

        fun fullHeight(position: Int) {
            constraintSet.apply {
                connect(
                    cvTweetBodyImages[position].id,
                    ConstraintSet.BOTTOM,
                    clImages.id,
                    ConstraintSet.BOTTOM,
                    0
                )
            }
        }

        fun fullWidth(position: Int) {
            constraintSet.apply {
                connect(
                    cvTweetBodyImages[position].id,
                    ConstraintSet.END,
                    clImages.id,
                    ConstraintSet.END,
                    0
                )
            }
        }

        fun halfHeight(position: Int) {
            constraintSet.apply {
                connect(
                    cvTweetBodyImages[position].id,
                    ConstraintSet.BOTTOM,
                    R.id.guideline_horizontal,
                    ConstraintSet.TOP,
                    defaultSpace
                )
            }
        }

        fun halfWidthEnd(position: Int) {
            constraintSet.apply {
                connect(
                    cvTweetBodyImages[position].id,
                    ConstraintSet.END,
                    R.id.guideline_vertical,
                    ConstraintSet.START,
                    defaultSpace
                )
            }
        }

        when (item.mediaEntities.size) {
            1 -> {
                fullWidth(0)
                fullHeight(0)
            }

            2 -> {
                halfWidthEnd(0)
                fullHeight(0)
                fullHeight(1)
            }

            3 -> {
                halfWidthEnd(0)
                halfHeight(0)
                fullHeight(1)
            }

            4 -> {
                halfWidthEnd(0)
                halfHeight(0)
                halfHeight(1)
            }
        }

        constraintSet.applyTo(clImages)
        getImages(item.mediaEntities)
    }

    private fun showImageViews(size: Int) {
        cvTweetBodyImages.forEachIndexed { index, cardView ->
            cardView.visibility = if (index < size) View.VISIBLE else View.GONE
        }
    }

    private fun getImages(medias: Array<MediaEntity>) {
        medias.forEachIndexed { index, media ->
            Picasso.get().load(media.mediaURLHttps)
                .fit()
                .centerCrop()
                .into(ivTweetBodyImages[index])
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ItemClickListener<Status>): TweetViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
            return TweetViewHolder(view, listener)
        }
    }
}