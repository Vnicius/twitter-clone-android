package io.github.vnicius.twitterclone.ui.searchable

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView

import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.result.SearchResultActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(baseContext, SearchResultActivity::class.java)

        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            requestFocus()
            maxWidth = Int.MAX_VALUE
            isIconified = false
            queryHint = "Search Twitter"
            findViewById<View>(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            findViewById<View>(android.support.v7.appcompat.R.id.search_mag_icon).visibility = View.GONE
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
