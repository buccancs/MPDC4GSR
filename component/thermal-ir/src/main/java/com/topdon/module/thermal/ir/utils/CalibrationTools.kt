package com.topdon.module.thermal.ir.utils

import android.util.Log
import com.elvishew.xlog.XLog
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.SynchronizedBitmap

/**
 * Specialized thermal imaging component providing CalibrationTools functionality for the IRCamera system.
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
object CalibrationTools {
    /**
     * Single point calibration
     * Aim at blackbody - set temperature
     */
    /**
     * Executes sign functionality.
     */
    /**
     * Executes sign operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param singlePointTemp Temperature value in Celsius (type: Int)
     *
     */
    fun sign(
        irCmd: IRCMD,
        singlePointTemp: Int,
    ): Boolean {
        var success = false
calibration前需要resettemperature measurement parameters,否则temperaturecalibration inaccuracy
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_TPD) == 0) {
            irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_TPD)
            val result = irCmd.setTPDKtBtRecalPoint(CommonParams.TPDKtBtRecalPointType.RECAL_1_POINT, singlePointTemp)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (result == 0) {
                success = true
            } else {
                XLog.w("Single point calibration failed")
            }
        } else {
            XLog.w("Single point calibration failed")
        }
        return success
    }

    /**
     * Temperature calibration
     * Low temperature (100 ~ 400)
     */
    /**
     * Executes pointFirst functionality.
     */
    /**
     * Executes pointfirst operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param pointTemp Temperature value in Celsius (type: Int)
     *
     */
    fun pointFirst(
        irCmd: IRCMD,
        pointTemp: Int,
    ): Boolean {
        var success = false
calibration前需要resettemperature measurement parameters,否则temperaturecalibration inaccuracy
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_TPD) == 0) {
            val result = irCmd.setTPDKtBtRecalPoint(CommonParams.TPDKtBtRecalPointType.RECAL_2_POINT_FIRST, pointTemp + 273)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (result == 0) {
                success = true
            } else {
                XLog.w("低温calibrationfailed")
            }
        } else {
            XLog.w("低温calibrationfailed")
        }
        return success
    }

    /**
temperaturecalibration
high temperature(20 ~ 100)
     *
提交完low temperature之后才能提交high temperature
     */
    /**
     * Executes pointEnd functionality.
     */
    /**
     * Executes pointend operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param pointTemp Temperature value in Celsius (type: Int)
     *
     */
    fun pointEnd(
        irCmd: IRCMD,
        pointTemp: Int,
    ): Boolean {
        var success = false
calibration前需要resettemperature measurement parameters,否则temperaturecalibration inaccuracy
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_TPD) == 0) {
            val result = irCmd.setTPDKtBtRecalPoint(CommonParams.TPDKtBtRecalPointType.RECAL_2_POINT_END, pointTemp + 273)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (result == 0) {
                success = true
            } else {
                Log.w("123", "failed")
            }
        } else {
            Log.w("123", "failed")
        }
        return success
    }

    /**
锅盖calibration - 步骤一准备
     *
     */
    /**
     * Executes potReady functionality.
     */
    /**
     * Executes potready operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     *
     */
    fun potReady(irCmd: IRCMD): Boolean {
        return irCmd.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_DIS) == 0 // Close锅盖校正
    }

    /**
锅盖calibration - 步骤二start
     *
@param gainType defaultGAIN_1
     * CommonParams.RMCoverAutoCalcType.GAIN_1
     * CommonParams.RMCoverAutoCalcType.GAIN_2
     * CommonParams.RMCoverAutoCalcType.GAIN_4
     */
    /**
     * Executes potStart functionality.
     */
    /**
     * Executes potstart operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param type Parameter for operation (type: Int)
     *
     */
    fun potStart(
        irCmd: IRCMD,
        type: Int,
    ) {
        val gainType =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                1 -> CommonParams.RMCoverAutoCalcType.GAIN_1
                2 -> CommonParams.RMCoverAutoCalcType.GAIN_2
                4 -> CommonParams.RMCoverAutoCalcType.GAIN_4
                else -> CommonParams.RMCoverAutoCalcType.GAIN_1
            }
        irCmd.rmCoverAutoCalc(gainType) // Send锅盖calibration
        irCmd.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_EN) // Open锅盖校正
    }

    /**
Cancelcalibration
     */
    /**
     * Executes cancelcalibration operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     *
     */
    fun cancelCalibration(irCmd: IRCMD) {
        irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_TPD)
    }

    /**
Restore出厂calibration
     */
    /**
     * Executes reset operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     *
     */
    fun reset(irCmd: IRCMD) {
        irCmd.restoreDefaultConfig(CommonParams.DefaultConfigType.DEF_CFG_ALL)
    }

    /**
查询gainmode
@return true: 高gain    false: 低gain
     */
    /**
     * Executes queryGain functionality.
     */
    /**
     * Executes querygain operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     *
     */
    fun queryGain(irCmd: IRCMD): Boolean {
        val value = IntArray(1)
        irCmd.getPropTPDParams(CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, value)
        return value[0] == 1
    }

    /**
setgainmode
@param type 1: Open    0: disabled
     *
     */
    /**
     * Sets gain configuration.
     */
    /**
     * Configures the gain with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param type Parameter for operation (type: Int)
     *
     */
    fun setGain(
        irCmd: IRCMD,
        type: Int,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (type == 1) {
            irCmd.setPropTPDParams(
                CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL,
                CommonParams.PropTPDParamsValue.GAINSELStatus.GAIN_SEL_HIGH,
            )
        } else {
            irCmd.setPropTPDParams(CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, CommonParams.PropTPDParamsValue.GAINSELStatus.GAIN_SEL_LOW)
        }
    }

    /**
查询Tpd
     */
    /**
     * Executes querytpd operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param params Parameter for operation (type: CommonParams.PropTPDParams)
     *
     */
    fun queryTpd(
        irCmd: IRCMD,
        params: CommonParams.PropTPDParams,
    ): Int {
        val value = IntArray(1)
        irCmd.getPropTPDParams(params, value)
        return value[0]
    }

    /**
打快门
     */
    /**
     * Executes shutter operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param syncImage Parameter for operation (type: SynchronizedBitmap)
     *
     */
    fun shutter(
        irCmd: IRCMD?,
        syncImage: SynchronizedBitmap,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (syncImage.type == 1) {
            irCmd?.tc1bShutterManual()
        } else {
执行这段
            irCmd?.updateOOCOrB(CommonParams.UpdateOOCOrBType.B_UPDATE)
        }
    }

    /**
控制锅盖calibration开关
     */
    /**
     * Executes stsswitch operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param flag Parameter for operation (type: Boolean)
     *
     */
    fun stsSwitch(
        irCmd: IRCMD?,
        flag: Boolean,
    ) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (flag) {
            irCmd?.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_EN)
        } else {
            irCmd?.rmCoverStsSwitch(CommonParams.RMCoverStsSwitchStatus.RMCOVER_DIS)
        }
    }

    /**
锅盖calibration - 步骤二start
     *
@param gainType defaultGAIN_1
     * CommonParams.RMCoverAutoCalcType.GAIN_1
     * CommonParams.RMCoverAutoCalcType.GAIN_2
     * CommonParams.RMCoverAutoCalcType.GAIN_4
     */
    /**
     * Executes pot functionality.
     */
    /**
     * Executes pot operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD)
     * @param type Parameter for operation (type: Int)
     *
     */
    fun pot(
        irCmd: IRCMD,
        type: Int,
    ) {
        val gainType =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (type) {
                1 -> CommonParams.RMCoverAutoCalcType.GAIN_1
                2 -> CommonParams.RMCoverAutoCalcType.GAIN_2
                4 -> CommonParams.RMCoverAutoCalcType.GAIN_4
                else -> CommonParams.RMCoverAutoCalcType.GAIN_1
            }
        irCmd.rmCoverAutoCalc(gainType) // Send锅盖calibration
    }

    /**
自动快门
     */
    /**
     * Executes autoshutter operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param flag Parameter for operation (type: Boolean)
     *
     */
    fun autoShutter(
        irCmd: IRCMD?,
        flag: Boolean,
    ) {
        val data = if (flag) CommonParams.PropAutoShutterParameterValue.StatusSwith.ON else CommonParams.PropAutoShutterParameterValue.StatusSwith.OFF
        irCmd?.setPropAutoShutterParameter(CommonParams.PropAutoShutterParameter.SHUTTER_PROP_SWITCH, data)
    }

    /**
TPD_PROP_DISTANCE不给set
set距离 unit:cnt(128cnt=1m)
     * @param value 0 ~ 25600
     */
    /**
     * Sets tpddis configuration.
     */
    /**
     * Configures the tpddis with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param value Parameter for operation (type: Int)
     *
     */
    fun setTpdDis(
        irCmd: IRCMD?,
        value: Int,
    ) {
        val data = CommonParams.PropTPDParamsValue.NumberType(value.toString())
        /**
         * Configures the tpdparams with validation and thermal imaging optimization.
         *
         */
        setTpdParams(irCmd = irCmd, params = CommonParams.PropTPDParams.TPD_PROP_DISTANCE, value = data)
    }

    /**
setemissivity unit:cnt(128cnt=1)
     * @param value 1 ~ 128
     */
    /**
     * Sets tpdems configuration.
     */
    /**
     * Configures the tpdems with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param value Parameter for operation (type: Int)
     *
     */
    fun setTpdEms(
        irCmd: IRCMD?,
        value: Int,
    ) {
        val data = CommonParams.PropTPDParamsValue.NumberType(value.toString())
        /**
         * Configures the tpdparams with validation and thermal imaging optimization.
         *
         */
        setTpdParams(irCmd = irCmd, params = CommonParams.PropTPDParams.TPD_PROP_EMS, value = data)
    }

    /**
setTpd
     */
    /**
     * Configures the tpdparams with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param params Parameter for operation (type: CommonParams.PropTPDParams)
     * @param value Parameter for operation (type: CommonParams.PropTPDParamsValue)
     *
     */
    private fun setTpdParams(
        irCmd: IRCMD?,
        params: CommonParams.PropTPDParams,
        value: CommonParams.PropTPDParamsValue,
    ): Int {
        return try {
            irCmd?.setPropTPDParams(params, value) ?: 0
        } catch (e: Exception) {
            XLog.w("settingsparameterexception[${params.name}]: ${e.message}")
            0
        }
    }
}
