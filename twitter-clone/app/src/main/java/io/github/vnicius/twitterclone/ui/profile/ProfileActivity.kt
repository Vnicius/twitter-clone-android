package io.github.vnicius.twitterclone.ui.profile

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import io.github.vnicius.twitterclone.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.tweets_list.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rv_tweets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rv_tweets.adapter = TweetsAdapter(
//            arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
//            object : TweetClick {
//                override fun onClick(view: View, tweet: Int) {
//                }
//            })

        app_bar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appbar: AppBarLayout?, verticalOffset: Int) {
                if(appbar?.totalScrollRange!! + verticalOffset == 0) {
                    toolbar_profile_infos.visibility = View.VISIBLE
                } else {
                    toolbar_profile_infos.visibility = View.GONE
                }
            }

        })
//        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
//        viewPagerAdapter.addFragment(TweetsFragment(), "Teste")
//        viewPagerAdapter.addFragment(TweetsFragment(), "Teste")
//
//        viewpager_tweets.adapter = viewPagerAdapter
//
//        profile_tabs.setupWithViewPager(viewpager_tweets)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
