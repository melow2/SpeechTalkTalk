package com.khs.kakaopay.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

object MainViewItemDiffUtilCallback : DiffUtil.ItemCallback<MainViewItem>() {
    override fun areItemsTheSame(
        oldItem: MainViewItem,
        newItem: MainViewItem
    ): Boolean = when {
        oldItem is MainViewItem.Loading && newItem is MainViewItem.Loading -> true
        oldItem is MainViewItem.Error && newItem is MainViewItem.Error -> true
        oldItem is MainViewItem.Content && newItem is MainViewItem.Content -> oldItem.book.isbn == newItem.book.isbn
        else -> oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MainViewItem,
        newItem: MainViewItem
    ): Boolean = oldItem == newItem

}

class MainAdapter:ListAdapter<MainViewItem,RecyclerView.ViewHolder>(MainViewItemDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}