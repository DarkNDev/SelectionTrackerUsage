package com.darkndev.selectiontrackerusage

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class SelectionDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    private lateinit var holder: RecyclerView.ViewHolder

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val v = recyclerView.findChildViewUnder(e.x, e.y)
        if (v != null) {
            holder = recyclerView.getChildViewHolder(v)
            return SelectionDetails()
        }
        return null
    }

    inner class SelectionDetails : ItemDetails<Long>() {
        override fun getPosition() = holder.bindingAdapterPosition

        override fun getSelectionKey() = holder.itemId

    }
}