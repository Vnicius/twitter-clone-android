package io.github.vnicius.twitterclone.ui.main

import android.app.SearchManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.security.ProviderInstaller
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
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var trendsAdapter: TrendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
            && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT
        ) {
            ProviderInstaller.installIfNeeded(baseContext)
        }

        setupTrendsRecyclerView()

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java].apply {
            trends.observe(this@MainActivity, Observer {
                showTrends(it)
            })
        }

        viewModel.getTrends()

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
                viewModel.getTrends()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun showLoader() {
        hideContent()
        inc_main_spinner.visibility = View.VISIBLE
    }

    private fun showTrends(trends: Array<Trend>) {

        trendsAdapter.apply {
            this.trends = trends
            notifyDataSetChanged()
        }

        if (trends.isNotEmpty()) {
            hideContent()
            ll_main_trends.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    private fun showConnectionErrorMessage() {
        hideContent()
        inc_main_connection_error.visibility = View.VISIBLE
    }

    private fun hideContent() {
        inc_main_spinner.visibility = View.GONE
        ll_main_trends.visibility = View.GONE
        inc_main_connection_error.visibility = View.GONE
    }

    private fun setupTrendsRecyclerView() {
        trendsAdapter = TrendsAdapter(arrayOf(), object : ItemClickListener<Trend> {
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

        rv_main_trends_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = trendsAdapter
        }
    }
}
