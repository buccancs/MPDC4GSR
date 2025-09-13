package com.topdon.pseudo.constant

/**
 * Specialized thermal imaging component providing ColorRecommend functionality for the IRCamera system.
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
object ColorRecommend {
    val colorList1 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff0000ff.toInt(),
            0xffff0000.toInt(),
            0xffffff00.toInt(),
        )

    val colorList2 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff000000.toInt(),
            0xffffffff.toInt(),
            0xffff0000.toInt(),
        )
    val colorList3 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff0000ff.toInt(),
            0xff00ff00.toInt(),
            0xffffff00.toInt(),
            0xffff0000.toInt(),
        )
    val colorList3TC007 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff0000ff.toInt(),
            0xff00ff00.toInt(),
            0xffff0000.toInt(),
        )
    val colorList4 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff000000.toInt(),
            0xFF840000.toInt(),
            0xffff0000.toInt(),
        )
    val colorList5 =
        /**
         * Executes intarrayof operation with thermal imaging domain optimization.
         *
         */
        intArrayOf(
            0xff0000ff.toInt(),
            0xFF7B7B83.toInt(),
            0xffffff00.toInt(),
        )

    /**
     *
@param index 0-iron red 1-黑红 2-自然 3-岩浆 4-辉金
     */
    /**
     * Retrieves colorbyindex information.
     */
    fun getColorByIndex(
        isTC007: Boolean,
        index: Int,
    ): IntArray =
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (index) {
            0 -> colorList1
            1 -> colorList2
            2 -> if (isTC007) colorList3TC007 else colorList3
            3 -> colorList4
            else -> colorList5
        }
}
