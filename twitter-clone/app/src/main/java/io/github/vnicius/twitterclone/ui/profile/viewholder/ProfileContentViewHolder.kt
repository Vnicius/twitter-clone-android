package io.github.vnicius.twitterclone.ui.profile.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.utils.highlightClickable
import io.github.vnicius.twitterclone.utils.summarizeNumber

class ProfileContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvLocation = itemView.findViewById<TextView>(R.id.tv_profile_location)
    private val tvName = itemView.findViewById<TextView>(R.id.tv_profile_name)
    private val tvUsername = itemView.findViewById<TextView>(R.id.tv_profile_username)
    private val tvBio = itemView.findViewById<TextView>(R.id.tv_profile_bio)
    private val tvFollowingLabel = itemView.findViewById<TextView>(R.id.tv_profile_following_label)
    private val tvFollowersLabel = itemView.findViewById<TextView>(R.id.tv_profile_followers_label)
    private val ivAvatar = itemView.findViewById<ImageView>(R.id.iv_profile_avatar)

    fun bindView(user: User) {
        val userLocation = user.location

        // show the user location
        if (userLocation.isEmpty()) {
            tvLocation.visibility = View.GONE
        } else {
            tvLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(
                AppCompatResources.getDrawable(itemView.context, R.drawable.ic_location),
                null,
                null,
                null
            )
            tvLocation.text = userLocation
        }

        // user name in the profile
        tvName.text = user.name

        // user screen name
        tvUsername.text = "@${user.screenName}"

        // user bio
        if (user.description.isEmpty()) {
            tvBio.visibility = View.GONE
        } else {
            tvBio.text = user.description.highlightClickable()
        }
        // user count of followings
        tvFollowingLabel.text = user.friendsCount.summarizeNumber()

        // user count of followers
        tvFollowersLabel.text = user.followersCount.summarizeNumber()

        // set the user profile image
        Picasso.get().load(user.profileImageURLHttps)
            .placeholder(R.drawable.img_default_avatar)
            .error(R.drawable.img_default_avatar)
            .into(ivAvatar)
    }

    companion object {
        fun create(parent: ViewGroup): ProfileContentViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.partial_profile, parent, false)
            return ProfileContentViewHolder(view)
        }
    }
}