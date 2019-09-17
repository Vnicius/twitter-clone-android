package io.github.vnicius.twitterclone.ui.profile

import androidx.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.profile.adapters.LocalProfileAdapter
import io.github.vnicius.twitterclone.ui.profile.adapters.ProfileTweetsAdapter
import io.github.vnicius.twitterclone.utils.State
import io.github.vnicius.twitterclone.utils.summarizeNumber
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.partial_connection_error.*
import twitter4j.Status


/**
 * Profile Activity View
 */
class ProfileActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener<Status> {

    private lateinit var viewModel: ProfileViewModel
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

        viewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]
        viewModel.getUser(currentUserID)
        viewModel.buildTweets(currentUserID)

        setupLocalTweetsRecyclerView()
        setupTweetsRecyclerView()
        initTweetsState()
        initUserState()

        // handle the appbar scroll to show some texts
        app_bar_profile.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->
            ll_profile_toolbar_user_infos.visibility =
                if (appbar?.totalScrollRange!! + verticalOffset == 0) View.VISIBLE else View.GONE
        })

        btn_connection_error_action.setOnClickListener(this)
        swipe_profile_refresh.apply {
            setOnRefreshListener { refresh() }
            setColorSchemeColors(ContextCompat.getColor(this.context, R.color.blue))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    override fun onClick(view: View) {
        when (view.id) {
            btn_connection_error_action.id -> {
                refresh()
            }
        }
    }

    override fun onClick(view: View, item: Status) {
        val intent = Intent(view.context, ProfileActivity::class.java)
        intent.putExtra(USER_ID, item.user.id)

        startActivity(intent)

        overridePendingTransition(
            R.anim.anim_slide_in_left,
            R.anim.anim_fade_out
        )
    }

    private fun setupToolbarData(user: User) {
        val userBGColor = Color.parseColor("#${user.profileBackgroundColor}")
        val textColor =
            if (user.profileTextColor == user.profileBackgroundColor) "FFFFFF" else user.profileTextColor
        val userTextColor = Color.parseColor("#$textColor")

        // user name in the toolbar
        tv_profile_toolbar_user_name.text = user.name
        tv_profile_toolbar_user_name.setTextColor(userTextColor)

        // user number of tweets
        tv_profile_toolbar_tweets_number.text = user.statusesCount.summarizeNumber()
        tv_profile_toolbar_tweets_number.setTextColor(userTextColor)
        tv_profile_toolbar_tweet_label.setTextColor(userTextColor)

        // toolbar color
        userBGColor.let {
            iv_profile_toolbar_user_header.setBackgroundColor(it)
            app_bar_profile.setBackgroundColor(it)
            toolbar_layout_profile.setContentScrimColor(it)
        }

        // set back button color
        supportActionBar?.setHomeAsUpIndicator(
            AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back)?.apply {
                setColorFilter(userTextColor, PorterDuff.Mode.SRC_ATOP)
            })

        // set the header image
        Picasso.get().load(user.profileBanner600x200URL)
            .fit()
            .into(iv_profile_toolbar_user_header)
    }

    private fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    private fun showTweets() {
        hideContent()
        rv_profile_tweets_list.visibility = View.VISIBLE
        swipe_profile_refresh.isRefreshing = false
    }

    private fun showLoader() {
        hideContent()
        inc_profile_tweets_spinner.visibility = View.VISIBLE

        viewModel.userData.observe(this, Observer {
            rv_profile_local_tweets.visibility = View.VISIBLE
        })
    }

    private fun showConnectionError() {
        hideContent()
        inc_profile_connection_error.visibility = View.VISIBLE
    }

    private fun hideContent() {
        inc_profile_tweets_spinner.visibility = View.GONE
        inc_profile_connection_error.visibility = View.GONE
        rv_profile_tweets_list.visibility = View.GONE
        rv_profile_local_tweets.visibility = View.GONE
    }

    private fun setupTweetsRecyclerView() {
        val profileTweetsAdapter = ProfileTweetsAdapter(null, this)

        rv_profile_tweets_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = profileTweetsAdapter
        }

        viewModel.userData.observe(this, Observer {
            profileTweetsAdapter.user = it
            profileTweetsAdapter.notifyDataSetChanged()
            setupToolbarData(it)
        })

        viewModel.homeTweetsList.observe(this, Observer {
            profileTweetsAdapter.submitList(it)
        })
    }

    private fun setupLocalTweetsRecyclerView() {
        val localProfileAdapter = LocalProfileAdapter(null, listOf(), this)

        rv_profile_local_tweets.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = localProfileAdapter
        }

        viewModel.userData.observe(this, Observer {
            localProfileAdapter.apply {
                user = it
                notifyDataSetChanged()
            }
            setupToolbarData(it)
        })

        viewModel.localHomeTweetsList.observe(this, Observer { localTweets ->
            localTweets?.let {
                localProfileAdapter.apply {
                    tweets = it
                    notifyDataSetChanged()
                }
            }
        })
    }

    private fun initTweetsState() {
        viewModel.stateTweets.observe(this, Observer {
            when (it) {
                State.LOADING -> showLoader()
                State.DONE -> showTweets()
                State.CONNECTION_ERROR -> showConnectionError()
            }
        })
    }

    private fun initUserState() {
        viewModel.stateUserData.observe(this, Observer {
            when (it) {
                State.ERROR -> showError(getString(R.string.error_message_connection))
                State.CONNECTION_ERROR -> showConnectionError()
            }
        })
    }

    private fun refresh() {
        viewModel.getUser(currentUserID)
        viewModel.getTweetsDataSource()?.invalidate()
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
