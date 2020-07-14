package rocks.wintren.rency.util.databinding.bindings

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import rocks.wintren.rency.util.ImageUtil
import java.nio.charset.Charset
import java.security.MessageDigest

@BindingAdapter("glideUrlCircle")
fun ImageView.bindGlideCircle(url: String?) {
    Glide.with(context)
        .load(url)
        .transform(TrimTransparent(), CircleCrop())
        .into(this)
}

class TrimTransparent() : BitmapTransformation() {

    companion object {
        private const val ID = "rocks.wintren.rency.transformations.TrimTransparent"
        private val ID_BYTES: ByteArray = ID.toByteArray(Charset.forName("UTF-8"))
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        return messageDigest.update(ID_BYTES);
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return ImageUtil.trim(toTransform)
    }

}
