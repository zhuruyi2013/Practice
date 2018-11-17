package com.zry.shortcutdemo.constraint


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.zry.shortcutdemo.R.layout
import kotlinx.android.synthetic.main.content_constaint_layout.daTalentShowPublishVideoList

class ConstaintLayoutActivity : AppCompatActivity() {

    private var recordingAdapter: SelectVideoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.content_constaint_layout)

        val listData = mutableListOf<RecordingVideoItem>()
        for (i in 0..20) {
            val item = RecordingVideoItem("a$i", "b$i", "c$i", i)
            listData.add(item)
        }

        recordingAdapter =
            SelectVideoAdapter(
                this,
                listData
            )
        daTalentShowPublishVideoList.layoutManager = GridLayoutManager(this, 3)
        daTalentShowPublishVideoList.addItemDecoration(ItemDivider().apply {
            lineWidth = this@ConstaintLayoutActivity.dpToPx(2)
            lineHeight = this@ConstaintLayoutActivity.dpToPx(2)
        })
        daTalentShowPublishVideoList.adapter = recordingAdapter
    }
}
