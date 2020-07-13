package rocks.wintren.rency.util.databinding.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rocks.wintren.rency.util.view.PaddingDecoration

@BindingAdapter("useAdapter", "useManager", requireAll = false)
fun RecyclerView.bindAdapterManager(
    adapter: RecyclerView.Adapter<*>?,
    manager: RecyclerView.LayoutManager?
) {
    setAdapter(adapter)
    layoutManager = manager ?: LinearLayoutManager(context)
}

@BindingAdapter(
    "spacing",
    "spacingStart",
    "spacingTop",
    "spacingEnd",
    "spacingBottom",
    requireAll = false
)
fun RecyclerView.bindSpacing(
    spacing: Float?,
    spacingStart: Float?,
    spacingTop: Float?,
    spacingEnd: Float?,
    spacingBottom: Float?
) {
    addItemDecoration(
        spacing?.toInt()
            ?.let { PaddingDecoration(it, it, it, it) }
            ?: PaddingDecoration(
                start = spacingStart?.toInt() ?: 0,
                top = spacingTop?.toInt() ?: 0,
                end = spacingEnd?.toInt() ?: 0,
                bottom = spacingBottom?.toInt() ?: 0
            )
    )
}
