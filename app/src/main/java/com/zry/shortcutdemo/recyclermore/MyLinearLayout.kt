package com.zry.shortcutdemo.recyclermore

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

/**
 * Created by zhuruyi on 2018/11/18.
 * Copyright  2018 henzry.zhu@dadaabc.com. All rights reserved
 */
class MyLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val TAG = "MyLinearLayout"

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (visibility == View.GONE) {
            Log.d(TAG, "set GONE")
        } else if (visibility == View.VISIBLE) {
            Log.d(TAG, "set VISIBLE")
        }
    }
}
