package io.github.vnicius.twitterclone.ui.common.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.vnicius.twitterclone.R

private const val ARG_QUERY = "query"

/**
 * [Fragment] to show the message that none result was founded
 */
class NoResultFragment : Fragment() {

    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get the query that has no result
        arguments?.let {
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_result, container, false)
        view.findViewById<TextView>(R.id.tv_no_result_message).text = "No results for \"$query\""

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String) =
            NoResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }
}
