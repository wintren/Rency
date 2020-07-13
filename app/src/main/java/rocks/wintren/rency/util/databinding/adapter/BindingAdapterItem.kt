package rocks.wintren.rency.util.databinding.adapter

import androidx.annotation.LayoutRes

data class BindingAdapterItem(
    @LayoutRes val layout: Int,
    val variableId: Int? = null,
    val model: Any? = null
)

fun <T> Sequence<T>.mapToBindingAdapterItem(@LayoutRes layout: Int, bindingReference: Int): Sequence<BindingAdapterItem> {
    return this.map { BindingAdapterItem(layout, bindingReference, it) }
}

fun <T> List<T>.mapToBindingAdapterItem(@LayoutRes layout: Int, bindingReference: Int): List<BindingAdapterItem> {
    return this.map { BindingAdapterItem(layout, bindingReference, it) }
}
