package com.zry.shortcutdemo.constraint

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zry.shortcutdemo.R
import com.zry.shortcutdemo.constraint.SelectVideoAdapter.ViewHolder

class SelectVideoAdapter(
    val context: Context,
    var dataList: List<RecordingVideoItem>
) : RecyclerView.Adapter<ViewHolder>() {

    private var onItemClickCallback: OnClickCallback? = null

    var selectPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_da_talent_show_publish,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        val dadaChoicePublish = dataList[position]
        if (dadaChoicePublish.isShared()) {
            holder.isShared.visibility = View.VISIBLE
        } else {
            holder.isShared.visibility = View.GONE
        }

        if (selectPosition == position) {
            holder.isSelectIcon.visibility = View.VISIBLE
            holder.isSelectBg.visibility = View.VISIBLE
        } else {
            holder.isSelectIcon.visibility = View.GONE
            holder.isSelectBg.visibility = View.INVISIBLE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dadaChoicePublish = dataList[position]

        if(position < 3) {
            holder.videoCover.setImageResource(R.mipmap.test_small)
        }else {
            holder.videoCover.postDelayed({
                holder.videoCover.setImageResource(R.mipmap.test)
            }, 3000)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun onItemClick(callback: OnClickCallback) {
        this.onItemClickCallback = callback
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val isShared: TextView = itemView.findViewById(R.id.sharedStatusText)
        val isSelectIcon: ImageView = itemView.findViewById(R.id.isSelectImgIcon)
        val isSelectBg: ImageView = itemView.findViewById(R.id.isSelectImgBg)
        val videoCover: ImageView = itemView.findViewById(R.id.videoCoverImg)
    }

    fun replaceData(dadaChoicePublishList: List<RecordingVideoItem>) {
        selectPosition = 0
        run breaking@{
            dadaChoicePublishList.forEachIndexed { index, recordingVideoItem ->
                if (!recordingVideoItem.isShared()) {
                    selectPosition = index
                    return@breaking
                }
            }
        }
        dataList = dadaChoicePublishList
        notifyDataSetChanged()
    }

    fun getSelectVideo(): RecordingVideoItem? {
        return if (selectPosition >= 0) {
            dataList[selectPosition]
        } else {
            null
        }
    }
}

typealias OnClickCallback = (data: RecordingVideoItem, position: Int) -> Unit

data class RecordingVideoItem(
    val videoUrl: String,
    val videoId: String,
    val coverUrl: String,
    private val isShared: Int
) {
    fun isShared(): Boolean {
        return isShared == 1
    }
}
