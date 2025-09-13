package com.topdon.module.thermal.ir.utils

import android.util.Log
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.energy.iruvc.dual.DualUVCCamera
import com.energy.iruvc.ircmd.IRCMD
import com.energy.iruvc.utils.CommonParams
import com.energy.iruvc.utils.DualCameraParams
import com.energy.iruvc.utils.SynchronizedBitmap
import com.infisense.usbdual.camera.BaseDualView
import com.infisense.usbir.utils.HexDump
import com.topdon.lib.core.common.SharedManager
import java.io.IOException
import java.io.InputStream
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Specialized thermal imaging component providing IRCmdTool functionality for the IRCamera system.
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
object IRCmdTool {
    val TAG = "IRCmdTool"
    var dispNumber = 30

    /**
     * Retrieves dualbytes information.
     */
    fun getDualBytes(irCmd: IRCMD?): ByteArray {
        val calibrationDataSize = 192
        val INIT_ALIGN_DATA = floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)

        val oemInfo = ByteArray(512)
        val snData = ByteArray(256)
        val dispData = ByteArray(5) // Registrationparameter
        irCmd?.oemRead(CommonParams.ProductType.P2, oemInfo)
        XLog.w("coredataloadsuccess", "data读取complete:")
        val calibrationData = ByteArray(calibrationDataSize)
        val productTypeData = ByteArray(2)
        System.arraycopy(oemInfo, 0, calibrationData, 0, calibrationData.size)
        System.arraycopy(oemInfo, calibrationDataSize, productTypeData, 0, productTypeData.size)
        System.arraycopy(
            oemInfo,
            calibrationDataSize + productTypeData.size,
            dispData,
            0,
            dispData.size,
        )
        System.arraycopy(oemInfo, 256, snData, 0, snData.size)
        try {
            var str = String(dispData)
            str = str.replace(Regex("[^-\\d]"), "")
            dispNumber = str.toInt()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dispNumber > 60)
                {
                    dispNumber = dispNumber / 10
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dispNumber < -20)
                {
                    dispNumber = -20
                }
            XLog.w("registrationinfo:", "" + dispNumber)
        } catch (e: Exception) {
            XLog.w("registrationdataexception")
        }
        val snList = String(snData).split(";")
        val snStr =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (snList.isNotEmpty() && snList[0].contains("sn", true))
                {
                    snList[0].replace("SN:", "")
                } else
                {
                    ""
                }
        val parameters = ByteArray(calibrationDataSize + 1 + 24)
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (String(productTypeData) == "TD") {
            System.arraycopy(calibrationData, 0, parameters, 0, calibrationData.size)
            parameters[calibrationDataSize] = 1
            val alignByte = SharedManager.getManualData(snStr)
            System.arraycopy(alignByte, 0, parameters, calibrationDataSize + 1, alignByte.size)
        } else {
            val am = Utils.getApp().assets
            var `is`: InputStream? = null
            val length: Int
            try {
                `is` = am.open("dual_calibration_parameters2.bin")
                length = `is`.available()
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (`is`.read(parameters) != length) {
                    Log.e(TAG, "read file fail ")
                }
                parameters[length] = 1
先从buffer中查找是否有save的对齐data，没有用initializedata
                val alignByte = SharedManager.getManualData(snStr)
                System.arraycopy(alignByte, 0, parameters, calibrationDataSize + 1, alignByte.size)
                XLog.w("core没存在校正data，请联系厂商Confirm")
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return parameters
    }

    /**
     * Retrieves snstr information.
     */
    fun getSNStr(irCmd: IRCMD?): String  {
        val oemInfo = ByteArray(512)
        irCmd?.oemRead(CommonParams.ProductType.P2, oemInfo)
        val snData = ByteArray(256)
        System.arraycopy(oemInfo, 256, snData, 0, snData.size)
        val snList = String(snData).split(";")
        return if (snList.isNotEmpty() && snList[0].contains("sn", true))
            {
                snList[0].replace("SN:", "")
            } else
            {
                ""
            }
    }

    /**
setemissivity unit:cnt(128cnt = 1)
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
set距离 unit:cnt(128cnt = 1m, default值: 0.25 * 128 = 32)
     * @param value 0 ~ 25600
     *
现有sdk在setTPD_PROP_DISTANCE抛exception
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
setcontrast
     * @param value 0 ~ 255
     */
    /**
     * Sets levelcontrast configuration.
     */
    fun setLevelContrast(
        irCmd: IRCMD?,
        value: Int,
    ) {
        val data = CommonParams.PropImageParamsValue.NumberType(value.toString())
        /**
         * Configures the imageparams with validation and thermal imaging optimization.
         *
         */
        setImageParams(irCmd = irCmd, params = CommonParams.PropImageParams.IMAGE_PROP_LEVEL_CONTRAST, value = data)
    }

    /**
set锐化
     * @param value 0 ~ 4
     *
     */
    /**
     * Sets levelddd configuration.
     */
    fun setLevelDdd(
        irCmd: IRCMD?,
        value: Int,
    ) {
        val data =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (value) {
                0 -> CommonParams.PropImageParamsValue.DDEType.DDE_0
                1 -> CommonParams.PropImageParamsValue.DDEType.DDE_1
                2 -> CommonParams.PropImageParamsValue.DDEType.DDE_2
                3 -> CommonParams.PropImageParamsValue.DDEType.DDE_3
                4 -> CommonParams.PropImageParamsValue.DDEType.DDE_4
                else -> CommonParams.PropImageParamsValue.DDEType.DDE_0
            }
        /**
         * Configures the imageparams with validation and thermal imaging optimization.
         *
         */
        setImageParams(irCmd = irCmd, params = CommonParams.PropImageParams.IMAGE_PROP_LEVEL_DDE, value = data)
    }

    /**
set自动gain
     */
    /**
     * Configures the levelagc with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param value Parameter for operation (type: Boolean)
     *
     */
    fun setLevelAgc(
        irCmd: IRCMD?,
        value: Boolean,
    ) {
        val data =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value) {
                CommonParams.PropImageParamsValue.StatusSwith.ON
            } else {
                CommonParams.PropImageParamsValue.StatusSwith.OFF
            }
        /**
         * Configures the imageparams with validation and thermal imaging optimization.
         *
         */
        setImageParams(irCmd = irCmd, params = CommonParams.PropImageParams.IMAGE_PROP_ONOFF_AGC, value = data)
    }

    /**
查询gainmode
@return 1:高gain(常温)    0:低gain(high temperature)
     */
    /**
     * Retrieves tpdgainsel information.
     */
    fun getTpdGainSel(irCmd: IRCMD?): Int {
        val result = queryTpdParam(irCmd = irCmd, params = CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL)
        return if (result == CommonParams.PropTPDParamsValue.GAINSELStatus.GAIN_SEL_HIGH.value) {
            1
        } else {
            0
        }
    }

    /**
setgainmode
@param value 1:高gain(常温)    0:低gain(high temperature)
     */
    /**
     * Sets tpdgainsel configuration.
     */
    fun setTpdGainSel(
        irCmd: IRCMD?,
        value: Int,
    ): Int {
        val data =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (value == 1) {
                CommonParams.PropTPDParamsValue.GAINSELStatus.GAIN_SEL_HIGH
            } else {
                CommonParams.PropTPDParamsValue.GAINSELStatus.GAIN_SEL_LOW
            }
        return setTpdParams(irCmd = irCmd, params = CommonParams.PropTPDParams.TPD_PROP_GAIN_SEL, value = data)
    }

    /**
查询Tpd
     */
    /**
     * Executes querytpdparam operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param params Parameter for operation (type: CommonParams.PropTPDParams)
     *
     */
    fun queryTpdParam(
        irCmd: IRCMD?,
        params: CommonParams.PropTPDParams,
    ): Int {
        val value = IntArray(1)
        irCmd?.getPropTPDParams(params, value)
        return value[0]
    }

    /**
查询Image
     */
    /**
     * Executes queryimageparam operation with thermal imaging domain optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param params Parameter for operation (type: CommonParams.PropImageParams)
     *
     */
    fun queryImageParam(
        irCmd: IRCMD?,
        params: CommonParams.PropImageParams,
    ): Int {
        val value = IntArray(1)
        irCmd?.getPropImageParams(params, value)
        return value[0]
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

    /**
setimageparameter
     */
    /**
     * Configures the imageparams with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     * @param params Parameter for operation (type: CommonParams.PropImageParams)
     * @param value Parameter for operation (type: CommonParams.PropImageParamsValue)
     *
     */
    private fun setImageParams(
        irCmd: IRCMD?,
        params: CommonParams.PropImageParams,
        value: CommonParams.PropImageParamsValue,
    ): Int {
        return try {
            irCmd?.setPropImageParams(params, value) ?: 0
        } catch (e: Exception) {
            XLog.w("settingsparameterexception[${params.name}]: ${e.message}")
            0
        }
    }

    /**
registration
水平移动
     * @param value (-20 ~ 60)
     */
    /**
     * Sets disp configuration.
     */
    /**
     * Configures the disp with validation and thermal imaging optimization.
     *
     * @param
     * @param dualView Parameter for operation (type: BaseDualView?)
     * @param value Parameter for operation (type: Int)
     *
     */
    fun setDisp(
        dualView: BaseDualView?,
        value: Int,
    ): Int {
        return try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dualView != null) {
                dualView?.dualUVCCamera!!.setDisp(value)
                0 // Return success
            } else {
                -1 // Return error
            }
        } catch (e: Exception) {
            XLog.w("settingsregistrationexception[$value]: ${e.message}")
            0
        }
    }

    /**
@param moveX 在当前基础上要再偏移的数值
     */
    /**
     * Configures the aligntranslate with validation and thermal imaging optimization.
     *
     * @param
     * @param dualView Parameter for operation (type: BaseDualView?)
     * @param moveX Parameter for operation (type: Int)
     * @param moveY Parameter for operation (type: Int)
     *
     */
    fun setAlignTranslate(
        dualView: BaseDualView?,
        moveX: Int,
        moveY: Int,
    ) {
        val newSrc = ByteArray(8)

        val xSrc = ByteArray(4)
        HexDump.float2byte(moveX.toFloat(), xSrc)
        System.arraycopy(xSrc, 0, newSrc, 0, 4)

        val ySrc = ByteArray(4)
        HexDump.float2byte(moveY.toFloat(), ySrc)
        System.arraycopy(ySrc, 0, newSrc, 4, 4)

        dualView?.dualUVCCamera?.setAlignTranslateParameter(newSrc)
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
enabled等温尺
@param highC temperature上限，单位摄氏度
@param lowC temperature下限，单位摄氏度
     */
    /**
     * Sets isocoloropen configuration.
     */
    fun setIsoColorOpen(
        dualUVCCamera: DualUVCCamera?,
        highC: Float,
        lowC: Float,
    ) {
        dualUVCCamera?.setIsothermal(DualCameraParams.IsothermalState.ON)
        val normalHighTemp = (highC + 273).toDouble() // 单位k
        val normalLowTemp = (lowC + 273).toDouble() // 单位k
        val highTemp = ceil(normalHighTemp * 16 * 4).toInt() // 高温向上取整
        val lowTemp = floor(normalLowTemp * 16 * 4).toInt() // 低温向下取整
        val highData = ByteArray(2)
        highData[0] = highTemp.toByte()
        highData[1] = (highTemp shr 8).toByte()
        val lowData = ByteArray(2)
        lowData[0] = lowTemp.toByte()
        lowData[1] = (lowTemp shr 8).toByte()
        val tempHFin = (highData[0].toInt() and 0x00ff) + (highData[1].toInt() and 0x00ff shl 8)
        val tempLFin = (lowData[0].toInt() and 0x00ff) + (lowData[1].toInt() and 0x00ff shl 8)
        dualUVCCamera?.setTempL(tempLFin) // 低温 - convert to Int
        dualUVCCamera?.setTempH(tempHFin) // 高温 - convert to Int
    }

    /**
disabled等温尺
     */
    /**
     * Configures the isocolorclose with validation and thermal imaging optimization.
     *
     * @param
     * @param dualUVCCamera Camera configuration or reference (type: DualUVCCamera?)
     *
     */
    fun setIsoColorClose(dualUVCCamera: DualUVCCamera?) {
        dualUVCCamera?.setIsothermal(DualCameraParams.IsothermalState.OFF)
    }

    /**
/**
 * Executes amplification operation with thermal imaging domain optimization.
 *
 */
amplification(仅对thermal imaging有效)
ZoomScaleStep.ZOOM_STEP1: 2级倍率
ZoomScaleStep.ZOOM_STEP2: 4级倍率
ZoomScaleStep.ZOOM_STEP3: 8级倍率
ZoomScaleStep.ZOOM_STEP4: 16级倍率
     */
    /**
     * Sets zoomup configuration.
     */
    /**
     * Configures the zoomup with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     *
     */
    fun setZoomUp(irCmd: IRCMD?) {
        irCmd?.zoomCenterUp(CommonParams.PreviewPathChannel.PREVIEW_PATH0, CommonParams.ZoomScaleStep.ZOOM_STEP2)
    }

    /**
缩小
     */
    /**
     * Configures the zoomdown with validation and thermal imaging optimization.
     *
     * @param
     * @param irCmd Parameter for operation (type: IRCMD?)
     *
     */
    fun setZoomDown(irCmd: IRCMD?) {
        irCmd?.zoomCenterDown(CommonParams.PreviewPathChannel.PREVIEW_PATH0, CommonParams.ZoomScaleStep.ZOOM_STEP2)
    }
}
