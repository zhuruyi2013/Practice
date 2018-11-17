package com.zry.shortcutdemo.constraint

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.LinearLayout

class ItemDivider : RecyclerView.ItemDecoration() {
    private var dividerDrawable: Drawable? = null
    private var DEFAULT_LINE_WIDTH = 10
    private var DEFAULT_LINE_HEIGHT = 20

    /**
     * 线的宽度
     */
    var lineWidth = DEFAULT_LINE_WIDTH
    /**
     * 线的高度
     */
    var lineHeight = DEFAULT_LINE_HEIGHT
    /**
     * 头的数量
     */
    var headerCount = 0
    /**
     * 尾的数量
     */
    var footerCount = 0

    init {
        dividerDrawable = ColorDrawable(Color.WHITE)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (isSkipDraw(parent, view))
            return
        val currentPosition = parent.getChildAdapterPosition(view)
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter.itemCount
        var right = lineWidth
        var bottom = lineHeight
        if (isNotDrawBottom(view, parent, currentPosition, spanCount, childCount))
            bottom = 0
        if (isNotDrawRight(view, parent, currentPosition, spanCount, childCount))
            right = 0
        outRect.set(0, 0, right, bottom)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(canvas, parent, lineWidth, lineHeight)
        drawVertical(canvas, parent, lineWidth, lineHeight)
    }

