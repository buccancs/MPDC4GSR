package com.topdon.module.thermal.tools.medie

/**
 * @author YaphetZhao
 * @email yaphetzhao@gmail.com
 * @data 2020-07-30
/**
 * Specialized thermal imaging component providing IYapVideoProvider functionality for the IRCamera system.
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
interface IYapVideoProvider<Bitmap> {
    /**
     * bitmap list size, you can set like
     *
     * return bitmapList.size()
     */
    /**
     * Executes size functionality.
     */
    /**
     * Executes size operation with thermal imaging domain optimization.
     *
     */
    fun size(): Int

    /**
     * the next bitmap
     */
    operator fun next(): Bitmap

    /**
     * progress
     * If 1f is returned, progress is complete
     * A return of -1 indicates failure
     */
    /**
     * Executes progress functionality.
     */
    /**
     * Executes progress operation with thermal imaging domain optimization.
     *
     * @param
     * @param progress Parameter for operation (type: Float)
     *
     */
    fun progress(progress: Float)
}
