package com.darkndev.selectiontrackerusage

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.darkndev.selectiontrackerusage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private var actionMode: ActionMode? = null
    private lateinit var tracker: SelectionTracker<Long>
    private val itemAdapter = ItemAdapter(this)

    companion object {
        const val ITEM_SELECT = "com.darkndev.selectiontrackerusage.ITEM_SELECT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolbar)

            recyclerView.apply {
                adapter = itemAdapter
            }

            repopulateItems.setOnClickListener {
                tracker.clearSelection()
                itemAdapter.submitList(repopulate())
            }

            tracker = SelectionTracker.Builder(
                ITEM_SELECT,
                recyclerView,
                SelectionKeyProvider(recyclerView),
                SelectionDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
            ).build()

            itemAdapter.setSelectionTracker(tracker)

            tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    super.onItemStateChanged(key, selected)
                    if (actionMode == null && tracker.hasSelection()) {
                        actionMode = startActionMode(callback)
                    } else if (actionMode != null && tracker.selection.isEmpty) {
                        actionMode?.finish()
                    }
                    actionMode?.title = tracker.selection.size().toString()
                }
            })
        }
        itemAdapter.submitList(repopulate())
    }

    private fun repopulate() = listOf(
        Item(1, "Title 1", "Content 1"),
        Item(2, "Title 2", "Content 2"),
        Item(3, "Title 3", "Content 3"),
        Item(4, "Title 4", "Content 4"),
        Item(5, "Title 5", "Content 5"),
        Item(6, "Title 6", "Content 6"),
        Item(7, "Title 7", "Content 7")
    )

    override fun onItemClick(item: Item) {
        Toast.makeText(this, "Item Clicked", Toast.LENGTH_SHORT).show()
    }

    private val callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) =
            when (item?.itemId) {
                R.id.action_select_all -> {
                    itemAdapter.currentList.forEach {
                        tracker.select(it.id)
                    }
                    true
                }

                R.id.action_delete -> {
                    val items = itemAdapter.currentList.filterNot {
                        tracker.selection.contains(it.id)
                    }
                    itemAdapter.submitList(items)
                    mode?.finish()
                    true
                }

                else -> false
            }

        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.clearSelection()
            actionMode = null
        }
    }
}