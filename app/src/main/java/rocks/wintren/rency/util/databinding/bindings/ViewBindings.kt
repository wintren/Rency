package rocks.wintren.rency.util.databinding.bindings

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleElseGone")
fun View.bindVisibleElseGone(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
}