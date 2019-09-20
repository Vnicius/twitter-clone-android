package io.github.vnicius.twitterclone.ui.trendslocations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Location
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.trendslocations.adapters.LocationsAdapter
import kotlinx.android.synthetic.main.activity_trends_locations.*

class TrendsLocationsActivity : AppCompatActivity() {

    private lateinit var viewModel: TrendsLocationsViewModel
    private lateinit var locationsAdapter: LocationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trends_locations)
        setSupportActionBar(toolbar_trends_locations)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.title_trends_locations_screen)
        }

        viewModel = ViewModelProviders.of(this)[TrendsLocationsViewModel::class.java]
        viewModel.getLocations()

        setupLocationsRecyclerView()
        initObservers()

        ed_trends_locations_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank() and (viewModel.locationsList.value != null)) {
                    updateLocationsList(viewModel.locationsList.value)
                } else {
                    var searchRegex = ".*${s.toString()}.*".toRegex(RegexOption.IGNORE_CASE)
                    updateLocationsList(viewModel.locationsList.value?.filter {
                        searchRegex.matches(it.name)
                    })
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun setupLocationsRecyclerView() {
        locationsAdapter = LocationsAdapter(listOf(), object : ItemClickListener<Location> {
            override fun onClick(view: View, item: Location) {
                viewModel.saveLocation(item.woeid, item.name)
                this@TrendsLocationsActivity.finish()
            }
        })

        rv_trends_locations_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = locationsAdapter
        }
    }

    private fun initObservers() {
        viewModel.locationsList.observe(this, Observer {
            updateLocationsList(it)
        })
    }

    private fun updateLocationsList(locations: List<Location>?) {
        locations?.let {
            locationsAdapter.locations = parseList(locations)
            locationsAdapter.notifyDataSetChanged()
        }
    }

    private fun parseList(locations: List<Location>): List<Location> {
        val mutList = locations.toMutableList().apply {
            sortBy { location -> location.name }
        }
        val worldWideItem: Location? = locations.findLast { l -> l.woeid == 1 }

        if (worldWideItem != null) {
            mutList.remove(worldWideItem)
            mutList.add(0, worldWideItem)
        }

        return mutList.toList()
    }
}
