package io.github.vnicius.twitterclone.ui.trendsinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import io.github.vnicius.twitterclone.R
import kotlinx.android.synthetic.main.activity_trends_info.*

class TrendsInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trends_info)
        setSupportActionBar(toolbar_trends_info)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.title_trends_infos)
        }

        ll_trends_info_trend_location.setOnClickListener {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_slide_out_right)
    }
}
