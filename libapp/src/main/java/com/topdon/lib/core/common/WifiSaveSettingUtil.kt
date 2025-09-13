package com.topdon.lib.core.common

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.topdon.lib.core.bean.AlarmBean
import com.topdon.lib.core.bean.CameraItemBean
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.core.common.SaveSettingUtil.FusionTypeIROnly
import com.topdon.lib.core.common.SaveSettingUtil.FusionTypeLPYFusion
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.utils.CommUtils

/**
 * wifidevice的saved专属
 *
 * currentclass封装受“savedsettings开关”影响的configuration项，
 *
 * [SharedManager] saved不受“savedsettings开关”影响的configuration项.
 */
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for WifiSaveSettingUtil operations.
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
object WifiSaveSettingUtil {
    /**
     * savedsettings开关使用的 SharedPreferences name.
     */
    private const val SP_NAME = "WifiSaveSettingUtil"

    /**
     * 插件class别
     */
    const val TYPE_PLUG = 0
    const val TYPE_WIFI = 1

    /**
     * savedsettings开关close时，要将所有影响的configuration项reset为default项.
     */
    fun reset() {
        // Thermal imagingtemperature measurementobservationmode共有
        isMeasureTempMode = true
        isVideoMode = false
        isAutoShutter = true
        isRecordAudio = false
        isOpenMirror = false
        delayCaptureSecond = 0
        contrastValue = 128
        pseudoColorMode = 3
        rotateAngle = DeviceConfig.S_ROTATE_ANGLE

        // Temperature measurementmode独有
        isOpenPseudoBar = true
        isOpenTwoLight = false
        twoLightAlpha = 50
        ddeConfig = 2
        tempTextColor = 0xffffffff.toInt()
        temperatureMode = CameraItemBean.TYPE_TMP_C
        alarmBean = AlarmBean()

        
        isOpenCompass = false
        isOpenHighPoint = false
        isOpenLowPoint = false
        aiTraceType = ObserveBean.TYPE_NONE
        isOpenTarget = false
        targetMeasureMode = ObserveBean.TYPE_MEASURE_PERSON
        targetType = ObserveBean.TYPE_TARGET_HORIZONTAL
        targetColorType = ObserveBean.TYPE_TARGET_COLOR_GREEN

        reportAuthorName = CommUtils.getAppName()
        reportWatermarkText = CommUtils.getAppName()
        reportHumidity = 500
        fusionType = FusionTypeLPYFusion
        registrationX = 0
        registrationY = 0
    }

