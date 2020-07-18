package rocks.wintren.rency.app.ratecalc

import androidx.recyclerview.widget.DiffUtil
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.databinding.adapter.DataBindingAdapter

class CurrencyListAdapter : DataBindingAdapter(CurrencyDiff()) {

    private class CurrencyDiff : DiffUtil.ItemCallback<BindingAdapterItem>() {

        override fun areItemsTheSame(
            oldItem: BindingAdapterItem,
            newItem: BindingAdapterItem
        ): Boolean {
            oldItem.model as CurrencyItem
            newItem.model as CurrencyItem
            return oldItem.model.titleCurrencyCode == newItem.model.titleCurrencyCode
        }

        override fun areContentsTheSame(
            oldItem: BindingAdapterItem,
            newItem: BindingAdapterItem
        ): Boolean {
            // Should be true, we manage content through data binding so it should be regarded as same for recyclerview
            return true
        }

    }

}