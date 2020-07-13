package com.popsa.android.util.databinding.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.databinding.adapter.DataBindingAdapter

@SuppressLint("DiffUtilEquals")
class SimpleBindingAdapter(
    sameItem: ((old: Any, new: Any) -> Boolean)? = null,
    sameContent: ((old: Any, new: Any) -> Boolean)? = null
) : DataBindingAdapter(SimpleDiff(sameItem, sameContent)) {

    private class SimpleDiff(
        val sameItem: ((old: Any, new: Any) -> Boolean)?,
        val sameContent: ((old: Any, new: Any) -> Boolean)?
    ) : DiffUtil.ItemCallback<BindingAdapterItem>() {

        override fun areItemsTheSame(
            oldItem: BindingAdapterItem,
            newItem: BindingAdapterItem
        ): Boolean =
            sameItem?.invoke(oldItem, newItem) ?: (oldItem.model ?: oldItem) == (newItem.model ?: newItem)

        override fun areContentsTheSame(
            oldItem: BindingAdapterItem,
            newItem: BindingAdapterItem
        ): Boolean =
            sameContent?.invoke(oldItem, newItem) ?: (oldItem.model ?: oldItem) == (newItem.model ?: newItem)
    }

}

//package rocks.wintren.rency.util.databinding.adapter
//
//import android.annotation.SuppressLint
//import androidx.recyclerview.widget.DiffUtil
//import timber.log.Timber
//
//@SuppressLint("DiffUtilEquals")
//class SimpleBindingAdapter(
//    sameItem: ((old: BindingAdapterItem, new: BindingAdapterItem) -> Boolean)? = null,
//    sameContent: ((old: BindingAdapterItem, new: BindingAdapterItem) -> Boolean)? = null
//) : DataBindingAdapter(SimpleDiff(sameItem, sameContent)) {
//
//    private class SimpleDiff(
//        val sameItem: ((old: BindingAdapterItem, new: BindingAdapterItem) -> Boolean)?,
//        val sameContent: ((old: BindingAdapterItem, new: BindingAdapterItem) -> Boolean)?
//    ) : DiffUtil.ItemCallback<BindingAdapterItem>() {
//
//        override fun areItemsTheSame(
//            oldItem: BindingAdapterItem,
//            newItem: BindingAdapterItem
//        ): Boolean {
//            val overrideValue = sameItem?.invoke(oldItem, newItem)
//            Timber.d(if(overrideValue == true) "item same" else "different item")
//            return overrideValue ?: (oldItem.model ?: oldItem) == (newItem.model ?: newItem)
//        }
//
//        override fun areContentsTheSame(
//            oldItem: BindingAdapterItem,
//            newItem: BindingAdapterItem
//        ): Boolean {
//            val overrideValue = sameContent?.invoke(oldItem, newItem)
//            Timber.d(if(overrideValue == true) "content same" else "different content")
//            return overrideValue ?: (oldItem.model ?: oldItem) == (newItem.model ?: newItem)
//        }
//    }
//
//}