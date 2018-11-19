package com.zry.shortcutdemo.recyclermore

import android.view.View
import com.lovejjfg.powerrecycle.AdapterLoader
import com.lovejjfg.powerrecycle.OnLoadMoreListener
import com.lovejjfg.powerrecycle.annotation.LoadState
import com.lovejjfg.powerrecycle.holder.AbsBottomViewHolder
import com.zry.shortcutdemo.R
import kotlinx.android.synthetic.main.layout_loadmore.view.content
import kotlinx.android.synthetic.main.layout_loadmore.view.footerContainer
import kotlinx.android.synthetic.main.layout_loadmore.view.progressbar

/**
 * Created by joe on 2018/9/26.
 * Email: lovejjfg@gmail.com
 */
class DaDaBottomHolder(bottomView: View) : AbsBottomViewHolder(bottomView) {
    private val pb = itemView.progressbar
    private val container = itemView.footerContainer
    private val content = itemView.content
    private var listener: View.OnClickListener? = null

    override fun onBind(loadMoreListener: OnLoadMoreListener?, @LoadState loadState: Int) {
        when (loadState) {
            AdapterLoader.STATE_LASTED -> {
                pb.visibility = View.GONE
                container.visibility = View.VISIBLE
                container.setOnClickListener(null)
                content.setText(R.string.no_more_data)
            }
            AdapterLoader.STATE_LOADING -> {
                content.visibility = View.VISIBLE
                content.setText(R.string.bottom_loading)
                container.setOnClickListener(null)
                pb.visibility = View.VISIBLE
                loadMoreListener?.onLoadMore()
            }
            AdapterLoader.STATE_ERROR -> {
                container.visibility = View.VISIBLE
                pb.visibility = View.GONE
                content.setText(R.string.power_recycler_load_error)
                if (listener == null) {
                    listener = View.OnClickListener {
                        content.setText(R.string.bottom_loading_error)
                        pb.visibility = View.VISIBLE
                        loadMoreListener?.onLoadMore()
                    }
                }
                container.setOnClickListener(listener)
            }
        }
    }
}
