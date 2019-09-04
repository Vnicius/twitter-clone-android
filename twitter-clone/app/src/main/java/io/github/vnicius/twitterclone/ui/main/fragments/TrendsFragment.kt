package io.github.vnicius.twitterclone.ui.main.fragments

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.main.adapters.TrendsAdapter
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.result.SearchResultActivity
import twitter4j.Trend

/**
 * [Fragment] to show the trends
 */
class TrendsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trends, container, false)

        // get the list of trends by the arguments
        val bundle = arguments
        val trends: Array<Trend> = bundle?.getSerializable(ARG_CODE) as Array<Trend>
        val rv = view.findViewById<RecyclerView>(R.id.rv_trends_list)

        // inflate the RecyclerView
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = TrendsAdapter(trends, object :
            ItemClickListener<Trend> {
            override fun onClick(view: View, item: Trend) {
                // make the search with the trend name
                val intent = Intent(view.context, SearchResultActivity::class.java).apply {
                    action = Intent.ACTION_SEARCH
                    putExtra(SearchManager.QUERY, item.name)
                }
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_left_in, R.anim.fade_out)
            }
        })

        return view
    }

    companion object {
        const val ARG_CODE = "TRENDS"
        @JvmStatic
        fun newInstance() =
            TrendsFragment()
    }
}
