package io.github.vnicius.twitterclone.ui.trendsinfo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.trendslocations.TrendsLocationsActivity
import io.github.vnicius.twitterclone.utils.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_trends_info.*

class TrendsInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: TrendsInfoViewModel

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

        ll_trends_info_trend_location.setOnClickListener {
            startActivity(Intent(this, TrendsLocationsActivity::class.java))

            overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_fade_out
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setLocationName()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }

    private fun setLocationName() {
        tv_trend_info_location_name.text = viewModel.getLocationName() ?: ""
    }
}