package rocks.wintren.rency.util.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaddingDecoration(
    private val start: Int = 0,
    private val top: Int = 0,
    private val end: Int = 0,
    private val bottom: Int = 0
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val isFirstView = position == 0
        val isLastView = position == parent.adapter!!.itemCount - 1

        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager as LinearLayoutManager?
            when (linearLayoutManager!!.orientation) {
                LinearLayoutManager.HORIZONTAL -> {
                    outRect.top = top
                    outRect.bottom = bottom
                    if (isFirstView) outRect.left = start
                    if (isLastView) outRect.right = end
                }
                LinearLayoutManager.VERTICAL -> {
                    outRect.left = start
                    outRect.right = end
                    if (isFirstView) outRect.top = top
                    if (isLastView) outRect.bottom = bottom
                }
            }
        } else {
            TODO("Not supported yet, implement it")
        }
    }
}