package io.github.vnicius.twitterclone.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.View
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.fragments.LoaderFragment
import io.github.vnicius.twitterclone.fragments.TrendsFragment
import io.github.vnicius.twitterclone.fragments.TweetsFragment
import io.github.vnicius.twitterclone.ui.searchable.SearchableActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.searchfield.*
import twitter4j.Status
import twitter4j.Trend
import java.io.Serializable

class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {
    private val mPresenter = MainPresenter(this)
    private lateinit var mTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mPresenter.getTrends()

        search_item.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            search_item.id -> {
                val intent = Intent(this, SearchableActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun changeFragment(fragment: Fragment){
        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(frame_main.id, fragment)
        mTransaction.commitAllowingStateLoss()
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }

    override fun showTrends(trends: Array<Trend>) {
        val fragment = TrendsFragment.newInstance()
        val args = Bundle()
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

}
