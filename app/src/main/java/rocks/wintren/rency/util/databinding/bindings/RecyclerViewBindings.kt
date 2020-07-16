package rocks.wintren.rency.util.databinding.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("useAdapter", "useManager", requireAll = false)
fun RecyclerView.bindAdapterManager(
    adapter: RecyclerView.Adapter<*>?,
    manager: RecyclerView.LayoutManager?
) {
    setAdapter(adapter)
    layoutManager = manager ?: LinearLayoutManager(context)
}