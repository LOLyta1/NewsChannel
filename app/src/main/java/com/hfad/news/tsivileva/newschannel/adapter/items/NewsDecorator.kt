package com.hfad.news.tsivileva.newschannel.adapter.items

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NewsDecorator(val left: Int, val top: Int, val right: Int, val bottom: Int) : RecyclerView.ItemDecoration() {

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

/*    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
       // отступ справа-это ширина RecyclerView - 32
        val deviderLeft = 32;
        val deviderRight = parent.width - 32

       // добавляем отступ снизу ко всем, кроме последнего элемента
        for (i in 0 until parent.childCount) {
            if (i != parent.childCount - 1) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop = child.bottom + params.bottomMargin
                val deviderBottom = dividerTop + mDivider.intrinsicHeight
                mDivider.setBounds(deviderLeft, dividerTop, deviderRight, deviderBottom)
                mDivider.draw(c)
            }
        }
    }*/
}