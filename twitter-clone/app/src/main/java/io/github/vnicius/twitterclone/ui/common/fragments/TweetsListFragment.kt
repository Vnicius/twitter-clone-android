package io.github.vnicius.twitterclone.ui.common.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.TweetsAdapter
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.profile.ProfileActivity
import twitter4j.Status

/**
 * [Fragment] to show a list of tweets
 */
class TweetsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tweets_list, container, false)
        val bundle = arguments
        val tweets: MutableList<Status> = bundle?.getSerializable(ARG_CODE) as MutableList<Status>
        val rv = view.findViewById<RecyclerView>(R.id.rv_tweets_list)

        // inflate the RecyclerView
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter =
            TweetsAdapter(tweets, object :
                ItemClickListener<Status> {
                override fun onClick(view: View, item: Status) {
                    // open a new intent with the user profile
                    val intent = Intent(view.context, ProfileActivity::class.java)
                    intent.putExtra(ProfileActivity.USER_ID, item.user.id)

                    startActivity(intent)

                    activity!!.overridePendingTransition(
                        R.anim.anim_slide_in_left,
                        R.anim.anim_fade_out
                    )
                }
            })

        return view
    }

    companion object {
        const val ARG_CODE = "TWEETS"
        @JvmStatic
        fun newInstance() =
            TweetsListFragment()
    }
}
