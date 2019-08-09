package io.github.vnicius.twitterclone.ui.profile

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.fragments.LoaderFragment
import io.github.vnicius.twitterclone.fragments.TweetsFragment
import io.github.vnicius.twitterclone.utils.ParseUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.tweet_item.*
import kotlinx.android.synthetic.main.tweets_list.*
import twitter4j.Status
import twitter4j.User
import java.io.Serializable

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private var mUserId: Long = -1
    private lateinit var mUser: User
    private val mPresenter: ProfileContract.Presenter = ProfilePresenter(this)
    private lateinit var mTransaction: FragmentTransaction

    companion object{
        const val USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar_profile)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mUserId = intent.getLongExtra(USER_ID, -1)

        mPresenter.getUser(mUserId)
        mPresenter.getHomeTweets(mUserId)

        app_bar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appbar: AppBarLayout?, verticalOffset: Int) {
                if(appbar?.totalScrollRange!! + verticalOffset == 0) {
                    toolbar_profile_infos.visibility = View.VISIBLE
                } else {
                    toolbar_profile_infos.visibility = View.GONE
                }
            }

        })
    }

    private fun changeFragment(fragment: Fragment) {
        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(R.id.frame_profile, fragment)
        mTransaction.commitAllowingStateLoss()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showUser(user: User) {
        mUser = user

        val userLocation = mUser.location
        val userBGColor = Color.parseColor("#${mUser.profileBackgroundColor}")
        val userTextColor = Color.parseColor("#${mUser.profileTextColor}")

        if(userLocation == null) {
            user_profile_location.visibility = View.GONE
        } else {
            tv_user_profile_location.text = userLocation
        }

        tv_toolbar_user_name.text = mUser.name
        tv_toolbar_user_name.setTextColor(userTextColor)
        tv_toolbar_tweets_number.text = ParseUtils.parseNumber(mUser.statusesCount)
        tv_toolbar_tweets_number.setTextColor(userTextColor)
        tv_tweet_label.setTextColor(userTextColor)

        tv_user_profile_name.text = mUser.name
        tv_user_profile_username.text = "@${mUser.screenName}"
        tv_user_profile_bio.text = ParseUtils.parseTweetText(mUser.description)
        tv_user_profile_following.text = ParseUtils.parseNumber(mUser.friendsCount)
        tv_user_profile_followers.text = ParseUtils.parseNumber(mUser.followersCount)

        iv_toolbar_user_header.setBackgroundColor(userBGColor)
        app_bar.setBackgroundColor(userBGColor)
        toolbar_layout.setContentScrimColor(userBGColor)
        supportActionBar?.setHomeAsUpIndicator(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back, null)?.apply {
            setColorFilter(userTextColor, PorterDuff.Mode.SRC_ATOP)
        })

        Picasso.get().load(mUser.profileBanner600x200URL)
            .fit()
            .into(iv_toolbar_user_header)

        Picasso.get().load(mUser.profileImageURLHttps)
            .placeholder(R.drawable.deafult_avatar)
            .error(R.drawable.deafult_avatar)
            .into(iv_user_profile_avatar)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showTweets(tweets: MutableList<Status>) {
        val fragment = TweetsFragment.newInstance()
        val args = Bundle()
        args.putSerializable(TweetsFragment.ARG_CODE, tweets as Serializable)
        fragment.arguments = args

        changeFragment(fragment)
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }
}
