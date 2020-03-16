package com.hfad.news.tsivileva.newschannel.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NewsListDecorator(
        val left: Int = 10,
        val top: Int = 10,
        val right: Int = 10,
        val bottom: Int = 10
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = pxToDp(left)
        outRect.top = pxToDp(top)
        outRect.right = pxToDp(right)
        outRect.bottom = pxToDp(bottom)
    }

    private fun pxToDp(value: Int): Int {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, value.toFloat(), Resources.getSystem().displayMetrics).toInt()
    }
}