package io.github.vnicius.twitterclone.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.adapters.click.AdapterClickHandler
import io.github.vnicius.twitterclone.ui.profile.ProfileActivity
import twitter4j.Status


class TweetsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tweets_list, container, false)
        val bundle = arguments
        val tweets: MutableList<Status> = bundle?.getSerializable(ARG_CODE) as MutableList<Status>
        val rv = view.findViewById<RecyclerView>(R.id.rv_tweets)

        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = TweetsAdapter(tweets, object : AdapterClickHandler<Status> {
            override fun onClick(view: View, tweet: Status) {
                val intent = Intent(view.context, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER_ID, tweet.user.id)
                startActivity(intent)
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