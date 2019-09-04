package io.github.vnicius.twitterclone.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.utils.summarizeNumber
import twitter4j.Trend

/**
 * Adapter to show the trends
 * @property trends a list of Trend objects
 * @property listener listener to handle the click in the item
 */
class TrendsAdapter(val trends: Array<Trend>, val listener: ItemClickListener<Trend>) :
    RecyclerView.Adapter<TrendsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // inflate the view
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_trend, viewGroup, false)

        // add the view and the listener to the ViewHolder
        return ViewHolder(
            view,
            listener
        )
    }

    override fun getItemCount(): Int {
        return trends.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(trends[position])
    }


    /**
     * Class to bind the object items with the view items
     * @property itemView view with the elements
     * @property listener to handle the clicks
     */
    class ViewHolder(itemView: View, private val listener: ItemClickListener<Trend>) :
        RecyclerView.ViewHolder(itemView) {

        /**
         * Bind the item with the view
         */
        fun bindView(trend: Trend) {

            itemView.setOnClickListener {
                listener.onClick(it, trend)
            }

            val tvTweetsCount = itemView.findViewById<TextView>(R.id.tv_trend_tweets_count)
            itemView.findViewById<TextView>(R.id.tv_trend_position).text =
                (adapterPosition + 1).toString()
            itemView.findViewById<TextView>(R.id.tv_trend_name).text = trend.name

            // check if the trend has the any volume of Tweets
            if (trend.tweetVolume != -1) {
                tvTweetsCount.text = "${trend.tweetVolume.summarizeNumber()} Tweets"
            } else {
                tvTweetsCount.visibility = View.GONE
            }
        }
    }
}