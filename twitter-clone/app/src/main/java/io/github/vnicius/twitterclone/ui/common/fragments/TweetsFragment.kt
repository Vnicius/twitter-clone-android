package io.github.vnicius.twitterclone.ui.common.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.ui.common.adapters.AdapterClickHandler
import io.github.vnicius.twitterclone.ui.profile.ProfileActivity
import twitter4j.Status

/**
 * [Fragment] to show a list of tweets
 */
class TweetsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tweets_list, container, false)

        // get the list of tweets by the argument
        val bundle = arguments
        val tweets: MutableList<Status> = bundle?.getSerializable(ARG_CODE) as MutableList<Status>
        val rv = view.findViewById<RecyclerView>(R.id.rv_tweets)

        // inflate the RecyclerView
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter =
            TweetsAdapter(tweets, object :
                AdapterClickHandler<Status> {
                override fun onClick(view: View, tweet: Status) {
                    // open a new intent with the user profile
                    val intent = Intent(view.context, ProfileActivity::class.java)
                    intent.putExtra(ProfileActivity.USER_ID, tweet.user.id)
                    startActivity(intent)
                    activity!!.overridePendingTransition(R.anim.slide_left_in, R.anim.fade_out)
                }
            })

        return view
    }

    companion object {
        const val ARG_CODE = "TWEETS"
        @JvmStatic
        fun newInstance() =
            TweetsFragment()
    }
}