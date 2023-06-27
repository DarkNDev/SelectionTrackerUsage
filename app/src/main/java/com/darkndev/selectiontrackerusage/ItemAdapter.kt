package com.darkndev.selectiontrackerusage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.darkndev.selectiontrackerusage.databinding.LayoutItemBinding

class ItemAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffUtil) {

    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    fun setSelectionTracker(tracker: SelectionTracker<Long>) {
        this.tracker = tracker
    }

    object ItemDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bind(item)
        tracker?.let {
            holder.binding.card.isChecked =
                tracker!!.isSelected(getItemId(position))
        }
    }

    override fun getItemId(position: Int) = getItem(position).id

    inner class ItemViewHolder(val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(bindingAdapterPosition)
                    if (item != null)
                        listener.onItemClick(item)
                }
            }
        }

        fun bind(item: Item) {
            binding.apply {
                title.text = item.title
                content.text = item.content
            }
        }
    }
}