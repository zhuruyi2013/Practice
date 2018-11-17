package com.zry.shortcutdemo.constraint

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue

/**
 * Created by zhuruyi on 2018/11/17.
 * Copyright  2018 henzry.zhu@dadaabc.com. All rights reserved
 */
fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}


fun Context.dpToPx(dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun Context.dpToPx(dp: Int): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
