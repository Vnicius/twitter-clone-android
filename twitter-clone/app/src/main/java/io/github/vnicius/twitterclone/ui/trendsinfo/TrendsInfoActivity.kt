package io.github.vnicius.twitterclone.ui.trendsinfo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_trends_info.*

class TrendsInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: TrendsInfoViewModel
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trends_info)
        setSupportActionBar(toolbar_trends_info)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.title_trends_infos)
        }

        viewModel = ViewModelProviders.of(this)[TrendsInfoViewModel::class.java]

        setLocationName()
        initObserver()

        ll_trends_info_trend_location.setOnClickListener {

        }
        viewModel.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        viewModel.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun setLocationName() {
        tv_trend_info_location_name.text = viewModel.getLocationName() ?: ""
    }

    private fun initObserver() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                SharedPreferencesKeys.LOCATION_NAME -> setLocationName()
            }
        }
    }
}
