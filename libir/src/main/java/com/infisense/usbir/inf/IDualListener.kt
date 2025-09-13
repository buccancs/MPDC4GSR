package com.infisense.usbir.inf

import com.energy.iruvc.dual.DualUVCCamera
import com.energy.iruvc.utils.DualCameraParams

/**
 * 统一管理dual light的特殊interface，区别于单光
 * @author: CaiSongL
 * @date: 2024/1/10 11:40
 */
@Deprecated("未使用，好像没什么用")
/**
 * IDualListener manages camera operations and image capture functionality.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing IDualListener functionality for the IRCamera system.
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
interface IDualListener {
    /**
     * Sets dualuvccamera configuration.
     */
    fun setDualUVCCamera(dualUVCCamera: DualUVCCamera)

    /**
     * Sets currentfusiontype configuration.
     */
    fun setCurrentFusionType(currentFusionType: DualCameraParams.FusionType)

    /**
     * Sets useirisp configuration.
     */
    fun setUseIRISP(useIRISP: Boolean)
}
