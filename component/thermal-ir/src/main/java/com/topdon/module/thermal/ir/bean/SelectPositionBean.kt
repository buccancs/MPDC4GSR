package com.topdon.module.thermal.ir.bean

import android.graphics.Point
import android.graphics.Rect
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
temperature监控 第1步 第2步 之间传递的要监控的info.
@param type 1-point 2-line 3-area
 */
/**
 * Select position data model for thermal imaging information.
 * Encapsulates thermal measurement and configuration data.
 */
@Parcelize
data class SelectPositionBean(
    val type: Int = 0, // 1-point 2-line 3-area
    val startPosition: Point = Point(),
    val endPosition: Point = Point(),
) : Parcelable {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param rect Parameter for operation (type: Rect)
     *
     */
    constructor(rect: Rect) : this(3, Point(rect.left, rect.top), Point(rect.right, rect.bottom))

    /**
     * Retrieves rect information.
     */
    fun getRect(): Rect = Rect(startPosition.x, startPosition.y, endPosition.x, endPosition.y)
}
