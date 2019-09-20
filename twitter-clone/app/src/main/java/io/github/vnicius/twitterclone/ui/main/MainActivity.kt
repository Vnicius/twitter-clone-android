package io.github.vnicius.twitterclone.ui.main

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.security.ProviderInstaller
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Trend
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.main.adapters.TrendsAdapter
import io.github.vnicius.twitterclone.ui.result.SearchResultActivity
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import io.github.vnicius.twitterclone.ui.trendsinfo.TrendsInfoActivity
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys
import io.github.vnicius.twitterclone.utils.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_connection_error.*
import kotlinx.android.synthetic.main.partial_search_field.*

/**
 * Main Activity View
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

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

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.getLocalTrends().invokeOnCompletion {
            setupTrendsRecyclerView()
            initState()
            viewModel.getTrends()
        }

        updateLocationName()
        initPreferencesListener()
        rl_search_field.setOnClickListener(this)
        btn_connection_error_action.setOnClickListener(this)
        swipe_main_trends_list.apply {
            setOnRefreshListener { refresh() }
            setColorSchemeColors(ContextCompat.getColor(this.context, R.color.blue))
        }
        viewModel.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onResume() {
        super.onResume()
        viewModel.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        viewModel.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            rl_search_field.id -> {
                val intent = Intent(this, SearchableActivity::class.java)

                startActivity(intent)
            }

            btn_connection_error_action.id -> {
                refresh()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(
                    Intent(
                        this,
                        TrendsInfoActivity::class.java
                    )
                )

                overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_fade_out
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLoader() {
        hideContent()
        inc_main_spinner.visibility = View.VISIBLE
    }

    private fun showTrends() {
        hideContent()
        ll_main_trends.visibility = View.VISIBLE
        swipe_main_trends_list.isRefreshing = false
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
        val trendsAdapter = TrendsAdapter(listOf(), object : ItemClickListener<Trend> {
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
            layoutManager =
                LinearLayoutManager(this.context).apply { initialPrefetchItemCount = 10 }
            adapter = trendsAdapter
        }

        viewModel.trends?.observe(this, Observer { trendsData ->
            if (trendsData.isNotEmpty()) {
                trendsAdapter.updateData(trendsData)
                tv_main_trend_title.isFocusableInTouchMode = true
            }
        })
    }

    private fun refresh() {
        viewModel.getTrends()
    }

    private fun initState() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                State.LOADING -> showLoader()
                State.DONE -> showTrends()
                State.ERROR -> showError(getString(R.string.error_message_connection))
                State.CONNECTION_ERROR -> showConnectionErrorMessage()
            }
        })
    }

    private fun initPreferencesListener() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener() { _, key ->
            when (key) {
                SharedPreferencesKeys.LOCATION_NAME -> {
                    updateLocationName()
                    refresh()
                }
                SharedPreferencesKeys.WOEID -> {
                    viewModel.updateLocation()
                }
            }
        }
    }

    private fun updateLocationName() {
        tv_main_trend_title.text =
            getString(R.string.title_trends_location, viewModel.getLocationName())
    }
}
