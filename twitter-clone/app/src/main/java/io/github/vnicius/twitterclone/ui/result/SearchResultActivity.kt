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
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.common.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.ui.profile.ProfileActivity
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import io.github.vnicius.twitterclone.utils.State
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.partial_connection_error.*
import kotlinx.android.synthetic.main.partial_search_field.*
import twitter4j.Status

/**
 * SearchResult Activity View
 */
class SearchResultActivity : AppCompatActivity(), SearchResultContract.View, View.OnClickListener {

    private val presenter: SearchResultContract.Presenter = SearchResultPresenter()
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
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                presenter.build(it)
                tv_search_field_search_label.text = it
                query = it
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_fade_out)
            }
            initState()
            setupTweetsRecyclerView()
        }
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
                presenter.getDataSourceValue()?.invalidate()
            }
        }
    }

    override fun showLoader() {
        hideContent()
        inc_search_result_spinner.visibility = View.VISIBLE
    }

    override fun showResult() {
        hideContent()
        rv_search_result_tweets_list.visibility = View.VISIBLE
    }

    override fun showNoResult() {
        hideContent()
        tv_search_result_no_result_message.visibility = View.VISIBLE
        tv_search_result_no_result_message.text = getString(R.string.error_message_no_result, query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_result, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showConnectionErrorMessage() {
        hideContent()
        inc_search_result_connection_error.visibility = View.VISIBLE
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun hideContent() {
        inc_search_result_connection_error.visibility = View.GONE
        inc_search_result_spinner.visibility = View.GONE
        rv_search_result_tweets_list.visibility = View.GONE
        tv_search_result_no_result_message.visibility = View.GONE
    }

    private fun setupTweetsRecyclerView() {
        tweetsAdapter = TweetsAdapter(object : ItemClickListener<Status> {
            override fun onClick(view: View, item: Status) {
                val intent = Intent(view.context, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER_ID, item.user.id)

                startActivity(intent)

                overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_fade_out
                )
            }
        })

        rv_search_result_tweets_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = tweetsAdapter
        }

        presenter.getValue().observe(this, Observer {
            tweetsAdapter.submitList(it)
        })
    }

    private fun initState() {
        presenter.getState().observe(this, Observer {
            when (it) {
                State.NO_RESULT -> showNoResult()
                State.ERROR -> showConnectionErrorMessage()
                State.DONE -> showResult()
                State.LOADING -> showLoader()
            }
        })
    }
}