    /**
     * 是否不绘制右部
     *
     * @param view 当前的view，StaggeredGridLayoutManager 用
     * @param parent RecyclerView
     * @param currentPosition 当前的位置，GridLayoutManager、LinearLayoutManager用
     * @param spanCount 列数
     * @param adapterCount adapter的总数
     * @return 返回true代表不绘制右部，返回false，代表绘制右部
     */
    private fun isNotDrawRight(
        view: View,
        parent: RecyclerView,
        currentPosition: Int,
        spanCount: Int,
        adapterCount: Int
    ): Boolean {
        var currentPosition = currentPosition
        var adapterCount = adapterCount
        val layoutManager = parent.layoutManager
        when (layoutManager) {
            is GridLayoutManager -> {
                currentPosition -= headerCount
                adapterCount -= headerCount + footerCount
                return if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                    (currentPosition + 1) % spanCount == 0
                } else {
                    if (adapterCount % spanCount == 0)
                        currentPosition >= adapterCount - spanCount
                    else
                        currentPosition >= adapterCount - adapterCount % spanCount
                }
            }
            is LinearLayoutManager -> return layoutManager.orientation == LinearLayout.VERTICAL || currentPosition == adapterCount - footerCount - 1
            is StaggeredGridLayoutManager -> {
                val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                return layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL && (lp.spanIndex + 1) % spanCount == 0
            }
            else -> return false
        }
    }

    /**
     * 是否不绘制底部
     *
     * @param view 当前的view，StaggeredGridLayoutManager 用
     * @param parent RecyclerView
     * @param currentPosition 当前的位置，GridLayoutManager、LinearLayoutManager用
     * @param spanCount 列数
     * @param adapterCount adapter的总数
     * @return 返回true代表不绘制底部，返回false，代表绘制底部
     */
    private fun isNotDrawBottom(
        view: View,
        parent: RecyclerView,
        currentPosition: Int,
        spanCount: Int,
        adapterCount: Int
    ): Boolean {
        var currentPosition = currentPosition
        var adapterCount = adapterCount
        val layoutManager = parent.layoutManager
        when (layoutManager) {
            is GridLayoutManager -> {
                currentPosition -= headerCount
                adapterCount -= headerCount + footerCount
                return if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                    if (adapterCount % spanCount == 0)
                        currentPosition >= adapterCount - spanCount
                    else
                        currentPosition >= adapterCount - adapterCount % spanCount
                } else {
                    (currentPosition + 1) % spanCount == 0
                }
            }
            is LinearLayoutManager -> return layoutManager.orientation != LinearLayout.VERTICAL || currentPosition == adapterCount - footerCount - 1
            is StaggeredGridLayoutManager -> {
                val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                return layoutManager.orientation != StaggeredGridLayoutManager.VERTICAL && (lp.spanIndex + 1) % spanCount == 0
            }
            else -> return false
        }
    }

    /**
     * 绘制水平线
     *
     * @param canvas 画布
     * @param parent RecyclerView
     * @param lineWidth 线宽
     * @param lineHeight 线高
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, lineWidth: Int, lineHeight: Int) {
        var isDrawDoubleLine = false
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager && layoutManager.orientation == StaggeredGridLayoutManager.HORIZONTAL)
        // 绘制双线
            isDrawDoubleLine = true
        canvas.save()
        val spanCount = getSpanCount(parent)
        val childCount = parent.childCount
        val adapterCount = parent.adapter.itemCount
        if (parent.clipToPadding) {
            canvas.clipRect(
                parent.paddingLeft, parent.paddingTop,
                parent.width - parent.paddingRight,
                parent.height - parent.paddingBottom
            )
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (isSkipDraw(parent, child))
                continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (!isNotDrawBottom(child, parent, currentPosition, spanCount, adapterCount)) {
                val bottomLineWidth = if (isNotDrawRight(
                        child,
                        parent,
                        currentPosition,
                        spanCount,
                        adapterCount
                    )) 0 else lineWidth
                val downLeft = child.left - params.leftMargin
                val downTop = child.bottom + params.bottomMargin
                val downRight = child.right + params.rightMargin + bottomLineWidth
                val downBottom = downTop + lineHeight
                dividerDrawable?.setBounds(downLeft, downTop, downRight, downBottom)
                dividerDrawable?.draw(canvas)
            }
            if (isDrawDoubleLine && isStaggeredGridNotFirstView(child, spanCount)) {
                val upLeft = child.left - params.leftMargin
                val upTop = child.top + params.topMargin - lineHeight
                val upRight = child.right + params.rightMargin + lineWidth
                val upBottom = upTop + lineHeight
                dividerDrawable?.setBounds(upLeft, upTop, upRight, upBottom)
                dividerDrawable?.draw(canvas)
            }
        }
        canvas.restore()
    }

    /**
     * 绘制垂直线
     *
     * @param canvas 画布
     * @param parent RecyclerView
     * @param lineWidth 线宽
     * @param lineHeight 线高
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView, lineWidth: Int, lineHeight: Int) {
        var lineHeight = lineHeight
        var isDrawDoubleLine = false
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager && layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL)
            isDrawDoubleLine = true
        canvas.save()
        if (parent.clipToPadding) {
            canvas.clipRect(
                parent.paddingLeft, parent.paddingTop,
                parent.width - parent.paddingRight,
                parent.height - parent.paddingBottom
            )
        }
        val spanCount = getSpanCount(parent)
        val childCount = parent.childCount
        val adapterCount = parent.adapter.itemCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (isSkipDraw(parent, child))
                continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (!isNotDrawRight(child, parent, currentPosition, spanCount, adapterCount)) {
                if (isNotDrawBottom(child, parent, currentPosition, spanCount, adapterCount))
                    lineHeight = 0
                val left = child.right + params.rightMargin
                val top = child.top - params.topMargin
                val right = left + lineWidth
                val bottom = child.bottom + params.bottomMargin + lineHeight
                dividerDrawable?.setBounds(left, top, right, bottom)
                dividerDrawable?.draw(canvas)
            }
            if (isDrawDoubleLine && isStaggeredGridNotFirstView(child, spanCount)) {
                val left = child.left + params.leftMargin - lineWidth
                val top = child.top - params.topMargin
                val right = left + lineWidth
                val bottom = child.bottom + params.bottomMargin + lineHeight
                dividerDrawable?.setBounds(left, top, right, bottom)
                dividerDrawable?.draw(canvas)
            }
        }
        canvas.restore()
    }

    /**
     * 是否是StaggeredGridLayoutManager的中间的view
     *
     * @param view 测定的view
     * @param spanCount 列数
     */
    private fun isStaggeredGridNotFirstView(view: View, spanCount: Int): Boolean {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        return lp.spanIndex != 0
    }

    /**
     * 是否跳过绘画
     *
     * @param parent RecyclerView
     * @param view 当前View
     */
    private fun isSkipDraw(parent: RecyclerView, view: View): Boolean {
        val currentPosition = parent.getChildAdapterPosition(view)
        val adapterCount = parent.adapter.itemCount
        return currentPosition < headerCount || currentPosition >= adapterCount - footerCount
    }

    /**
     * 获取列数
     *
     * @param parent RecyclerView
     * @return 列数
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is LinearLayoutManager) {
            spanCount = 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }
}
