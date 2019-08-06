package io.github.vnicius.twitterclone.ui.profile

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
import io.github.vnicius.twitterclone.utils.TweetClick

class TweetsFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tweets_list, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_tweets)
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = TweetsAdapter(arrayOf(1, 2, 3), object : TweetClick {
            override fun onClick(view: View, tweet: Int) {
                val intent = Intent(view.context, ProfileActivity::class.java)
                startActivity(intent)
            }
        })

        return view
    }
}