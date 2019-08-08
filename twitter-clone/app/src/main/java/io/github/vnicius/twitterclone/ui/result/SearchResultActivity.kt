package io.github.vnicius.twitterclone.ui.result

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.MenuItem
import android.view.View
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.fragments.TweetsFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.searchfield.*
import twitter4j.Status
import java.io.Serializable

class SearchResultActivity : AppCompatActivity(), SearchResultContract.View, View.OnClickListener {

    val mPresenter: SearchResultContract.Presenter = SearchResultPresenter(this)
    private lateinit var mTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setSupportActionBar(toolbar_search_result)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        handleIntent(intent)
        search_item.setOnClickListener(this)
        mTransaction = supportFragmentManager.beginTransaction()
    }

    private fun chageFragment(fragment: Fragment){
        mTransaction.add(frame_search_result.id, fragment)
        mTransaction.commit()
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                mPresenter.searchTweets(query)
                tv_search_text.text = query
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            search_item.id -> {
                val intent = Intent(this, SearchableActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showResult(tweets: MutableList<Status>) {
        val fragment = TweetsFragment()
        val args = Bundle()
        args.putSerializable("tweets", tweets as Serializable)
        fragment.arguments = args

        chageFragment(fragment)
    }

    override fun showNoResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
