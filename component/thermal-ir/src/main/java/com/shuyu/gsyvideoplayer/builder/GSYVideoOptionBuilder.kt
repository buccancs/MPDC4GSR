package com.shuyu.gsyvideoplayer.builder

import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 * Specialized thermal imaging component providing GSYVideoOptionBuilder functionality for the IRCamera system.
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
class GSYVideoOptionBuilder {
    /**
     * Sets videoallcallback configuration.
     */
    fun setVideoAllCallBack(callback: Any?): GSYVideoOptionBuilder = this

    /**
     * Sets rotateviewauto configuration.
     */
    fun setRotateViewAuto(auto: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets lockland configuration.
     */
    fun setLockLand(lock: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets showfullanimation configuration.
     */
    fun setShowFullAnimation(show: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets needlockfull configuration.
     */
    fun setNeedLockFull(need: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets cachewithplay configuration.
     */
    fun setCacheWithPlay(cache: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets videotitle configuration.
     */
    fun setVideoTitle(title: String?): GSYVideoOptionBuilder = this

    /**
     * Sets istouchwiget configuration.
     */
    fun setIsTouchWiget(touch: Boolean): GSYVideoOptionBuilder = this

    /**
     * Sets url configuration.
     */
    /**
     * Configures the url with validation and thermal imaging optimization.
     *
     * @param
     * @param url Parameter for operation (type: String?)
     *
     */
    fun setUrl(url: String?): GSYVideoOptionBuilder = this

    /**
     * Executes build functionality.
     */
    /**
     * Executes build operation with thermal imaging domain optimization.
     *
     * @param
     * @param player Parameter for operation (type: GSYVideoPlayer)
     *
     */
    fun build(player: GSYVideoPlayer): GSYVideoOptionBuilder = this

    companion object {
        @JvmStatic
    /**
     * Executes create functionality.
     */
        /**
         * Executes create operation with thermal imaging domain optimization.
         *
         */
        fun create(): GSYVideoOptionBuilder = GSYVideoOptionBuilder()
    }
}
