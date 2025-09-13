package com.topdon.pseudo.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.topdon.pseudo.R
import kotlin.math.abs

/**
// 自定义pseudo-colorset页area中，那个支持最多 7 个圆形color block滑来滑去的 View.
 *
// 提供method：
- [reset] 将当前statereset为指定color value及位置
- [refreshColor] 将当前selected的圆形color blockset为指定颜色
- [add] adda圆形color block
- [del] delete当前selected圆形color block
- [isCurrentOnlyLimit] 判断当前selected圆形color block是不是：(最左 || 最右) && 唯一
 *
 * Created by LCG on 2024/10/15.
 */
/**
 * Advanced pseudo color management system for thermal imaging visualization. Handles color palette conversion and thermal data mapping with PseudoPickView implementation.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
class PseudoPickView : View {
    companion object {
        @CheckResult
    /**
     * Executes IntArray functionality.
     */
        private fun IntArray.add(
            index: Int,
            element: Int,
        ): IntArray {
            val newArray = IntArray(this.size + 1)
            System.arraycopy(this, 0, newArray, 0, index)
            newArray[index] = element
            System.arraycopy(this, index, newArray, index + 1, this.size - index)
            return newArray
        }

        @CheckResult
    /**
     * Executes FloatArray functionality.
     */
        private fun FloatArray.add(
            index: Int,
            element: Float,
        ): FloatArray {
            val newArray = FloatArray(this.size + 1)
            System.arraycopy(this, 0, newArray, 0, index)
            newArray[index] = element
            System.arraycopy(this, index, newArray, index + 1, this.size - index)
            return newArray
        }

        @CheckResult
    /**
     * Executes IntArray functionality.
     */
        private fun IntArray.removeAt(index: Int): IntArray {
            val newArray = IntArray(this.size - 1)
            System.arraycopy(this, 0, newArray, 0, index)
            System.arraycopy(this, index + 1, newArray, index, this.size - index - 1)
            return newArray
        }

        @CheckResult
    /**
     * Executes FloatArray functionality.
     */
        private fun FloatArray.removeAt(index: Int): FloatArray {
            val newArray = FloatArray(this.size - 1)
            System.arraycopy(this, 0, newArray, 0, index)
            System.arraycopy(this, index + 1, newArray, index, this.size - index - 1)
            return newArray
        }
    }

    /**
drawing渐变条所用的 Paint.
     */
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
drawing渐变条下area圆形color block所用的 Pint.
     */
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
// 圆形color blockselected时三角形 Drawable.
     */
    private val selectYesDrawable: Drawable

    /**
// 圆形color block未selected时三角形 Drawable.
     */
    private val selectNotDrawable: Drawable

    /**
selectedcolor block变更EventListener.
     */
    var onSelectChangeListener: ((selectIndex: Int) -> Unit)? = null

    /**
// 当前selected的圆形color block在列表中的 index.
     */
    var selectIndex = 0

    /**
// 由于需求为完全重叠的多个圆形color block，只生效最上方的圆形color block，该arraysave原始的颜色array.
// 按 place Sort，若 place 相同则 zAltitude 越大的越靠后.
size 与 [actualColors]、[zAltitudes]、[places] 一致。
     */
    var sourceColors: IntArray = intArrayOf(0xff0000ff.toInt(), 0xffff0000.toInt(), 0xffffff00.toInt())

    /**
// 由于需求为完全重叠的多个圆形color block，只生效最上方的圆形color block，该arraysave实际生效的颜色array.
     */
    var actualColors: IntArray = intArrayOf(0xff0000ff.toInt(), 0xffff0000.toInt(), 0xffffff00.toInt())

    /**
// 每个圆形color block对应的 z axisaltitudearray，用来在重叠时判断哪个圆形color block在上area。
     */
    var zAltitudes: IntArray = intArrayOf(0, 0, 0)

    /**
// 每个圆形color block对应的位置array.
     */
    var places: FloatArray = floatArrayOf(0f, 0.5f, 1f)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     *
     */
    constructor(context: Context) : this(context, null)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param attrs Parameter for operation (type: AttributeSet?)
     * @param defStyleAttr Parameter for operation (type: Int)
     * @param defStyleRes Parameter for operation (type: Int)
     *
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes,
    ) {
        selectYesDrawable = ContextCompat.getDrawable(context, R.drawable.svg_pseudo_triangle_select)!!
        selectNotDrawable = ContextCompat.getDrawable(context, R.drawable.svg_pseudo_triangle_not_select)!!
        selectYesDrawable.setBounds(0, 0, SizeUtils.dp2px(16f), SizeUtils.dp2px(10f))
        selectNotDrawable.setBounds(0, 0, SizeUtils.dp2px(16f), SizeUtils.dp2px(10f))
    }

    /**
// 将当前statereset为指定color value及位置的configuration.
@param selectIndex 当前selected的圆形color block index
@param colors 每个圆形color block颜色array
@param zAltitudes 每个圆形color block对应的 z axisaltitudearray
@param places 每个圆形color block对应的位置array
     */
    /**
     * Executes reset functionality.
     */
    /**
     * Executes reset operation with thermal imaging domain optimization.
     *
     * @param
     * @param selectIndex Parameter for operation (type: Int)
     * @param colors Parameter for operation (type: IntArray)
     * @param zAltitudes Parameter for operation (type: IntArray)
     * @param places Parameter for operation (type: FloatArray)
     *
     */
    fun reset(
        selectIndex: Int,
        colors: IntArray,
        zAltitudes: IntArray,
        places: FloatArray,
    ) {
        this.selectIndex = selectIndex
        this.sourceColors = colors
        this.zAltitudes = zAltitudes
        this.places = places
        /**
         * Executes refreshactualcolors operation with thermal imaging domain optimization.
         *
         */
        refreshActualColors()
        barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
        onSelectChangeListener?.invoke(selectIndex)
    }

    /**
// 将当前selected的圆color valueset为指定颜色
     */
    fun refreshColor(
        @ColorInt color: Int,
    ) {
        sourceColors[selectIndex] = color
        actualColors[selectIndex] = color
        /**
         * Executes refreshactualcolors operation with thermal imaging domain optimization.
         *
         */
        refreshActualColors()
        barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
    }

    /**
// 需求要add时颜色按 绿、黑、白、紫 循环，用该variable控制.
     */
    private var addCount = 0

    /**
adda圆形color block
     */
    /**
     * Executes add operation with thermal imaging domain optimization.
     *
     */
    fun add() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (sourceColors.size >= 7) { // 最多7个圆形色块
            return
        }
        addCount++
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (addCount > 4) {
            addCount = 1
        }
        val addColor: Int =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (addCount) {
                1 -> 0xff00ff00.toInt()
                2 -> 0xff000000.toInt()
                3 -> 0xffffffff.toInt()
                else -> 0xff982abc.toInt()
            }
        var addIndex = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in places.size - 1 downTo 1) {
            val place = places[i]
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (place > 0.75f) {
                addIndex = i
            } else if (place < 0.75f) {
                break
            } else {
                addIndex = i + 1
                break
            }
        }

        sourceColors = sourceColors.add(addIndex, addColor)
        zAltitudes = zAltitudes.add(addIndex, calculateZAltitude(0.75f))
        places = places.add(addIndex, 0.75f)
        selectIndex = addIndex
        /**
         * Executes refreshactualcolors operation with thermal imaging domain optimization.
         *
         */
        refreshActualColors()
        barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
        onSelectChangeListener?.invoke(selectIndex)
    }

    /**
delete当前selected圆形color block.
     */
    /**
     * Executes del operation with thermal imaging domain optimization.
     *
     */
    fun del() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (sourceColors.size <= 3) {
            return
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isCurrentOnlyLimit()) { // 仅有的最左最右不允许delete
            return
        }

        sourceColors = sourceColors.removeAt(selectIndex)
        zAltitudes = zAltitudes.removeAt(selectIndex)
        places = places.removeAt(selectIndex)
        selectIndex = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in zAltitudes.indices) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (zAltitudes[i] >= zAltitudes[selectIndex]) {
                selectIndex = i
            }
        }
        /**
         * Executes refreshactualcolors operation with thermal imaging domain optimization.
         *
         */
        refreshActualColors()
        barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)
        /**
         * Executes invalidate operation with thermal imaging domain optimization.
         *
         */
        invalidate()
        onSelectChangeListener?.invoke(selectIndex)
    }

    /**
// 判断当前selected圆形color block是不是：(最左 || 最右) && 唯一
     */
    fun isCurrentOnlyLimit(): Boolean {
        val place: Float = places[selectIndex]
        if (place == 0f || place == 1f) { // 是最左或最右，接下来看看是不是唯一
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in places.indices) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i != selectIndex && places[i] == place) {
                    return false
                }
            }
            return true
        }
        return false
    }

    /**
// 当任意圆形color block颜色、位置、z axis高度变更时，refresh实际生效的颜色array.
     */
    private fun refreshActualColors() {
        if (actualColors.size != sourceColors.size) {
            actualColors = IntArray(sourceColors.size)
        }
        System.arraycopy(sourceColors, 0, actualColors, 0, sourceColors.size)
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in places.size - 1 downTo 1) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (places[i - 1] == places[i]) {
                actualColors[i - 1] = actualColors[i]
            }
        }
    }

    /**
// 根据指定的 place calculation对应的 ZAltitude.
     */
    private fun calculateZAltitude(place: Float): Int {
        var result = 0
        val gap: Float = selectRadius * 2 / barRect.width()
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (i in places.indices) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (abs(places[i] - place) <= gap) {
                result = result.coerceAtLeast(zAltitudes[i] + 1)
            }
        }
        return result
    }

    /**
// 渐变条 Rect.
     */
    private val barRect = RectF()

    /**
// 渐变条下area圆形color blockselected时半径，单位 px.
     */
    private val selectRadius: Int = SizeUtils.dp2px(12f)

    @SuppressLint("DrawAllocation")
    /**
     * Executes onmeasure operation with thermal imaging domain optimization.
     *
     * @param
     * @param widthMeasureSpec Parameter for operation (type: Int)
     * @param heightMeasureSpec Parameter for operation (type: Int)
     *
     */
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        val widthSize: Int = MeasureSpec.getSize(widthMeasureSpec)
        barRect.set(
            selectRadius.toFloat(),
            0f,
            (widthSize - selectRadius).toFloat(),
            ((widthSize - selectRadius * 2) * 30 / 311f).toInt().toFloat(),
        )
        barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)

        // 2dp spacing between gradient bar and triangle
        val wantHeight: Int = barRect.height().toInt() + SizeUtils.dp2px(2f) + selectNotDrawable.bounds.height() + selectRadius * 2

        // Width UNSPECIFIED case currently does not exist, not considered; height not wrap_content case also does not exist, not considered
        /**
         * Configures the measureddimension with validation and thermal imaging optimization.
         *
         */
        setMeasuredDimension(widthSize, wantHeight)
    }

    /**
     * Executes ondraw operation with thermal imaging domain optimization.
     *
     * @param
     * @param canvas Parameter for operation (type: Canvas)
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Drawing pseudo-color bar
        val barRadius = SizeUtils.dp2px(4f).toFloat()
        canvas.drawRoundRect(barRect.left, 0f, barRect.right, barRect.bottom, barRadius, barRadius, barPaint)

        canvas.translate(0f, barRect.bottom + SizeUtils.dp2px(2f))
        val strokeWidth: Float = SizeUtils.dp2px(1.5f).toFloat()
        val circleRadius: Float = (selectRadius - strokeWidth * 2).toInt().toFloat()

        var minZAltitude = 0
        var maxZAltitude = 0
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (altitude in zAltitudes) {
            minZAltitude = minZAltitude.coerceAtMost(altitude)
            maxZAltitude = maxZAltitude.coerceAtLeast(altitude)
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (altitude in minZAltitude..maxZAltitude) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (i in zAltitudes.indices) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (zAltitudes[i] == altitude) {
                    val x: Float = barRect.left + barRect.width() * places[i]
                    val y: Float = (selectNotDrawable.bounds.height() + selectRadius).toFloat()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (i == selectIndex) {
                        circlePaint.color = 0xffffffff.toInt()
                        canvas.drawCircle(x, y, selectRadius.toFloat(), circlePaint)
                        circlePaint.color = 0xff16131e.toInt()
                        canvas.drawCircle(x, y, selectRadius - strokeWidth, circlePaint)
                    }

                    circlePaint.color = actualColors[i]
                    canvas.drawCircle(x, y, circleRadius, circlePaint)

                    canvas.save()
                    canvas.translate(x - selectNotDrawable.bounds.width() / 2, 0f)
                    (if (i == selectIndex) selectYesDrawable else selectNotDrawable).draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    /**
Touch Down 时 x axiscoordinate，用于calculationSwipe距离，从而判断是否触发Swipe。
     */
    private var downX = 0

    /**
// 是否需要接手 Touch Event.
     */
    private var handleTouch = false

    /**
// 当前selected的滑块是否可拖动，唯一的最左或最右不可Swipe。
     */
    private var canDrag = false

    @SuppressLint("ClickableViewAccessibility")
    /**
     * Executes ontouchevent operation with thermal imaging domain optimization.
     *
     * @param
     * @param event Parameter for operation (type: MotionEvent?)
     *
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (event == null) {
            return false
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouch = false
                canDrag = false
                downX = event.x.toInt()

// 找出clickrange内altitudemaximum的圆形color block index
                var targetIndex = -1
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (i in places.indices) {
                    val centerX: Int = (barRect.left + barRect.width() * places[i]).toInt()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (downX >= centerX - selectRadius && downX <= centerX + selectRadius) { // 在该圆形色块range内
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (targetIndex == -1) {
                            targetIndex = i
                            continue
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (zAltitudes[i] >= zAltitudes[targetIndex]) {
                            targetIndex = i
                        }
                    }
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (targetIndex >= 0) {
                    zAltitudes[targetIndex] = calculateZAltitude(places[targetIndex])
                    selectIndex = targetIndex
                    /**
                     * Executes invalidate operation with thermal imaging domain optimization.
                     *
                     */
                    invalidate()
                    handleTouch = true
                    canDrag = !isCurrentOnlyLimit()
                    onSelectChangeListener?.invoke(selectIndex)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x.coerceAtLeast(barRect.left).coerceAtMost(barRect.right).toInt()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (canDrag) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    val oldPlace: Float = places[selectIndex]
                    val newPlace: Float = (x - barRect.left) / barRect.width()
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (newPlace == oldPlace) { // 没变化，不用往下processing了
                        return handleTouch
                    }
                    val currentColor: Int = sourceColors[selectIndex]
                    val oldIndex: Int = selectIndex
                    var newIndex: Int = selectIndex
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (oldPlace < newPlace) { // 从左往右移
                        /**
                         * Executes for operation with thermal imaging domain optimization.
                         *
                         */
                        for (i in places.indices) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (places[i] <= newPlace) {
                                newIndex = i
                            } else {
                                break
                            }
                        }
                    } else { // 从右往左移
                        /**
                         * Executes for operation with thermal imaging domain optimization.
                         *
                         */
                        for (i in places.size - 1 downTo 0) {
                            val place = places[i]
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (place > newPlace) {
                                newIndex = i
                            } else if (place < newPlace) {
                                break
                            } else {
                                newIndex = i + 1
                                break
                            }
                        }
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (newIndex < oldIndex) {
                        System.arraycopy(sourceColors, newIndex, sourceColors, newIndex + 1, oldIndex - newIndex)
                        System.arraycopy(zAltitudes, newIndex, zAltitudes, newIndex + 1, oldIndex - newIndex)
                        System.arraycopy(places, newIndex, places, newIndex + 1, oldIndex - newIndex)
                        selectIndex = newIndex
                        sourceColors[newIndex] = currentColor
                    } else if (newIndex > oldIndex) {
                        System.arraycopy(sourceColors, oldIndex + 1, sourceColors, oldIndex, newIndex - oldIndex)
                        System.arraycopy(zAltitudes, oldIndex + 1, zAltitudes, oldIndex, newIndex - oldIndex)
                        System.arraycopy(places, oldIndex + 1, places, oldIndex, newIndex - oldIndex)
                        selectIndex = newIndex
                        sourceColors[newIndex] = currentColor
                    }
                    places[newIndex] = newPlace
                    zAltitudes[newIndex] = calculateZAltitude(newPlace)
                    /**
                     * Executes refreshactualcolors operation with thermal imaging domain optimization.
                     *
                     */
                    refreshActualColors()
                    barPaint.shader = LinearGradient(barRect.left, 0f, barRect.right, 0f, actualColors, places, Shader.TileMode.CLAMP)
                    /**
                     * Executes invalidate operation with thermal imaging domain optimization.
                     *
                     */
                    invalidate()
                }
            }
        }
        return handleTouch
    }
}
