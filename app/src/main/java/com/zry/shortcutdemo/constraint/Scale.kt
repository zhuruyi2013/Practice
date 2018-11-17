package com.zry.shortcutdemo.constraint

class Scale constructor(
    /**
     * 宽
     */
    val width: Float,
    /**
     * 高
     */
    val height: Float,
    /**
     * 占位图中间内容宽占整体view的宽度的比例
     */
    val valueWidthRatio: Float,
    /**
     * 占位图中间内容高占整体view的高度的比例
     */
    val valueHeightRatio: Float
) : Comparable<Scale> {
    /**
     * 整体view本身的宽高比
     */
    val scale: Float = width / height

    override fun compareTo(other: Scale): Int {
        return scale.compareTo(other.scale)
    }
}

object ScaleFatory {

    /**
     * 宽高比 1:1
     */
    fun createOneToOne(): Scale {
        return Scale(1F, 1F, 0.41F, 0.11F)
    }

    /**
     * 宽高比 16:9
     */
    fun createSixteenToNine(): Scale {
        return Scale(16F, 9F, 0.41F, 0.21F)
    }

    /**
     * 宽高比 3:1
     */
    fun createThreeToOne(): Scale {
        return Scale(3F, 1F, 0.41F, 0.32F)
    }

    /**
     * 宽高比 3:4
     */
    fun createThreeToFour(): Scale {
        return Scale(3F, 4F, 0.55F, 0.11F)
    }

    /**
     * 宽高比 4:3
     */
    fun createFourToThree(): Scale {
        return Scale(4F, 3F, 0.41F, 0.14F)
    }

    /**
     * 宽高比 2:1
     */
    fun createTwoToOne(): Scale {
        return Scale(2F, 1F, 0.41F, 0.2F)
    }
}
