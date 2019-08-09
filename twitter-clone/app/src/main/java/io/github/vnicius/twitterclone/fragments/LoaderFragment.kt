package io.github.vnicius.twitterclone.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vnicius.twitterclone.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Loader.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Loader.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoaderFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loader, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoaderFragment()
    }
}
