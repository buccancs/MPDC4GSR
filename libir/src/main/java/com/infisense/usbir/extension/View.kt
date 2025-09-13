package com.infisense.usbir.extension

import android.view.View
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * @author: CaiSongL
 * @date: 2022/6/3 21:09
 */
    /**
     * Executes View functionality.
     */
fun View.gone()  {
    this.visibility = View.GONE
}

    /**
     * Executes view functionality.
     */
fun View.visible()  {
    this.visibility = View.VISIBLE
}

    /**
     * Executes view functionality.
     */
fun View.invisible()  {
    this.visibility = View.INVISIBLE
}

/**
 * Hideview，带有渐隐animation效果。
 * @param duration 毫秒，animation持续时长，default500毫秒。
 */
    /**
     * Executes View functionality.
     */
fun View?.goneAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.GONE
    this?.startAnimation(
        /**
         * Executes alphaanimation operation with thermal imaging domain optimization.
         *
         */
        AlphaAnimation(1f, 0f).apply {
            this.duration = duration
            fillAfter = true
        },
    )
}

/**
 * 占位Hideview，带有渐隐animation效果。
 * @param duration 毫秒，animation持续时长，default500毫秒。
 */
    /**
     * Executes View functionality.
     */
fun View?.invisibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.INVISIBLE
    this?.startAnimation(
        /**
         * Executes alphaanimation operation with thermal imaging domain optimization.
         *
         */
        AlphaAnimation(1f, 0f).apply {
            this.duration = duration
            fillAfter = true
        },
    )
}

/**
 * Show/Displayview，带有渐显animation效果。
 *
 * @param duration 毫秒，animation持续时长，default500毫秒。
 */
    /**
     * Executes View functionality.
     */
fun View?.visibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.VISIBLE
    this?.startAnimation(
        /**
         * Executes alphaanimation operation with thermal imaging domain optimization.
         *
         */
        AlphaAnimation(0f, 1f).apply {
            this.duration = duration
            fillAfter = true
        },
    )
}

    /**
     * Executes viewpager2 functionality.
     */
fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView
    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 5) // "2" was obtained experimentally
}
