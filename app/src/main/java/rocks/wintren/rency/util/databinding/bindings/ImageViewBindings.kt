package rocks.wintren.rency.util.databinding.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("glideUrlCircle")
fun ImageView.bindGlideCircle(url: String?) {
    Glide.with(context)
        .load(url)
        .circleCrop()
        .into(this)
}

@BindingAdapter("glideUrl")
fun ImageView.bindGlide(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}
