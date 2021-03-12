package com.khs.kakaopay.ui.main

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.khs.kakaopay.R
import com.khs.kakaopay.databinding.ItemMainRecyclerContentBinding
import com.khs.kakaopay.databinding.ItemMainRecyclerErrorBinding
import com.khs.kakaopay.databinding.ItemMainRecyclerLoadingBinding
import com.attractive.deer.util.data.inflater
import com.attractive.deer.util.data.onlyBind
import java.text.DecimalFormat
import java.text.SimpleDateFormat

object MainViewItemDiffUtilCallback : DiffUtil.ItemCallback<MainViewItem>() {
    override fun areItemsTheSame(
        oldItem: MainViewItem,
        newItem: MainViewItem
    ): Boolean = when {
        oldItem is MainViewItem.Loading && newItem is MainViewItem.Loading -> true
        oldItem is MainViewItem.Error && newItem is MainViewItem.Error -> true
        oldItem is MainViewItem.Content && newItem is MainViewItem.Content -> (oldItem.book.isbn == newItem.book.isbn) && (oldItem.book.title == newItem.book.title)
        else -> oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MainViewItem,
        newItem: MainViewItem
    ): Boolean = oldItem == newItem

}

class MainAdapter(val onClickItem: (MainViewItem.Content, ImageView) -> Unit) :
    ListAdapter<MainViewItem, RecyclerView.ViewHolder>(MainViewItemDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_main_recycler_content -> {
                ContentViewHolder(
                    ItemMainRecyclerContentBinding.inflate(
                        parent.inflater,
                        parent,
                        false
                    )
                )
            }
            R.layout.item_main_recycler_error -> {
                ErrorViewHolder(
                    ItemMainRecyclerErrorBinding.inflate(
                        parent.inflater,
                        parent,
                        false
                    )
                )
            }
            R.layout.item_main_recycler_loading -> {
                LoadingViewHolder(
                    ItemMainRecyclerLoadingBinding.inflate(
                        parent.inflater,
                        parent,
                        false
                    )
                )
            }
            else -> error("Not exist viewholder..")
        }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainViewItem.Loading -> R.layout.item_main_recycler_loading
            is MainViewItem.Error -> R.layout.item_main_recycler_error
            is MainViewItem.Content -> R.layout.item_main_recycler_content
        }
    }

    override fun getItemId(position: Int): Long {
        return when (val item = getItem(position)) {
            is MainViewItem.Loading -> item.toString().hashCode().toLong()
            is MainViewItem.Error -> item.toString().hashCode().toLong()
            is MainViewItem.Content -> item.book.isbn.hashCode().toLong()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder) holder.bind(getItem(position))
    }

    inner class ContentViewHolder(private val binding: ItemMainRecyclerContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: MainViewItem) {
            onlyBind<MainViewItem.Content, ItemMainRecyclerContentBinding>(
                book,
                binding
            ) { item ->
                binding.run {
                    tvBookName.text = item.book.title
                    tvPrice.text = PRICE_FORM.format(item.book.price)+"원"
                    tvDescription.text = item.book.contents
                    if (!item.book.datetime.isNullOrEmpty()) {
                        val date = ORIGIN_DATE_FORM.parse(item.book.datetime)
                        tvDateTime.text = PARSE_DATE_FORM.format(date)
                    }
                    isLike = item.book.like
                    Glide.with(binding.root.context)
                        .load(item.book.thumbnail)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(RequestOptions.placeholderOf(R.color.color_eaeaea))
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(ivBookImage)
                }
                itemView.setOnClickListener {
                    onClickItem(item, ivBookImage)
                }
            }
        }
    }

    inner class ErrorViewHolder(binding: ItemMainRecyclerErrorBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LoadingViewHolder(binding: ItemMainRecyclerLoadingBinding) : RecyclerView.ViewHolder(binding.root)
    companion object {
        val PRICE_FORM = DecimalFormat("#,##0");
        val ORIGIN_DATE_FORM = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val PARSE_DATE_FORM = SimpleDateFormat("yyyy.MM.dd")
    }

}