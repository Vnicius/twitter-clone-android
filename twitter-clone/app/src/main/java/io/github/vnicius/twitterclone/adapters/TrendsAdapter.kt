package io.github.vnicius.twitterclone.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.adapters.click.AdapterClickHandler
import io.github.vnicius.twitterclone.utils.ParseUtils
import twitter4j.Trend

class TrendsAdapter(val trends: Array<Trend>, val listener: AdapterClickHandler<Trend>): RecyclerView.Adapter<TrendsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.trend_item, viewGroup, false)
        return ViewHolder(view, object: OnClickTrendListener{
            override fun onClick(view: View, position: Int) {
                listener.onClick(view, trends[position])
            }
        })
    }

    override fun getItemCount(): Int {
        return trends.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBindView(trends[position])
    }

    interface OnClickTrendListener{
        fun onClick(view: View, position: Int)
    }

    class ViewHolder(itemView: View, private val listener: OnClickTrendListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
           listener.onClick(view, adapterPosition)
        }

        fun onBindView(trend: Trend) {
            val tvTweetsCount = itemView.findViewById<TextView>(R.id.tv_trend_tweets_count)
            itemView.findViewById<TextView>(R.id.tv_trend_position).text = (adapterPosition + 1).toString()
            itemView.findViewById<TextView>(R.id.tv_trend_name).text = trend.name

            if(trend.tweetVolume != -1) {
                tvTweetsCount.text = "${ParseUtils.parseNumber(trend.tweetVolume)} Tweets"
            } else {
                tvTweetsCount.visibility = View.GONE
            }
        }
    }
}