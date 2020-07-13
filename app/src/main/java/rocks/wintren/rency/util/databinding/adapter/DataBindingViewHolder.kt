package rocks.wintren.rency.util.databinding.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import rocks.wintren.rency.util.extensions.getLifecycleOwner

class DataBindingViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BindingAdapterItem) {
        if (item.variableId != null && item.model != null)
            binding.setVariable(item.variableId, item.model)
        binding.lifecycleOwner = itemView.getLifecycleOwner()
        binding.executePendingBindings()
    }

}

