package com.darkndev.selectiontrackerusage

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class SelectionKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<Long>(SCOPE_MAPPED) {

    private lateinit var holder: RecyclerView.ViewHolder

    override fun getKey(position: Int) = holder.itemId

    override fun getPosition(key: Long): Int {
        holder = recyclerView.findViewHolderForItemId(key)
        return holder.bindingAdapterPosition
    }
}