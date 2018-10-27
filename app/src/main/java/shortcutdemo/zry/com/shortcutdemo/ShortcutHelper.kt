package shortcutdemo.zry.com.shortcutdemo

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import java.util.Arrays

/**
 * Created by zhuruyi on 2018/10/27.
 * Copyright  2018 henzry.zhu@dadaabc.com. All rights reserved
 */
object ShortcutHelper{
    lateinit var shortcutManager: ShortcutManager

    fun init(context: Context){
        shortcutManager = context.getSystemService<ShortcutManager>(ShortcutManager::class.java)
    }

    fun addShortcut(context: Context){
        val shortcut = ShortcutInfo.Builder(context, "id1")
            .setShortLabel("Website")
            .setLongLabel("Open the website")
            .setIcon(Icon.createWithResource(context, R.drawable.icon_website))
            .setIntent(
                Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.mysite.example.com/"))
            )
            .build()

        shortcutManager.dynamicShortcuts = Arrays.asList(shortcut)
    }
}
