package io.github.vnicius.twitterclone.ui.result

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.fragments.LoaderFragment
import io.github.vnicius.twitterclone.ui.common.fragments.NoResultFragment
import io.github.vnicius.twitterclone.ui.common.fragments.TweetsListFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.partial_search_field.*
import twitter4j.Status
import java.io.Serializable

/**
 * SearchResult Activity View
 */
class SearchResultActivity : AppCompatActivity(), SearchResultContract.View, View.OnClickListener {

    private val presenter: SearchResultContract.Presenter = SearchResultPresenter(this)
    private lateinit var query: String

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
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                presenter.searchTweets(it)
                tv_search_field_search_label.text = it
                query = it
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_fade_out)
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == rl_search_field.id) {
            val intent = Intent(this, SearchableActivity::class.java).apply {
                putExtra(SearchableActivity.QUERY, query)
            }

            startActivity(intent)
        }
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }

    override fun showResult(tweets: MutableList<Status>) {
        val fragment = TweetsListFragment.newInstance()
        val args = Bundle()

        // pass the list of tweets to the Tweets List Fragment by argument
        args.putSerializable(TweetsListFragment.ARG_CODE, tweets as Serializable)
        fragment.arguments = args

        changeFragment(fragment)
    }

    override fun showNoResult() {
        changeFragment(NoResultFragment.newInstance(query))
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    /**
     * Change the fragment in the view
     * @param fragment
     */
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(fl_search_result_fragment_layout.id, fragment)
            commitAllowingStateLoss()
        }
    }
}
