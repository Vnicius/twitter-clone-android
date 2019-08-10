package io.github.vnicius.twitterclone.ui.result

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.fragments.LoaderFragment
import io.github.vnicius.twitterclone.fragments.NoResultFragment
import io.github.vnicius.twitterclone.fragments.TweetsFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.searchfield.*
import twitter4j.Status
import java.io.Serializable

/**
 * SearchResult Activity View
 */
class SearchResultActivity : AppCompatActivity(), SearchResultContract.View, View.OnClickListener {

    // presenter interface
    private val mPresenter: SearchResultContract.Presenter = SearchResultPresenter(this)
    private lateinit var mTransaction: FragmentTransaction
    private lateinit var mQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setSupportActionBar(toolbar_search_result)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // handle the intent call
        handleIntent(intent)

        // set the item search click
        search_item.setOnClickListener(this)
    }

    /**
     * Change the fragment in the view
     * @param fragment
     */
    private fun changeFragment(fragment: Fragment){
        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(frame_search_result.id, fragment)
        mTransaction.commitAllowingStateLoss()
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            // get the query value
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                mPresenter.searchTweets(query)
                tv_search_text.text = query
                mQuery = query
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            search_item.id -> {
                val intent = Intent(this, SearchableActivity::class.java).apply {
                    putExtra(SearchableActivity.QUERY, mQuery)
                }
                startActivity(intent)
            }
        }
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }

    override fun showResult(tweets: MutableList<Status>) {
        val fragment = TweetsFragment.newInstance()
        val args = Bundle()

        // pass the list of trends to the Trend Fragment by argument
        args.putSerializable(TweetsFragment.ARG_CODE, tweets as Serializable)
        fragment.arguments = args

        changeFragment(fragment)
    }

    override fun showNoResult() {
        changeFragment(NoResultFragment.newInstance(mQuery))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_result, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }
}
