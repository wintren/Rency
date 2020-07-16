package rocks.wintren.rency.util.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.getInputManager() : InputMethodManager{
    return this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}