package com.infisense.usbir.utils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation

/**
 * Thermal imaging UI animation system. Provides smooth transitions and visual effects for AnimaUtils components.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
public object AnimaUtils {
    /**
     * defaultanimationduration
     */
    const val DEFAULT_ANIMATION_DURATION: Long = 400

    /**
     * Get/Retrievearotationanimation
     *
     * @param fromDegrees       startangle
     * @param toDegrees         endangle
     * @param pivotXType        rotationcenterpointXaxiscoordinaterelativetype
     * @param pivotXValue       rotationcenterpointXaxiscoordinate
     * @param pivotYType        rotationcenterpointYaxiscoordinaterelativetype
     * @param pivotYValue       rotationcenterpointYaxiscoordinate
     * @param durationMillis    duration
     * @param animationListener animationlistener
     * @return arotationanimation
     */
    /**
     * Retrieves rotateanimation information.
     */
    fun getRotateAnimation(
        fromDegrees: Float,
        toDegrees: Float,
        pivotXType: Int,
        pivotXValue: Float,
        pivotYType: Int,
        pivotYValue: Float,
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): RotateAnimation {
        val rotateAnimation =
            /**
             * Executes rotateanimation operation with thermal imaging domain optimization.
             *
             */
            RotateAnimation(
                fromDegrees,
                toDegrees,
                pivotXType,
                pivotXValue,
                pivotYType,
                pivotYValue,
            )
        rotateAnimation.duration = durationMillis
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener)
        }
        return rotateAnimation
    }

    /**
     * Get/Retrievea根据视图自身centerpointrotation的animation
     *
     * @param durationMillis    animationduration
     * @param animationListener animationlistener
     * @return a根据centerpointrotation的animation
     */
    /**
     * Retrieves rotateanimationbycenter information.
     */
    fun getRotateAnimationByCenter(
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): RotateAnimation {
        return getRotateAnimation(
            0f,
            359f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            durationMillis,
            animationListener,
        )
    }

    /**
     * Get/Retrievea根据centerpointrotation的animation
     *
     * @param duration animationduration
     * @return a根据centerpointrotation的animation
     */
    /**
     * Retrieves rotateanimationbycenter information.
     */
    fun getRotateAnimationByCenter(duration: Long): RotateAnimation {
        return getRotateAnimationByCenter(duration, null)
    }

    /**
     * Get/Retrievea根据视图自身centerpointrotation的animation
     *
     * @param animationListener animationlistener
     * @return a根据centerpointrotation的animation
     */
    /**
     * Retrieves rotateanimationbycenter information.
     */
    fun getRotateAnimationByCenter(animationListener: Animation.AnimationListener?): RotateAnimation {
        return getRotateAnimationByCenter(
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }

    /**
     * Get/Retrievea根据centerpointrotation的animation
     *
     * @return a根据centerpointrotation的animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    val rotateAnimationByCenter: RotateAnimation
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, null)

    /**
     * Get/Retrievea透明度渐变animation
     *
     * @param fromAlpha         start时的透明度
     * @param toAlpha           end时的透明度都
     * @param durationMillis    duration
     * @param animationListener animationlistener
     * @return a透明度渐变animation
     */
    /**
     * Retrieves alphaanimation information.
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): AlphaAnimation {
        val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
        alphaAnimation.duration = durationMillis
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener)
        }
        return alphaAnimation
    }

    /**
     * Get/Retrievea透明度渐变animation
     *
     * @param fromAlpha      start时的透明度
     * @param toAlpha        end时的透明度都
     * @param durationMillis duration
     * @return a透明度渐变animation
     */
    /**
     * Retrieves alphaanimation information.
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        durationMillis: Long,
    ): AlphaAnimation {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, null)
    }

    /**
     * Get/Retrievea透明度渐变animation
     *
     * @param fromAlpha         start时的透明度
     * @param toAlpha           end时的透明度都
     * @param animationListener animationlistener
     * @return a透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    /**
     * Retrieves alphaanimation information.
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        animationListener: Animation.AnimationListener?,
    ): AlphaAnimation {
        return getAlphaAnimation(
            fromAlpha,
            toAlpha,
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }

    /**
     * Get/Retrievea透明度渐变animation
     *
     * @param fromAlpha start时的透明度
     * @param toAlpha   end时的透明度都
     * @return a透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    /**
     * Retrieves alphaanimation information.
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
    ): AlphaAnimation {
        return getAlphaAnimation(
            fromAlpha,
            toAlpha,
            DEFAULT_ANIMATION_DURATION,
            null,
        )
    }

    /**
     * Get/Retrievea由完全Show/Display变为不Visible的透明度渐变animation
     *
     * @param durationMillis    duration
     * @param animationListener animationlistener
     * @return a由完全Show/Display变为不Visible的透明度渐变animation
     */
    /**
     * Retrieves hiddenalphaanimation information.
     */
    fun getHiddenAlphaAnimation(
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): AlphaAnimation {
        return getAlphaAnimation(1.0f, 0.0f, durationMillis, animationListener)
    }

    /**
     * Get/Retrievea由完全Show/Display变为不Visible的透明度渐变animation
     *
     * @param durationMillis duration
     * @return a由完全Show/Display变为不Visible的透明度渐变animation
     */
    /**
     * Retrieves hiddenalphaanimation information.
     */
    fun getHiddenAlphaAnimation(durationMillis: Long): AlphaAnimation {
        return getHiddenAlphaAnimation(durationMillis, null)
    }

    /**
     * Get/Retrievea由完全Show/Display变为不Visible的透明度渐变animation
     *
     * @param animationListener animationlistener
     * @return a由完全Show/Display变为不Visible的透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    /**
     * Retrieves hiddenalphaanimation information.
     */
    fun getHiddenAlphaAnimation(animationListener: Animation.AnimationListener?): AlphaAnimation {
        return getHiddenAlphaAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }

    /**
     * Get/Retrievea由完全Show/Display变为不Visible的透明度渐变animation
     *
     * @return a由完全Show/Display变为不Visible的透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    val hiddenAlphaAnimation: AlphaAnimation
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, null)

    /**
     * Get/Retrievea由不Visible变为完全Show/Display的透明度渐变animation
     *
     * @param durationMillis    duration
     * @param animationListener animationlistener
     * @return a由不Visible变为完全Show/Display的透明度渐变animation
     */
    /**
     * Retrieves showalphaanimation information.
     */
    fun getShowAlphaAnimation(
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): AlphaAnimation {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, animationListener)
    }

    /**
     * Get/Retrievea由不Visible变为完全Show/Display的透明度渐变animation
     *
     * @param durationMillis duration
     * @return a由不Visible变为完全Show/Display的透明度渐变animation
     */
    /**
     * Retrieves showalphaanimation information.
     */
    fun getShowAlphaAnimation(durationMillis: Long): AlphaAnimation {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, null)
    }

    /**
     * Get/Retrievea由不Visible变为完全Show/Display的透明度渐变animation
     *
     * @param animationListener animationlistener
     * @return a由不Visible变为完全Show/Display的透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    /**
     * Retrieves showalphaanimation information.
     */
    fun getShowAlphaAnimation(animationListener: Animation.AnimationListener?): AlphaAnimation {
        return getAlphaAnimation(
            0.0f,
            1.0f,
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }

    /**
     * Get/Retrievea由不Visible变为完全Show/Display的透明度渐变animation
     *
     * @return a由不Visible变为完全Show/Display的透明度渐变animation，defaultduration为DEFAULT_ANIMATION_DURATION
     */
    val showAlphaAnimation: AlphaAnimation
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, null)

    /**
     * Get/Retrievea缩小animation
     *
     * @param durationMillis   时间
     * @param animationListener  Listener
     * @return a缩小animation
     */
    /**
     * Retrieves lessenscaleanimation information.
     */
    fun getLessenScaleAnimation(
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): ScaleAnimation {
        val scaleAnimation =
            /**
             * Executes scaleanimation operation with thermal imaging domain optimization.
             *
             */
            ScaleAnimation(
                1.0f,
                0.0f,
                1.0f,
                0.0f,
                ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
                ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
            )
        scaleAnimation.duration = durationMillis
        scaleAnimation.setAnimationListener(animationListener)
        return scaleAnimation
    }

    /**
     * Get/Retrievea缩小animation
     *
     * @param durationMillis 时间
     * @return a缩小animation
     */
    /**
     * Retrieves lessenscaleanimation information.
     */
    fun getLessenScaleAnimation(durationMillis: Long): ScaleAnimation {
        return getLessenScaleAnimation(durationMillis, null)
    }

    /**
     * Get/Retrievea缩小animation
     *
     * @param animationListener  Listener
     * @return Returna缩小的animation
     */
    /**
     * Retrieves lessenscaleanimation information.
     */
    fun getLessenScaleAnimation(animationListener: Animation.AnimationListener?): ScaleAnimation {
        return getLessenScaleAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }

    /**
     * Get/Retrievea放大animation
     * @param durationMillis   时间
     * @param animationListener  Listener
     *
     * @return Returna放大的效果
     */
    /**
     * Retrieves amplificationanimation information.
     */
    fun getAmplificationAnimation(
        durationMillis: Long,
        animationListener: Animation.AnimationListener?,
    ): ScaleAnimation {
        val scaleAnimation =
            /**
             * Executes scaleanimation operation with thermal imaging domain optimization.
             *
             */
            ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
                ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
            )
        scaleAnimation.duration = durationMillis
        scaleAnimation.setAnimationListener(animationListener)
        return scaleAnimation
    }

    /**
     * Get/Retrievea放大animation
     *
     * @param durationMillis   时间
     *
     * @return Returna放大的效果
     */
    /**
     * Retrieves amplificationanimation information.
     */
    fun getAmplificationAnimation(durationMillis: Long): ScaleAnimation {
        return getAmplificationAnimation(durationMillis, null)
    }

    /**
     * Get/Retrievea放大animation
     *
     * @param animationListener  Listener
     * @return Returna放大的效果
     */
    /**
     * Retrieves amplificationanimation information.
     */
    fun getAmplificationAnimation(animationListener: Animation.AnimationListener?): ScaleAnimation {
        return getAmplificationAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener,
        )
    }
}