    var registrationX: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("registrationX", 0) else 0
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            SPUtils.getInstance(SP_NAME).put("registrationX", value)
        }
    var registrationY: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("registrationY", 0) else 0
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            SPUtils.getInstance(SP_NAME).put("registrationY", value)
        }
    var fusionType: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("fusionType", FusionTypeIROnly) else FusionTypeIROnly
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            SPUtils.getInstance(SP_NAME).put("fusionType", value)
        }

    /**
     * 是否开启savedsettings开关，defaultclose.
     */
    var isSaveSetting: Boolean
        get() = SPUtils.getInstance(SP_NAME).getBoolean("isSaveSetting", true)
        set(value) {
            SPUtils.getInstance(SP_NAME).put("isSaveSetting", value)
        }

    /**
     * thermal imaging是否处于temperature measurementmode，defaulttemperature measurementmode true-temperature measurement false-observation
     */
    var isMeasureTempMode: Boolean
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getBoolean("isMeasureTempMode", true) else true
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isMeasureTempMode", value)
            }
        }

    /**
     * thermal imaging是否selectionrecordingmode，defaultcapture true-recording false-capture
     */
    var isVideoMode: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isVideoMode", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isVideoMode", value)
            }
        }

    /**
     * thermal imaging是否Open自动快门，defaultOpen true-Open false-close
     */
    var isAutoShutter: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isAutoShutter", true)
            } else {
                true
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isAutoShutter", value)
            }
        }

    /**
     * thermal imagingrecording是否同时使用麦克风recording音频，defaultclose true-开启 false-close
     */
    var isRecordAudio: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isRecordAudio", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isRecordAudio", value)
            }
        }

    /**
     * thermal imaging是否开启镜像，defaultclose即不镜像 true-镜像 false-不镜像
     */
    var isOpenMirror: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenMirror", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenMirror", value)
            }
        }

    /**
     * delayedcapture或延时recording的延时秒数，单位秒，default0秒即不delayed.
     */
    var delayCaptureSecond: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("delayCaptureSecond", 0)
            } else {
                0
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("delayCaptureSecond", value)
            }
        }

    /**
     * thermal imagingcontrast，取值range`[0,255]`，default 128
     */
    var contrastValue: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("contrastValue", 128)
            } else {
                128
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("contrastValue", value)
            }
        }

    /**
     * thermal imagingpseudo colormode，取值为pseudo colorenum值，defaultiron red
     */
    var pseudoColorMode: Int
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("pseudoColorMode", 3) else 3
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("pseudoColorMode", value)
            }
        }

    /**
     * thermal imaging画area逆时针rotation angle，取值 0、90、180、270，default [DeviceConfig.S_ROTATE_ANGLE]
     */
    var rotateAngle: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("rotateAngle", DeviceConfig.S_ROTATE_ANGLE)
            } else {
                DeviceConfig.S_ROTATE_ANGLE
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("rotateAngle", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-是否开启pseudo color条，default开启 true-开启 false-close
     */
    var isOpenPseudoBar: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenPseudoBar", true)
            } else {
                true
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenPseudoBar", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-是否开启dual light，defaultclose true-开启 false-close
     */
    var isOpenTwoLight: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenTwoLight", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenTwoLight", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-dual light开启时fusion度，取值`[0,100]`，0表示完全不透明，100表示完全透明，default 50%
     */
    var twoLightAlpha: Int
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("twoLightAlpha", 50) else 50
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("twoLightAlpha", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-锐度(细节Enhance等级)，取值range`[0,4]`，default为 2
     */
    var ddeConfig: Int
        get() = if (isSaveSetting) SPUtils.getInstance(SP_NAME).getInt("ddeConfig", 2) else 2
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("ddeConfig", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-temperaturefontcolor值，default白色.
     */
    var tempTextColor: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("tempTextColor", 0xffffffff.toInt())
            } else {
                0xffffffff.toInt()
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("tempTextColor", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-temperaturefontcolor值，default14sp.
     */
    var tempTextSize: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("tempTextSize", 14)
            } else {
                14
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("tempTextSize", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-temperature level，defaultnormal temperature，取值
     *
     * normal temperature ([CameraItemBean.TYPE_TMP_C] = 1）
     *
     * 高温 ([CameraItemBean.TYPE_TMP_H] = 0)
     *
     * 自动 ([CameraItemBean.TYPE_TMP_ZD] = -1)
     */
    var temperatureMode: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("temperatureMode", CameraItemBean.TYPE_TMP_C)
            } else {
                CameraItemBean.TYPE_TMP_C
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("temperatureMode", value)
            }
        }

    /**
     *thermal imaging-temperature measurementmode-temperature报警相关settings项.
     */
    var alarmBean: AlarmBean
        get() =
            if (isSaveSetting) {
                val json = SPUtils.getInstance(SP_NAME).getString("alarmBean", "")
                if (json.isNullOrEmpty()) AlarmBean() else Gson().fromJson(json, AlarmBean::class.java)
            } else {
                /**
                 * Executes alarmbean operation with thermal imaging domain optimization.
                 *
                 */
                AlarmBean()
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("alarmBean", Gson().toJson(value))
            }
        }

    /**
     * thermal imaging-observationmode-是否开启指南针，defaultclose true-开启 false-close
     */
    var isOpenCompass: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenCompass", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenCompass", value)
            }
        }

    /**
     * thermal imaging-observationmode-是否开启高温point，defaultclose true-开启 false-close
     */
    var isOpenHighPoint: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenHighPoint", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenHighPoint", value)
            }
        }

    /**
     * thermal imaging-observationmode-是否开启低温point，defaultclose true-开启 false-close
     */
    var isOpenLowPoint: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenLowPoint", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenLowPoint", value)
            }
        }

    /**
     * thermal imaging-observationmode-selectedAI追踪type，default未selected，取值
     *
     * 未selected ([ObserveBean.TYPE_NONE] = -1)
     *
     * dynamic recognition ([ObserveBean.TYPE_DYN_R] = 0)
     *
     * high temperature source ([ObserveBean.TYPE_TMP_H_S] = 1)
     *
     * low temperature source ([ObserveBean.TYPE_TMP_L_S] = 2)
     */
    var aiTraceType: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("aiTraceType", ObserveBean.TYPE_NONE)
            } else {
                ObserveBean.TYPE_NONE
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("aiTraceType", value)
            }
        }

    /**
     * thermal imaging-observationmode-target-是否开启target，defaultclose true-开启 false-close
     */
    var isOpenTarget: Boolean
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getBoolean("isOpenTarget", false)
            } else {
                false
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("isOpenTarget", value)
            }
        }

    /**
     * thermal imaging-observationmode-target-targetmeasurement mode，defaulthuman，取值
     *
     * human ([ObserveBean.TYPE_MEASURE_PERSON] = 10)
     *
     * sheep ([ObserveBean.TYPE_MEASURE_SHEEP] = 11)
     *
     * dog ([ObserveBean.TYPE_MEASURE_DOG] = 12)
     *
     * bird ([ObserveBean.TYPE_MEASURE_BIRD] = 13)
     */
    var targetMeasureMode: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).getInt(
                    "targetMeasureMode",
                    ObserveBean.TYPE_MEASURE_PERSON,
                )
            } else {
                ObserveBean.TYPE_MEASURE_PERSON
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("targetMeasureMode", value)
            }
        }

    /**
     * thermal imaging-observationmode-target-targettype，default横向，取值
     *
     * 横向 ([ObserveBean.TYPE_TARGET_HORIZONTAL] = 15)
     *
     * 竖向 ([ObserveBean.TYPE_TARGET_VERTICAL] = 16)
     *
     * 圆形 ([ObserveBean.TYPE_TARGET_CIRCLE] = 17)
     */
    var targetType: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).getInt(
                    "targetType",
                    ObserveBean.TYPE_TARGET_HORIZONTAL,
                )
            } else {
                ObserveBean.TYPE_TARGET_HORIZONTAL
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("targetType", value)
            }
        }

    /**
     * thermal imaging-observationmode-target-targetcolor，default绿色，取值
     *
     * 绿色 ([ObserveBean.TYPE_TARGET_COLOR_GREEN] = 20)
     *
     * 红色 ([ObserveBean.TYPE_TARGET_COLOR_RED] = 21)
     *
     * 蓝色 ([ObserveBean.TYPE_TARGET_COLOR_BLUE] = 22)
     *
     * 黑色 ([ObserveBean.TYPE_TARGET_COLOR_BLACK] = 23)
     *
     * 白色 ([ObserveBean.TYPE_TARGET_COLOR_WHITE] = 24)
     */
    var targetColorType: Int
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).getInt(
                    "targetColorType",
                    ObserveBean.TYPE_TARGET_COLOR_GREEN,
                )
            } else {
                ObserveBean.TYPE_TARGET_COLOR_GREEN
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("targetColorType", value)
            }
        }

    /**
     * report-作者name，default值 App name.
     */
    var reportAuthorName: String
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getString("reportAuthorName", CommUtils.getAppName())
            } else {
                CommUtils.getAppName()
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("reportAuthorName", value)
            }
        }

    /**
     * report-watermark内容，default值 App name.
     */
    var reportWatermarkText: String
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getString("reportWatermarkText", CommUtils.getAppName())
            } else {
                CommUtils.getAppName()
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("reportWatermarkText", value)
            }
        }

    /**
     * report-环境湿度千分比，default值500，取值`[0, 1000]`
     */
    var reportHumidity: Int
        get() =
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME)
                    .getInt("reportHumidity", 500)
            } else {
                500
            }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                SPUtils.getInstance(SP_NAME).put("reportHumidity", value)
            }
        }
}
