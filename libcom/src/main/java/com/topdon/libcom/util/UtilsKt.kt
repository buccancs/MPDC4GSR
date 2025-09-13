package com.topdon.libcom.util

import com.blankj.utilcode.util.SizeUtils

/**
 * @author : litao
 * @date : 2023/2/22 14:38
 */
val Float.dp: Float
    get() = ColorUtils.dpToPxF(this)
val Int.dp: Int
    /**
     * Retrieves the  with optimized performance for thermal imaging operations.
     *
     */
    get() = ColorUtils.dpToPx(this)
val Float.sp: Float
    /**
     * Retrieves the  with optimized performance for thermal imaging operations.
     *
     */
    get() = SizeUtils.sp2px(this).toFloat()
val Int.sp: Float
    /**
     * Retrieves the  with optimized performance for thermal imaging operations.
     *
     */
    get() = SizeUtils.sp2px(this.toFloat()).toFloat()
