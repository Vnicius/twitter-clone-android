package io.github.vnicius.twitterclone.ui.profile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.utils.State
import io.github.vnicius.twitterclone.utils.highlightClickable
import io.github.vnicius.twitterclone.utils.summarizeNumber
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.partial_profile_content.*
import twitter4j.Status
import twitter4j.User

/**
 * Profile Activity View
 */
class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private val presenter: ProfileContract.Presenter = ProfilePresenter(this)
    private lateinit var tweetsAdapter: TweetsAdapter
    private var currentUserID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar_profile)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        currentUserID = intent.getLongExtra(USER_ID, -1)

        presenter.getUser(currentUserID)
        presenter.buildTweets(currentUserID)

        setupTweetsRecyclerView()
        initState()

        // handle the appbar scroll to show some texts
        app_bar_profile.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->
            ll_profile_toolbar_user_infos.visibility =
                if (appbar?.totalScrollRange!! + verticalOffset == 0) View.VISIBLE else View.GONE
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun showUser(user: User) {

        val userLocation = user.location
        val userBGColor = Color.parseColor("#${user.profileBackgroundColor}")
        val textColor =
            if (user.profileTextColor == user.profileBackgroundColor) "FFFFFF" else user.profileTextColor
        val userTextColor = Color.parseColor("#$textColor")

        // show the user location
        if (userLocation.isEmpty()) {
            tv_profile_content_location.visibility = View.GONE
        } else {
            tv_profile_content_location.text = userLocation
        }

        // user name in the toolbar
        tv_profile_toolbar_user_name.text = user.name
        tv_profile_toolbar_user_name.setTextColor(userTextColor)

        // user number of tweets
        tv_profile_toolbar_tweets_number.text = user.statusesCount.summarizeNumber()
        tv_profile_toolbar_tweets_number.setTextColor(userTextColor)
        tv_profile_toolbar_tweet_label.setTextColor(userTextColor)

        // user name in the profile
        tv_user_profile_name.text = user.name

        // user screen name
        tv_profile_content_username.text = "@${user.screenName}"

        // user bio
        if (user.description.isEmpty()) {
            tv_profile_content_bio.visibility = View.GONE
        } else {
            tv_profile_content_bio.text = user.description.highlightClickable()
        }
        // user count of followings
        tv_profile_content_following_label.text = user.friendsCount.summarizeNumber()

        // user count of followers
        tv_user_profile_content_followers_label.text = user.followersCount.summarizeNumber()

        // toolbar color
        userBGColor.let {
            iv_profile_toolbar_user_header.setBackgroundColor(it)
            app_bar_profile.setBackgroundColor(it)
            toolbar_layout_profile.setContentScrimColor(it)
        }

        // set back button color
        supportActionBar?.setHomeAsUpIndicator(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_arrow_back,
                null
            )?.apply {
                setColorFilter(userTextColor, PorterDuff.Mode.SRC_ATOP)
            })

        // set the header image
        Picasso.get().load(user.profileBanner600x200URL)
            .fit()
            .into(iv_profile_toolbar_user_header)

        // set the user profile image
        Picasso.get().load(user.profileImageURLHttps)
            .placeholder(R.drawable.img_default_avatar)
            .error(R.drawable.img_default_avatar)
            .into(iv_user_profile_avatar)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showTweets() {
        hideContent()
        rv_profile_content_tweets_list.visibility = View.VISIBLE
    }

    override fun showLoader() {
        hideContent()
        inc_profile_content_tweets_spinner.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun hideContent() {
        inc_profile_content_tweets_spinner.visibility = View.GONE
        rv_profile_content_tweets_list.visibility = View.GONE
    }

    private fun setupTweetsRecyclerView() {
        tweetsAdapter = TweetsAdapter(object : ItemClickListener<Status> {
            override fun onClick(view: View, item: Status) {
                val intent = Intent(view.context, ProfileActivity::class.java)
                intent.putExtra(USER_ID, item.user.id)

                startActivity(intent)

                overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_fade_out
                )
            }
        })

        rv_profile_content_tweets_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = tweetsAdapter
        }

        presenter.getTweetsValue().observe(this, Observer {
            tweetsAdapter.submitList(it)
        })
    }

    private fun initState() {
        presenter.getTweetsState().observe(this, Observer {
            when (it) {
                State.LOADING -> showLoader()
                State.DONE -> showTweets()
            }
        })
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
