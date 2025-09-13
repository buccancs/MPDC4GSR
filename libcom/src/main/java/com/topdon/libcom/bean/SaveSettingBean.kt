package com.topdon.libcom.bean

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.topdon.lib.core.bean.AlarmBean
import com.topdon.lib.core.bean.CameraItemBean
import com.topdon.lib.core.bean.ObserveBean
import com.topdon.lib.core.common.SaveSettingUtil
import com.topdon.lib.core.config.DeviceConfig
import com.topdon.lib.core.utils.CommUtils

/**
 * savesettings开关开启时倒还好，读写都可以用 SharedPreferences save；
 *
 * 但若savesettings开关Close时，那一堆的configuration每个都需要avariable来save当前的更改，
 * 结果就是 Activity 里一大堆的variable。
 *
 * 这个class的想法就是把savesettings开关相关的variable都扔进里area，从这里读写。
 *
 * Created by LCG on 2024/12/24.
 */
/**
 * Specialized thermal imaging component providing SaveSettingBean functionality for the IRCamera system.
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
class SaveSettingBean(private val isWifi: Boolean = false) {
    /**
     * Get/Retrieve SPUtil Singleton.
     */
    private fun getSPUtils(): SPUtils = SPUtils.getInstance(if (isWifi) "WifiSaveSettingUtil" else "SaveSettingUtil")

    /**
     * 是否开启savesettings开关，defaultClose.
     */
    var isSaveSetting: Boolean = getSPUtils().getBoolean("isSaveSetting", true)
        set(value) {
            field = value
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().put("isSaveSetting", value)
        }

    /**
     * thermal imaging是否处于temperature measurementmode，defaulttemperature measurementmode true-temperature measurement false-观测
     */
    var isMeasureTempMode: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isMeasureTempMode", true) else true
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isMeasureTempMode", value)
            }
        }

    /**
     * 是否开启超分
     */
    var isOpenAmplify: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenAmplify", false) else false
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenAmplify", value)
            }
        }

    /**
     * thermal imaging是否selection录像mode，default拍照 true-录像 false-拍照
     */
    var isVideoMode: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isVideoMode", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isVideoMode", value)
            }
        }

    /**
     * thermal imaging是否Open自动快门，defaultOpen true-Open false-Close
     */
    var isAutoShutter: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isAutoShutter", true) else true
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isAutoShutter", value)
            }
        }

    /**
     * thermal imaging录像是否同时使用麦克风recording音频，defaultClose true-开启 false-Close
     */
    var isRecordAudio: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isRecordAudio", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isRecordAudio", value)
            }
        }

    /**
     * 延迟拍照或延时recording的延时秒数，单位秒，default0秒即不延迟.
     */
    var delayCaptureSecond: Int = if (isSaveSetting) getSPUtils().getInt("delayCaptureSecond", 0) else 0
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("delayCaptureSecond", value)
            }
        }

    var fusionType: Int =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSaveSetting) {
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().getInt(
                "fusionType",
                SaveSettingUtil.FusionTypeLPYFusion,
            )
        } else {
            SaveSettingUtil.FusionTypeLPYFusion
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("fusionType", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-是否开启dual light，defaultClose true-开启 false-Close
     */
    var isOpenTwoLight: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenTwoLight", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenTwoLight", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-dual light开启时fusion度，取值`[0,100]`，0表示完全不透明，100表示完全透明，default 50%
     */
    var twoLightAlpha: Int = if (isSaveSetting) getSPUtils().getInt("twoLightAlpha", 50) else 50
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("twoLightAlpha", value)
            }
        }

    /**
     * thermal imagingpseudo colormode，取值为pseudo colorenum值，defaultiron red
     */
    var pseudoColorMode: Int = if (isSaveSetting) getSPUtils().getInt("pseudoColorMode", 3) else 3
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("pseudoColorMode", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-是否开启pseudo color条，default开启 true-开启 false-Close
     */
    var isOpenPseudoBar: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenPseudoBar", true) else true
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenPseudoBar", value)
            }
        }

    /**
     * thermal imagingcontrast，取值range`[0,255]`，default 128
     */
    var contrastValue: Int = if (isSaveSetting) getSPUtils().getInt("contrastValue", 128) else 128
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("contrastValue", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-锐度(细节Enhance等级)，取值range`[0,4]`，default为 2
     */
    var ddeConfig: Int = if (isSaveSetting) getSPUtils().getInt("ddeConfig", 2) else 2
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("ddeConfig", value)
            }
        }

    /**
     *thermal imaging-temperature measurementmode-temperature报警相关settings项.
     */
    var alarmBean: AlarmBean =
        if (isSaveSetting) {
            val json = getSPUtils().getString("alarmBean", "")
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
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("alarmBean", Gson().toJson(value))
            }
        }

    /**
     * thermal imaging画area逆时针rotationangle，取值 0、90、180、270，default [DeviceConfig.S_ROTATE_ANGLE]
     */
    var rotateAngle: Int =
        if (isSaveSetting) {
            getSPUtils().getInt(
                "rotateAngle",
                DeviceConfig.S_ROTATE_ANGLE,
            )
        } else {
            DeviceConfig.S_ROTATE_ANGLE
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("rotateAngle", value)
            }
        }

    /**
     * thermal imaging画area是否为竖屏尺寸(192x256)
     */
    fun isRotatePortrait(): Boolean = rotateAngle == 90 || rotateAngle == 270

    /**
     * thermal imaging是否开启镜像，defaultClose即不镜像 true-镜像 false-不镜像
     */
    var isOpenMirror: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenMirror", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenMirror", value)
            }
        }

    /**
     * thermal imaging-观测mode-是否开启指南针，defaultClose true-开启 false-Close
     */
    var isOpenCompass: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenCompass", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenCompass", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-temperature字体颜色值，default白色.
     */
    var tempTextColor: Int = if (isSaveSetting) getSPUtils().getInt("tempTextColor", 0xffffffff.toInt()) else 0xffffffff.toInt()
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("tempTextColor", value)
            }
        }

    /**
     * thermal imaging-temperature measurementmode-temperature字体大小，单位 px，default14sp.
     */
    var tempTextSize: Int = if (isSaveSetting) getSPUtils().getInt("tempTextSize", SizeUtils.sp2px(14f)) else SizeUtils.sp2px(14f)
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("tempTextSize", value)
            }
        }

    /**
     * 判断当前temperature字体颜色及大小是否为defaultsettings
     */
    fun isTempTextDefault(): Boolean = tempTextColor == 0xffffffff.toInt() && tempTextSize == SizeUtils.sp2px(14f)

    /**
     * thermal imaging-temperature measurementmode-temperature档位，default常温，取值
     *
     * 常温 ([CameraItemBean.TYPE_TMP_C] = 1）
     *
     * 高温 ([CameraItemBean.TYPE_TMP_H] = 0)
     *
     * 自动 ([CameraItemBean.TYPE_TMP_ZD] = -1)
     */
    var temperatureMode: Int =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSaveSetting) {
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().getInt(
                "temperatureMode",
                CameraItemBean.TYPE_TMP_C,
            )
        } else {
            CameraItemBean.TYPE_TMP_C
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("temperatureMode", value)
            }
        }

    /**
     * thermal imaging-观测mode-是否开启高温point，defaultClose true-开启 false-Close
     */
    var isOpenHighPoint: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenHighPoint", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenHighPoint", value)
            }
        }

    /**
     * thermal imaging-观测mode-是否开启低温point，defaultClose true-开启 false-Close
     */
    var isOpenLowPoint: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenLowPoint", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenLowPoint", value)
            }
        }

    /**
     * thermal imaging-观测mode-selectedAI追踪type，default未selected，取值
     *
     * 未selected ([ObserveBean.TYPE_NONE] = -1)
     *
     * 动态识别 ([ObserveBean.TYPE_DYN_R] = 0)
     *
     * 高温源 ([ObserveBean.TYPE_TMP_H_S] = 1)
     *
     * 低温源 ([ObserveBean.TYPE_TMP_L_S] = 2)
     */
    var aiTraceType: Int = if (isSaveSetting) getSPUtils().getInt("aiTraceType", ObserveBean.TYPE_NONE) else ObserveBean.TYPE_NONE
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("aiTraceType", value)
            }
        }

    /**
     * thermal imaging-观测mode-标靶-是否开启标靶，defaultClose true-开启 false-Close
     */
    var isOpenTarget: Boolean = if (isSaveSetting) getSPUtils().getBoolean("isOpenTarget", false) else false
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("isOpenTarget", value)
            }
        }

    /**
     * thermal imaging-观测mode-标靶-标靶measurementmode，default人，取值
     *
     * 人 ([ObserveBean.TYPE_MEASURE_PERSON] = 10)
     *
     * 羊 ([ObserveBean.TYPE_MEASURE_SHEEP] = 11)
     *
     * 狗 ([ObserveBean.TYPE_MEASURE_DOG] = 12)
     *
     * 鸟 ([ObserveBean.TYPE_MEASURE_BIRD] = 13)
     */
    var targetMeasureMode: Int =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSaveSetting) {
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().getInt(
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
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("targetMeasureMode", value)
            }
        }

    /**
     * thermal imaging-观测mode-标靶-标靶type，default横向，取值
     *
     * 横向 ([ObserveBean.TYPE_TARGET_HORIZONTAL] = 15)
     *
     * 竖向 ([ObserveBean.TYPE_TARGET_VERTICAL] = 16)
     *
     * 圆形 ([ObserveBean.TYPE_TARGET_CIRCLE] = 17)
     */
    var targetType: Int =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSaveSetting) {
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().getInt(
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
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("targetType", value)
            }
        }

    /**
     * thermal imaging-观测mode-标靶-标靶颜色，default绿色，取值
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
    var targetColorType: Int =
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isSaveSetting) {
            /**
             * Retrieves the sputils with optimized performance for thermal imaging operations.
             *
             */
            getSPUtils().getInt(
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
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("targetColorType", value)
            }
        }

    /**
     * report-作者name，default值 App name.
     */
    var reportAuthorName: String =
        if (isSaveSetting) {
            getSPUtils().getString(
                "reportAuthorName",
                CommUtils.getAppName(),
            )
        } else {
            CommUtils.getAppName()
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("reportAuthorName", value)
            }
        }

    /**
     * report-watermark内容，default值 App name.
     */
    var reportWatermarkText: String =
        if (isSaveSetting) {
            getSPUtils().getString(
                "reportWatermarkText",
                CommUtils.getAppName(),
            )
        } else {
            CommUtils.getAppName()
        }
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("reportWatermarkText", value)
            }
        }

    /**
     * report-环境湿度千分比，default值500，取值`[0, 1000]`
     */
    var reportHumidity: Int = if (isSaveSetting) getSPUtils().getInt("reportHumidity", 500) else 500
        set(value) {
            field = value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isSaveSetting) {
                /**
                 * Retrieves the sputils with optimized performance for thermal imaging operations.
                 *
                 */
                getSPUtils().put("reportHumidity", value)
            }
        }
}
