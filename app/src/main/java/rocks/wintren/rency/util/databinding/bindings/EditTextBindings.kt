package rocks.wintren.rency.util.databinding.bindings

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import rocks.wintren.rency.util.extensions.getInputManager

@BindingAdapter("onFocus")
fun EditText.bindOnFocusListener(onFocus: (() -> Unit)?) {
    if (onFocus == null) {
        onFocusChangeListener = null
    } else {

        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) onFocus.invoke()
            v.context.getInputManager().toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }
}
