package io.github.vnicius.twitterclone.ui.result

import android.app.SearchManager
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.result.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.ui.profile.ProfileActivity
import io.github.vnicius.twitterclone.ui.result.adapters.LocalTweetsAdapter
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import io.github.vnicius.twitterclone.utils.State
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.partial_connection_error.*
import kotlinx.android.synthetic.main.partial_search_field.*
import twitter4j.Status

/**
 * SearchResult Activity View
 */
class SearchResultActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener<Status> {

    private lateinit var viewModel: SearchResultViewModel
    private lateinit var query: String
    private lateinit var tweetsAdapter: TweetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setSupportActionBar(toolbar_search_result)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        handleIntent(intent)

        rl_search_field.setOnClickListener(this)
        btn_connection_error_action.setOnClickListener(this)
        swipe_search_result_tweets.apply {
            setOnRefreshListener { refresh() }
            setColorSchemeColors(ContextCompat.getColor(this.context, R.color.blue))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        viewModel = ViewModelProviders.of(this)[SearchResultViewModel::class.java]

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                viewModel.build(it)
                tv_search_field_search_label.text = it
                query = it
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_fade_out)
            }
            setupLocalTweetsRecyclerView()
            initState()
            setupTweetsRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_result, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            rl_search_field.id -> {
                val intent = Intent(this, SearchableActivity::class.java).apply {
                    putExtra(SearchableActivity.QUERY, query)
                }

                startActivity(intent)
            }

            btn_connection_error_action.id -> {
                refresh()
            }
        }
    }

    override fun onClick(view: View, item: Status) {
        val intent = Intent(view.context, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.USER_ID, item.user.id)

        startActivity(intent)

        overridePendingTransition(
            R.anim.anim_slide_in_left,
            R.anim.anim_fade_out
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun showLoader() {
        hideContent()

        viewModel.localTweetsList.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                inc_search_result_spinner.visibility = View.VISIBLE
            } else {
                rv_search_result_local_tweets.visibility = View.VISIBLE
            }
        })
    }

    private fun showResult() {
        hideContent()
        rv_search_result_tweets_list.visibility = View.VISIBLE
        swipe_search_result_tweets.isRefreshing = false
    }

    private fun showNoResult() {
        hideContent()
        tv_search_result_no_result_message.visibility = View.VISIBLE
        tv_search_result_no_result_message.text = getString(R.string.error_message_no_result, query)
    }

    private fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    private fun showConnectionErrorMessage() {
        hideContent()
        inc_search_result_connection_error.visibility = View.VISIBLE
    }

    private fun hideContent() {
        inc_search_result_connection_error.visibility = View.GONE
        inc_search_result_spinner.visibility = View.GONE
        rv_search_result_tweets_list.visibility = View.GONE
        rv_search_result_local_tweets.visibility = View.GONE
        tv_search_result_no_result_message.visibility = View.GONE
    }

    private fun setupTweetsRecyclerView() {
        tweetsAdapter = TweetsAdapter(this)

        rv_search_result_tweets_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = tweetsAdapter
        }

        viewModel.tweetsList.observe(this, Observer {
            tweetsAdapter.submitList(it)
        })
    }

    private fun setupLocalTweetsRecyclerView() {
        val localTweetsAdapter = LocalTweetsAdapter(listOf(), this)

        rv_search_result_local_tweets.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = localTweetsAdapter
        }

        viewModel.localTweetsList.observe(this, Observer { tweetsList ->
            tweetsList?.let {
                localTweetsAdapter.apply {
                    tweets = tweetsList
                    notifyDataSetChanged()
                }
            }
        })
    }

    private fun initState() {
        viewModel.state.observe(this, Observer {
            when (it) {
                State.NO_RESULT -> showNoResult()
                State.ERROR -> showError(getString(R.string.error_message_connection))
                State.DONE -> showResult()
                State.LOADING -> showLoader()
                State.CONNECTION_ERROR -> showConnectionErrorMessage()
            }
        })
    }

    private fun refresh() {
        viewModel.getDataSourceValue()?.invalidate()
    }
}
