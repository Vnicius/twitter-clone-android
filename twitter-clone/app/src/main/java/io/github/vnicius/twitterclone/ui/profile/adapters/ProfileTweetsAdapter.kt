package io.github.vnicius.twitterclone.ui.profile.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import io.github.vnicius.twitterclone.data.model.User
import io.github.vnicius.twitterclone.data.model.UserStatus
import io.github.vnicius.twitterclone.ui.common.adapters.ItemClickListener
import io.github.vnicius.twitterclone.ui.profile.viewholder.ProfileContentViewHolder
import io.github.vnicius.twitterclone.ui.profile.viewholder.TweetViewHolder

class ProfileTweetsAdapter(
    var user: User?,
    private val listener: ItemClickListener<UserStatus>
) : PagedListAdapter<UserStatus, RecyclerView.ViewHolder>(tweetsDiffCallback) {

    private val adapterCallback = AdapterListUpdateCallback(this)

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapterCallback.onChanged(position + 1, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
        }

        override fun onInserted(position: Int, count: Int) {
            adapterCallback.onInserted(position + 1, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapterCallback.onRemoved(position + 1, count)
        }
    }

    private val differ = AsyncPagedListDiffer<UserStatus>(
        listUpdateCallback, AsyncDifferConfig.Builder<UserStatus>(
            tweetsDiffCallback
        ).build()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ItemType.HEADER.ordinal) ProfileContentViewHolder.create(parent) else TweetViewHolder.create(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, index: Int) {
        if (getItemViewType(index) == ItemType.HEADER.ordinal) {
            user?.let { (vh as ProfileContentViewHolder).bindView(it) }
        } else {
            getItem(index)?.let { (vh as TweetViewHolder).bindView(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ItemType.HEADER.ordinal else ItemType.STATUS.ordinal
    }

    override fun getItemCount(): Int {
        return differ.itemCount + 1
    }

    override fun getItem(position: Int): UserStatus? {
        return differ.getItem(position - 1)
    }

    override fun submitList(pagedList: PagedList<UserStatus>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<UserStatus>? {
        return differ.currentList
    }

    companion object {
        val tweetsDiffCallback = object : DiffUtil.ItemCallback<UserStatus>() {
            override fun areItemsTheSame(oldItem: UserStatus, newItem: UserStatus): Boolean =
                oldItem.status.statusId == newItem.status.statusId

            override fun areContentsTheSame(oldItem: UserStatus, newItem: UserStatus): Boolean =
                (oldItem.status.retweetCount == newItem.status.retweetCount) && (oldItem.status.favoriteCount == newItem.status.favoriteCount)
        }

        enum class ItemType { HEADER, STATUS }
    }
}