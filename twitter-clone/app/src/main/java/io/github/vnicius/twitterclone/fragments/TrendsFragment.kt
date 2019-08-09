package io.github.vnicius.twitterclone.fragments

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.adapters.TrendsAdapter
import io.github.vnicius.twitterclone.adapters.click.AdapterClickHandler
import io.github.vnicius.twitterclone.ui.result.SearchResultActivity
import twitter4j.Trend

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TrendsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TrendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */


class TrendsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trends, container, false)
        val bundle = arguments
        val trends: Array<Trend> = bundle?.getSerializable(ARG_CODE) as Array<Trend>
        val rv = view.findViewById<RecyclerView>(R.id.rv_trends)

        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = TrendsAdapter(trends, object: AdapterClickHandler<Trend> {
            override fun onClick(view: View, item: Trend) {
                val intent = Intent(view.context, SearchResultActivity::class.java).apply {
                    action = Intent.ACTION_SEARCH
                    putExtra(SearchManager.QUERY, item.name)
                }
                startActivity(intent)
            }
        })

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrendsFragment.
         */
        const val ARG_CODE = "TRENDS"
        @JvmStatic
        fun newInstance() =
            TrendsFragment()
    }
}
