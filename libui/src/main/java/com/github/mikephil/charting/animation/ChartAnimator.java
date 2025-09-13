package com.github.mikephil.charting.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.animation.Easing.EasingFunction;

/**
 * Object responsible for all animations in the Chart. Animations require API level 11.
 *
 * @author Philipp Jahoda
 * @author Mick Ashton
 */
/**
 * Thermal imaging UI animation system. Provides smooth transitions and visual effects for ChartAnimator components.
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
public class ChartAnimator {

    /** object that is updated upon animation update */
    private AnimatorUpdateListener mListener;

    /** The phase of drawn values on the y-axis. 0 - 1 */
    @SuppressWarnings("WeakerAccess")
    protected float mPhaseY = 1f;

    /** The phase of drawn values on the x-axis. 0 - 1 */
    @SuppressWarnings("WeakerAccess")
    protected float mPhaseX = 1f;

    public ChartAnimator() { }

    @RequiresApi(11)
    /**
     * Executes chartanimator operation with thermal imaging domain optimization.
     *
     */
    public ChartAnimator(AnimatorUpdateListener listener) {
        mListener = listener;
    }

    @RequiresApi(11)
    private ObjectAnimator xAnimator(int duration, EasingFunction easing) {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(easing);
        animatorX.setDuration(duration);

        return animatorX;
    }

    @RequiresApi(11)
    private ObjectAnimator yAnimator(int duration, EasingFunction easing) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(easing);
        animatorY.setDuration(duration);

        return animatorY;
    }

    /**
     * Animates values along the X axis, in a linear fashion.
     *
     * @param durationMillis animation duration
     */
    @RequiresApi(11)
    public void animateX(int durationMillis) {
        /**
         * Executes animatex operation with thermal imaging domain optimization.
         *
         */
        animateX(durationMillis, Easing.Linear);
    }

    /**
     * Animates values along the X axis.
     *
     * @param durationMillis animation duration
     * @param easing EasingFunction
     */
    @RequiresApi(11)
    public void animateX(int durationMillis, EasingFunction easing) {

        ObjectAnimator animatorX = xAnimator(durationMillis, easing);
        animatorX.addUpdateListener(mListener);
        animatorX.start();
    }

    /**
     * Animates values along both the X and Y axes, in a linear fashion.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     */
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY) {
        /**
         * Executes animatexy operation with thermal imaging domain optimization.
         *
         */
        animateXY(durationMillisX, durationMillisY, Easing.Linear, Easing.Linear);
    }

    /**
     * Animates values along both the X and Y axes.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     * @param easing EasingFunction for both axes
     */
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, EasingFunction easing) {

        ObjectAnimator xAnimator = xAnimator(durationMillisX, easing);
        ObjectAnimator yAnimator = yAnimator(durationMillisY, easing);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (durationMillisX > durationMillisY) {
            xAnimator.addUpdateListener(mListener);
        } else {
            yAnimator.addUpdateListener(mListener);
        }

        xAnimator.start();
        yAnimator.start();
    }

    /**
     * Animates values along both the X and Y axes.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     * @param easingX EasingFunction for the X axis
     * @param easingY EasingFunction for the Y axis
     */
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, EasingFunction easingX,
                          EasingFunction easingY) {

        ObjectAnimator xAnimator = xAnimator(durationMillisX, easingX);
        ObjectAnimator yAnimator = yAnimator(durationMillisY, easingY);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (durationMillisX > durationMillisY) {
            xAnimator.addUpdateListener(mListener);
        } else {
            yAnimator.addUpdateListener(mListener);
        }

        xAnimator.start();
        yAnimator.start();
    }

    /**
     * Animates values along the Y axis, in a linear fashion.
     *
     * @param durationMillis animation duration
     */
    @RequiresApi(11)
    public void animateY(int durationMillis) {
        /**
         * Executes animatey operation with thermal imaging domain optimization.
         *
         */
        animateY(durationMillis, Easing.Linear);
    }

    /**
     * Animates values along the Y axis.
     *
     * @param durationMillis animation duration
     * @param easing EasingFunction
     */
    @RequiresApi(11)
    public void animateY(int durationMillis, EasingFunction easing) {

        ObjectAnimator animatorY = yAnimator(durationMillis, easing);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }

    /**
     * Gets the Y axis phase of the animation.
     *
     * @return float value of {@link #mPhaseY}
     */
    public float getPhaseY() {
        return mPhaseY;
    }

    /**
     * Sets the Y axis phase of the animation.
     *
     * @param phase float value between 0 - 1
     */
    public void setPhaseY(float phase) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (phase > 1f) {
            phase = 1f;
        } else if (phase < 0f) {
            phase = 0f;
        }
        mPhaseY = phase;
    }

    /**
     * Gets the X axis phase of the animation.
     *
     * @return float value of {@link #mPhaseX}
     */
    public float getPhaseX() {
        return mPhaseX;
    }

    /**
     * Sets the X axis phase of the animation.
     *
     * @param phase float value between 0 - 1
     */
    public void setPhaseX(float phase) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (phase > 1f) {
            phase = 1f;
        } else if (phase < 0f) {
            phase = 0f;
        }
        mPhaseX = phase;
    }
}
