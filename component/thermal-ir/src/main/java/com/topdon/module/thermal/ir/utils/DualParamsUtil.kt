package com.topdon.module.thermal.ir.utils

import com.energy.iruvc.utils.DualCameraParams
import com.topdon.lib.core.common.SaveSettingUtil

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for DualParamsUtil operations.
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
object DualParamsUtil {
    /**
     * Executes wifiFusionTypeToParams functionality.
     */
    /**
     * Executes wififusiontypetoparams operation with thermal imaging domain optimization.
     *
     * @param
     * @param fusionType Parameter for operation (type: Int)
     *
     */
    fun wifiFusionTypeToParams(fusionType: Int): Int  {
        return when (fusionType) {
            SaveSettingUtil.FusionTypeVLOnly -> 3
            SaveSettingUtil.FusionTypeIROnlyNoFusion -> 1
            SaveSettingUtil.FusionTypeMeanFusion -> 4
            SaveSettingUtil.FusionTypeIROnly -> 0
            else -> 3
        }
    }

    /**
     * Executes fusionTypeToParams functionality.
     */
    /**
     * Executes fusiontypetoparams operation with thermal imaging domain optimization.
     *
     * @param
     * @param fusionType Parameter for operation (type: Int)
     *
     */
    fun fusionTypeToParams(fusionType: Int): DualCameraParams.FusionType {
        return when (fusionType) {
            SaveSettingUtil.FusionTypeVLOnly -> DualCameraParams.FusionType.VLOnly
            SaveSettingUtil.FusionTypeIROnlyNoFusion -> DualCameraParams.FusionType.IROnlyNoFusion
            SaveSettingUtil.FusionTypeScreenFusion -> DualCameraParams.FusionType.ScreenFusion
            SaveSettingUtil.FusionTypeHSLFusion -> DualCameraParams.FusionType.HSLFusion
            SaveSettingUtil.FusionTypeMeanFusion -> DualCameraParams.FusionType.MeanFusion
            SaveSettingUtil.FusionTypeLPYFusion -> DualCameraParams.FusionType.LPYFusion
            SaveSettingUtil.FusionTypeIROnly -> DualCameraParams.FusionType.IROnly
            else -> DualCameraParams.FusionType.LPYFusion
        }
    }

    /**
     * Executes paramsToFusionType functionality.
     */
    /**
     * Executes paramstofusiontype operation with thermal imaging domain optimization.
     *
     * @param
     * @param fusionTypeP Parameter for operation (type: DualCameraParams.FusionType)
     *
     */
    fun paramsToFusionType(fusionTypeP: DualCameraParams.FusionType): Int  {
        return when (fusionTypeP) {
            DualCameraParams.FusionType.VLOnly -> SaveSettingUtil.FusionTypeVLOnly
            DualCameraParams.FusionType.IROnlyNoFusion -> SaveSettingUtil.FusionTypeIROnlyNoFusion
            DualCameraParams.FusionType.ScreenFusion -> SaveSettingUtil.FusionTypeScreenFusion
            DualCameraParams.FusionType.HSLFusion -> SaveSettingUtil.FusionTypeHSLFusion
            DualCameraParams.FusionType.MeanFusion -> SaveSettingUtil.FusionTypeMeanFusion
            DualCameraParams.FusionType.LPYFusion -> SaveSettingUtil.FusionTypeLPYFusion
            DualCameraParams.FusionType.IROnly -> SaveSettingUtil.FusionTypeIROnly
            else -> SaveSettingUtil.FusionTypeLPYFusion
        }
    }
}
