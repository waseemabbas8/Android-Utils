package com.softsolutions.bookryt.ui.common.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softsolutions.bookryt.model.common.RequestState
import com.softsolutions.bookryt.ui.GenericRecyclerViewAdapter
import com.softsolutions.bookryt.ui.OnListItemClickListener

const val VIEW_TYPE_PLACEHOLDER = 6982
const val VIEW_TYPE_DATA_ITEM = 6977

abstract class PaginationListAdapter<T>(
    private val itemLayout: Int,
    private val placeholderLayout: Int,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    private var itemClickListener: OnListItemClickListener<T>? = null
    private var state: RequestState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA_ITEM -> {
                val itemBinding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), itemLayout, parent, false
                )

                GenericRecyclerViewAdapter.GenericViewHolder(itemBinding)
            }

            VIEW_TYPE_PLACEHOLDER -> PlaceHolderVH(
                LayoutInflater.from(parent.context)
                    .inflate(placeholderLayout, parent, false)
            )

            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            VIEW_TYPE_DATA_ITEM -> {
                holder as GenericRecyclerViewAdapter.GenericViewHolder
                val obj = getItem(position)
                holder.bind(obj)
                holder.binding.root.setOnClickListener {
                    itemClickListener?.onItemClick(obj!!, position)
                }
            }

            else -> (holder as PlaceHolderVH).bind()

        }

    }

    override fun getItemViewType(position: Int): Int =
        if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_PLACEHOLDER
        } else {
            VIEW_TYPE_DATA_ITEM
        }

    private fun hasExtraRow() = state != null && state != RequestState.DONE

    fun setState(newState: RequestState) {
        val previousState = this.state
        val hadExtraRow = hasExtraRow()
        this.state = newState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun setItemClickListener(listener: OnListItemClickListener<T>?) {
        this.itemClickListener = listener
    }

}

class PlaceHolderVH(view: View) : RecyclerView.ViewHolder(view) {
    fun bind() {}
}