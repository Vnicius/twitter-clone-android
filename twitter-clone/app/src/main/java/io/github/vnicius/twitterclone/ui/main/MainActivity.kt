package io.github.vnicius.twitterclone.ui.main

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.main.adapters.TrendsAdapter
import io.github.vnicius.twitterclone.ui.result.SearchResultActivity
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_connection_error.*
import kotlinx.android.synthetic.main.partial_search_field.*
import twitter4j.Trend

/**
 * Main Activity View
 */
class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {

    private val presenter: MainContract.Presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter.getTrends()

        rl_search_field.setOnClickListener(this)
        btn_connection_error_action.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            rl_search_field.id -> {
                val intent = Intent(this, SearchableActivity::class.java)

                startActivity(intent)
            }

            btn_connection_error_action.id -> {
                presenter.getTrends()
            }
        }
    }

    override fun showLoader() {
        hideContent()
        inc_main_spinner.visibility = View.VISIBLE
    }

    override fun showTrends(trends: Array<Trend>) {
        hideContent()
        ll_main_trends.visibility = View.VISIBLE

        rv_main_trends_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = TrendsAdapter(trends, object : ItemClickListener<Trend> {
                override fun onClick(view: View, item: Trend) {
                    // make the search with the trend name
                    val intent = Intent(view.context, SearchResultActivity::class.java).apply {
                        action = Intent.ACTION_SEARCH
                        putExtra(SearchManager.QUERY, item.name)
                    }

                    startActivity(intent)

                    overridePendingTransition(
                        R.anim.anim_slide_in_left,
                        R.anim.anim_fade_out
                    )
                }
            })
        }
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun showConnectionErrorMessage() {
        hideContent()
        inc_main_connection_error.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    private fun hideContent() {
        inc_main_spinner.visibility = View.GONE
        ll_main_trends.visibility = View.GONE
        inc_main_connection_error.visibility = View.GONE
    }
}
