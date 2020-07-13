package rocks.wintren.rency.util.extensions

import android.content.ContextWrapper
import android.view.View
import androidx.lifecycle.LifecycleOwner

fun View.getLifecycleOwner(): LifecycleOwner {
    var context = context
    while (context !is LifecycleOwner) {
        context = (context as ContextWrapper).baseContext
    }
    return context
}