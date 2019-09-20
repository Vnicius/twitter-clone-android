package io.github.vnicius.twitterclone.ui.trendslocations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.vnicius.twitterclone.R
import io.github.vnicius.twitterclone.data.model.Location
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener

class LocationsAdapter(var locations: List<Location>, val listener: ItemClickListener<Location>) :
    RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)

        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(locations[position])
    }

    class ViewHolder(itemView: View, private val listener: ItemClickListener<Location>) :
        RecyclerView.ViewHolder(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tv_item_location_name)

        fun bindView(location: Location) {
            itemView.setOnClickListener {
                listener.onClick(it, location)
            }

            tvName.text =
                if (location.name != location.countryName && location.countryName != "")
                    "${location.name}, ${location.countryName}"
                else
                    location.name
        }
    }
}