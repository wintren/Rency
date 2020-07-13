package rocks.wintren.rency.util

import android.os.Handler
import android.util.Log
import rocks.wintren.rency.BuildConfig

inline fun <T> logTime(desc: String, block: () -> T): T {
    return if (BuildConfig.DEBUG) {
        val startAt = System.currentTimeMillis()
        val result = block()
        val duration = System.currentTimeMillis() - startAt
        Log.d("logTime", "$desc took ${duration}ms")
        result
    } else {
        block()
    }
}

fun delay(delayTime: Long, function: () -> Unit) {
    Handler().postDelayed(function, delayTime)
}