package io.github.vnicius.twitterclone.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.View
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.fragments.TweetsFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.searchfield.*
import twitter4j.Status
import java.io.Serializable

class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {

    private val mPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        rv_tweets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        search_item.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            search_item.id -> {
                val intent = Intent(this, SearchableActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun showSearchMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showResult(tweets: MutableList<Status>) {
//        rv_tweets.adapter =
//            TweetsAdapter(tweets, object : TweetClick {
//                override fun onClick(view: View, tweet: Status) {
//                    val intent = Intent(view.context, ProfileActivity::class.java)
//                    startActivity(intent)
//                }
//            })
    }

    override fun showNoResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

}
