package com.zry.shortcutdemo

import android.app.Application
import com.zry.shortcutdemo.constraint.CommonHelper

/**
 * Created by zhuruyi on 2018/11/17.
 * Copyright  2018 henzry.zhu@dadaabc.com. All rights reserved
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CommonHelper.context = this
    }
}
