package com.topdon.lib.ui.config

/**
 * 管理摄像头的property值
 * @author: CaiSongL
 * @date: 2023/4/4 9:57
 */
/**
 * CameraHelp class
 */
/**
 * Camera help utility class for thermal imaging operations.
 * Provides helper functions and common functionality.
 */
object CameraHelp {
    /**
     * pseudo color条
     */
    val TYPE_SET_PSEUDOCOLOR = 4

    /**
     * contrast
     */
    val TYPE_SET_ParamLevelContrast = 3

    /**
     * 锐度（细节）
     */
    val TYPE_SET_ParamLevelDde = 2

    /**
     * warning
     */
    val TYPE_SET_ALARM = 12 // 预警

    /**
     * 旋转
     */
    val TYPE_SET_ROTATE = 1

    /**
     * font
     */
    val TYPE_SET_COLOR = 13 // color值

    /**
     * 镜像
     */
    val TYPE_SET_MIRROR = 14 // 镜像

    /**
     * 仅 2D 编辑：watermark
     */
    val TYPE_SET_WATERMARK = 15 // watermark

    /**
     * 仅 TS001-observation：指南针
     */
    val TYPE_SET_COMPASS = 23 // 指南针

    // TS001 -- calibrationmode
    val TYPE_SET_HIGHTEMP = 20 // maximum温
    val TYPE_SET_LOWTEMP = 21 // minimum温
    val TYPE_SET_DETELE = 22 // delete

    // TS001 -- targetmenu
    val TYPE_SET_TARGET_MODE = 30 // target
    val TYPE_SET_TARGET_ZOOM = 31 // 缩放
    val TYPE_SET_MEASURE_MODE = 32 // measurement mode
    val TYPE_SET_TARGET_COLOR = 33 // targetcolor
    val TYPE_SET_TARGET_DELETE = 34 // delete
    val TYPE_SET_TARGET_HELP = 35 // 帮助
}
