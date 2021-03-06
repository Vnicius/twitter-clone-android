package io.github.vnicius.twitterclone.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.fragments.ConnectionErrorFragment
import io.github.vnicius.twitterclone.ui.common.fragments.LoaderFragment
import io.github.vnicius.twitterclone.ui.main.fragments.TrendsFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_search_field.*
import twitter4j.Trend
import java.io.Serializable

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
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            rl_search_field.id -> {
                val intent = Intent(this, SearchableActivity::class.java)

                startActivity(intent)
            }
        }
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }

    override fun showTrends(trends: Array<Trend>) {
        val fragment = TrendsFragment.newInstance()
        val args = Bundle()

        // pass the list of trends to the Trend Fragment by argument
        args.putSerializable(TrendsFragment.ARG_CODE, trends as Serializable)
        fragment.arguments = args

        changeFragment(fragment)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun showConnectionErrorMessage() {
        changeFragment(ConnectionErrorFragment.newInstance {
            presenter.getTrends()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    /**
     * Change the fragment in the view
     * @param fragment
     */
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(fl_main_fragment_layout.id, fragment)
            commitAllowingStateLoss()
        }
    }
}
