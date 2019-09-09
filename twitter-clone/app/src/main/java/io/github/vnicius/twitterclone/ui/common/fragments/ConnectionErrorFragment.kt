package io.github.vnicius.twitterclone.ui.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.github.vnicius.twitterclone.R

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("ValidFragment")
class ConnectionErrorFragment(private val tryAgainHandler: (() -> Unit)) : Fragment(),
    View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connection_error, container, false)

        view.findViewById<Button>(R.id.btn_connection_error_action).setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_connection_error_action) {
            tryAgainHandler()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(tryAgainHandler: (() -> Unit)) =
            ConnectionErrorFragment(tryAgainHandler)
    }
}
