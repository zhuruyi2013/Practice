package com.zry.shortcutdemo.constraint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.zry.shortcutdemo.R
import com.zry.shortcutdemo.constraint.BitmapCache.BitmapKey
import com.zry.shortcutdemo.constraint.BitmapCache.BitmapType.OTHER
import java.lang.ref.SoftReference

/**
 * Created by 王天明 on 2018/8/17.
 *
 */
class DaDaPlaceholderDrawable(
    val context: Context,
    var backgroundColor: Int = context.color(R.color.dada_gray_5),
    var cornerRadius: Float = 0.0f,
    val model: Int = MODEL_RECT
) : Drawable() {

    /**
     * 支持的比例列表
     */
    private var supportScales = mutableListOf<Scale>()

    init {
        supportScales.add(ScaleFatory.createOneToOne())
        supportScales.add(ScaleFatory.createSixteenToNine())
        supportScales.add(ScaleFatory.createThreeToOne())
        supportScales.add(ScaleFatory.createThreeToFour())
        supportScales.add(ScaleFatory.createFourToThree())
        supportScales.add(ScaleFatory.createTwoToOne())
        // 排序
        supportScales.sortWith(kotlin.Comparator { o1, o2 ->
            return@Comparator o1.scale.compareTo(o2.scale)
        })
    }

    /**
     * 当前比例
     */
    private var currentScale: Float? = null

    /**
     * 设计提供的要缩放到的目标比例
     */
    private var valueTargetScale: Scale? = null

    private val paint = Paint()
    private val mRect = RectF()

    private val defaultLogo by lazy {
        BitmapCache.getDefaultInstance().get(BitmapKey.createByResourceId(R.mipmap.ic_logo_placeholder)).get()
            ?: BitmapFactory.decodeResource(context.resources, R.mipmap.ic_logo_placeholder)
    }

    var logo: Bitmap = defaultLogo

    init {
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.color = backgroundColor
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRect.set(bounds)
        // 宽除高
        currentScale = mRect.width() / mRect.height()
        findValueTargetScale()
        scaleLogoBitmap()
    }

    private fun scaleLogoBitmap() {
        val targetScale = valueTargetScale ?: return
        if (mRect.width() > 0 && mRect.height() > 0) {
            // 计算出目标宽高
            val min = Math.min(mRect.width(), mRect.height())
            val rectWidth = if (model == MODEL_CIRCLE) min else mRect.width()
            val rectHeight = if (model == MODEL_CIRCLE) min else mRect.height()
            val targetWidth: Float = rectWidth * targetScale.valueWidthRatio
            val targetHeight: Float = rectHeight * targetScale.valueHeightRatio

            val bitmapKey = BitmapKey(targetWidth.toInt(), targetHeight.toInt(), targetScale.scale, OTHER)
            val bitmap = BitmapCache.getDefaultInstance().get(bitmapKey).get()
            if (logo.isRecycled) {
                logo = BitmapCache.getDefaultInstance().get(BitmapKey.createByResourceId(R.mipmap.ic_logo_placeholder)).get()
                    ?: BitmapFactory.decodeResource(context.resources, R.mipmap.ic_logo_placeholder)
            }
            logo = if (bitmap == null || bitmap.isRecycled) {
                val matrix = Matrix()
                // 计算出原始图片宽高放大到设计要求的比例的宽高需要缩放多少
                matrix.postScale(targetWidth / logo.width, targetHeight / logo.height)
                // 缩放原始bitmap
                val tempBitmap = Bitmap.createBitmap(logo, 0, 0, logo.width, logo.height, matrix, true)
                BitmapCache.getDefaultInstance().put(bitmapKey, SoftReference(tempBitmap))
                tempBitmap
            } else {
                bitmap
            }
        }
    }

    override fun draw(canvas: Canvas) {
        drawBg(canvas)
        // 绘制logo
        if (logo.isRecycled) {
            scaleLogoBitmap()
        }
        drawLogo(canvas)
    }

    /**
     *  找到内容放大缩小目标比例
     *
     *  现有{@currentScale}是控件整体大小的比例，这个比例对应的是4:3、16:9这类的比例
     *
     *  这个比例对应下，内容会占据一个尺寸，这个尺寸是定死的。
     *
     *  现在需要判断@{@currentScale}是那个支持比
     *
     *  如果有误差,计算是在哪两个支持的比例的中间，选择差距最小的那个
     */
    private fun findValueTargetScale() {
        if (model == MODEL_CIRCLE) {
            // 圆形固定1比1
            valueTargetScale = ScaleFatory.createOneToOne()
            return
        }
        // 比当前比例小的中间最大的那个 ，例如  当前比例是 4， 1 2 3 5 6，minTarget是3那个
        var minTarget: Scale = supportScales[0]
        // 比当前比例大的中间最小的那个 ，例如  当前比例是 4， 1 2 3 5 6，minTarget是5那个
        var maxTarget: Scale = supportScales[0]
        currentScale?.let { scale ->
            supportScales.forEach {
                if (currentScale == it.scale) {
                    valueTargetScale = it
                    return@forEach
                } else {
                    if (scale < it.scale) {
                        maxTarget = it
                        return@forEach
                    } else {
                        minTarget = it
                    }
                }
            }

            if (currentScale != valueTargetScale?.scale) {
                // 判断选择左小还是右大的那个
                valueTargetScale = if (scale - minTarget.scale > maxTarget.scale - scale) {
                    maxTarget
                } else {
                    minTarget
                }
            }
        }
    }

    private fun drawLogo(canvas: Canvas) {
        if (logo.isRecycled) return
        val left = if (logo.width > canvas.width) 0.0F else (canvas.width - logo.width) / 2.0F
        val top = if (logo.height > canvas.height) 0.0F else (canvas.height - logo.height) / 2.0F
        canvas.drawBitmap(logo, left, top, paint)
    }

    private fun drawBg(canvas: Canvas) {
        if (model == MODEL_RECT) {
            canvas.drawRoundRect(
                mRect,
                cornerRadius, cornerRadius,
                paint
            )
        } else {
            canvas.drawCircle(
                mRect.centerX(), mRect.centerY(),
                Math.min(mRect.width(), mRect.height()) / 2.0f,
                paint
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    companion object {
        /**
         * 圆形背景
         */
        const val MODEL_CIRCLE = 0x11122233
        /**
         * 圆角背景
         */
        const val MODEL_RECT = 0x11122234
    }
}
