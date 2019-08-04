package io.github.vnicius.twitterclone.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.vnicius.twitterclone.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_tb)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
