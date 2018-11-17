package com.zry.shortcutdemo.constraint

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.DrawableRes
import android.support.annotation.IntRange
import android.util.LruCache
import com.zry.shortcutdemo.constraint.BitmapCache.BitmapKey
import com.zry.shortcutdemo.constraint.BitmapCache.BitmapType.FILE
import com.zry.shortcutdemo.constraint.BitmapCache.BitmapType.RESOURCE
import java.io.File
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

/**
 * Created by 王天明 on 2018/10/19.
 */
class BitmapCache private constructor(@IntRange(from = 1) maxSize: Int) : LruCache<BitmapKey, SoftReference<Bitmap>>(maxSize) {

    private var bitmapCreatorDelegate: WeakReference<BitmapProvider>? = null

    override fun create(key: BitmapKey?): SoftReference<Bitmap>? {
        if (key == null) return null
        return try {
            return when (key.type) {
                RESOURCE -> SoftReference(
                    BitmapFactory.decodeResource(CommonHelper.context.resources, key.tag as Int)
                )
                FILE -> SoftReference(
                    BitmapFactory.decodeFile(key.tag as String)
                )
                else -> return SoftReference<Bitmap>(
                    bitmapCreatorDelegate?.get()?.createBitmap(key)
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun setBitmapProvider(provider: BitmapProvider?) {
        if (provider == null) {
            clearBitmapProvider()
            return
        }
        bitmapCreatorDelegate = WeakReference(provider)
    }

    fun clearBitmapProvider() {
        bitmapCreatorDelegate = null
    }

    class BitmapKey(
        val width: Int = 0,
        val height: Int = 0,
        val tag: Any,
        val type: BitmapType = BitmapType.RESOURCE
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BitmapKey) return false

            if (width != other.width) return false
            if (height != other.height) return false
            if (tag != other.tag) return false

            return true
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + height
            result = 31 * result + tag.hashCode()
            return result
        }

        companion object {
            fun createByResourceId(@DrawableRes resId: Int): BitmapKey {
                return BitmapKey(tag = resId)
            }

            fun createByFile(file: File): BitmapKey {
                return createByFile(file.absolutePath)
            }

            fun createByFile(path: String): BitmapKey {
                return BitmapKey(tag = path, type = FILE)
            }
        }
    }

    enum class BitmapType {
        FILE,
        RESOURCE,
        OTHER
    }

    interface BitmapProvider {
        fun createBitmap(key: BitmapKey): Bitmap?
    }

    companion object {

        private val INSTANCE by lazy { BitmapCache(20) }

        fun createNewInstance(@IntRange(from = 1) maxSize: Int): BitmapCache {
            return BitmapCache(maxSize)
        }

        fun getDefaultInstance(): BitmapCache {
            return INSTANCE
        }
    }
}
