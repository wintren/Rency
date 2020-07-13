package rocks.wintren.rency.util.databinding.bindings

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("refreshListener")
fun SwipeRefreshLayout.bindRefreshListener(listener: (() -> Unit)) {
    setOnRefreshListener {
        listener.invoke()
    }
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.bindIsRefreshing(isRefreshing: Boolean?) {
    this.isRefreshing = isRefreshing ?: return
}
