package rocks.wintren.rency.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifeCycleOwner: LifecycleOwner, onData: (T) -> Unit) {
    observe(lifeCycleOwner, DataObserver<T>(onData))
}

class DataObserver<T>(private val onData: (T) -> Unit) : Observer<T> {
    override fun onChanged(data: T): Unit = onData.invoke(data)
}