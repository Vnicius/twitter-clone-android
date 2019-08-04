package io.github.vnicius.twitterclone.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Tweet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tweets_list.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private val mPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rv_tweets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_tweets.adapter = TweetsAdapter(arrayOf(1,2,3), object: SearchTweetClick{
            override fun onClick(view: View, tweet: Int) {
                Toast.makeText(view.context, tweet.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun showSearchMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showResult(tweets: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
