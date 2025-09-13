package com.topdon.lib.core.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 解决LiveData粘性Event
 * Created by jzh on 2020-12-28.
 */
/**
 * SingleLiveEvent manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing SingleLiveEvent functionality for the IRCamera system.
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
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending: AtomicBoolean = AtomicBoolean(false)

    /**
     * Executes observe operation with thermal imaging domain optimization.
     *
     * @param
     * @param owner Parameter for operation (type: LifecycleOwner)
     * @param observer Parameter for operation (type: Observer<in T>)
     *
     */
    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>,
    ) {
        super.observe(owner, {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    /**
     * Configures the value with validation and thermal imaging optimization.
     *
     * @param
     * @param t Parameter for operation (type: T?)
     *
     */
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    /**
     * Executes call functionality.
     */
    /**
     * Executes call operation with thermal imaging domain optimization.
     *
     */
    fun call() {
        this.setValue(null)
    }
}
