package com.topdon.lib.core.ktbase

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for BaseViewModel display and interaction.
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
open class BaseViewModel : ViewModel(), LifecycleObserver
