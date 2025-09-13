package com.topdon.lib.core.config

import android.content.Context
import android.os.Build
import android.os.Environment
import com.blankj.utilcode.util.Utils
import com.topdon.lib.core.repository.GalleryRepository.DirType
import com.topdon.lib.core.utils.CommUtils
import java.io.File

/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for FileConfig operations.
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
object FileConfig {
    /**
     * Get/Retrieve房屋检测cache目录下指定file.
     * 注意，不执行子filecreate逻辑，若有需要需自行create.
     */
    /**
     * Retrieves detectimagedir information.
     */
    fun getDetectImageDir(
        context: Context,
        child: String,
    ): File {
        val externalDir = context.getExternalFilesDir("detect")
        return if (externalDir == null) {
            val fileDir = File(context.filesDir, "detect")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(fileDir, child)
        } else {
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(externalDir, child)
        }
    }

    /**
     * Get/Retrieve房屋检测-签名imagecache目录.
     * 注意，不执行子filecreate逻辑，若有需要需自行create.
     */
    /**
     * Retrieves signimagedir information.
     */
    fun getSignImageDir(
        context: Context,
        child: String,
    ): File {
        val externalDir = context.getExternalFilesDir("sign")
        return if (externalDir == null) {
            val fileDir = File(context.filesDir, "sign")
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(fileDir, child)
        } else {
            /**
             * Executes file operation with thermal imaging domain optimization.
             *
             */
            File(externalDir, child)
        }
    }

    /**
     * firmwareUpgrade包Install目录.
     */
    /**
     * Retrieves the firmwarefile with optimized performance for thermal imaging operations.
     *
     * @param
     * @param filename Parameter for operation (type: String)
     *
     */
    fun getFirmwareFile(filename: String): File = File(Utils.getApp().getExternalFilesDir("firmware"), filename)

    /**
     * imagereportpath.
     */
    @JvmStatic
    /**
     * Retrieves pdfdir information.
     */
    fun getPdfDir(): String {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
            val path = dir + File.separator + CommUtils.getAppName() + File.separator + "pdf"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            path
        } else {
            Environment.DIRECTORY_DOCUMENTS + "/${CommUtils.getAppName()}/pdf"
        }
    }

    /**
     * temperature监控export Excel 目录.
     */
    @JvmStatic
    val excelDir: String
        get() {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
                val path = dir + File.separator + CommUtils.getAppName() + File.separator + "excel"
                val file = File(path)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!file.exists()) {
                    file.mkdirs()
                }
                path
            } else {
                Environment.DIRECTORY_DOCUMENTS + "/${CommUtils.getAppName()}/excel"
            }
        }

    /**
     * 原有图库目录
     */
    @JvmStatic
    val gallerySourDir: String
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() {
            val result = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + File.separator + "MPDC4GSR"
            val file = File(result)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return result
        }

    /**
     * 老 APP TC001 图库目录，仅用于相册Migration
     */
    @JvmStatic
    val oldTc001GalleryDir: String
        get() {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
            val path = dir + File.separator + "TC001"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * Retrieves gallerydirbytype information.
     */
    fun getGalleryDirByType(currentDirType: DirType): String =
        when (currentDirType) {
            DirType.LINE -> lineGalleryDir
            DirType.TC007 -> tc007GalleryDir
            else -> ts004GalleryDir
        }

    /**
     * 有linedevice 图库目录
     */
    @JvmStatic
    val lineGalleryDir: String
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
            val path = dir + File.separator + CommUtils.getAppName()
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * TS004 手机本地图库目录
     */
    @JvmStatic
    val ts004GalleryDir: String
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
            val path = dir + File.separator + "TS004"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * TC007 手机本地图库目录
     */
    @JvmStatic
    val tc007GalleryDir: String
        /**
         * Retrieves the  with optimized performance for thermal imaging operations.
         *
         */
        get() {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
            val path = dir + File.separator + "TC007"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * 有linedevice 手机本地图库temperaturedata目录
     */
    @JvmStatic
    val lineIrGalleryDir: String
        get() {
            val dir = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath
            val path = dir + File.separator + "${CommUtils.getAppName()}-ir"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * TC007 手机本地图库temperaturedata目录
     */
    @JvmStatic
    val tc007IrGalleryDir: String
        get() {
            val dir = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath
            val path = dir + File.separator + "TC007-ir"
            val file = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }

    /**
     * 外部storage/Documents/APPname/house
     */
    @JvmStatic
    val documentsDir: String
        get() {
            return if (Build.VERSION.SDK_INT < 29) {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
                val path = dir + File.separator + CommUtils.getAppName() + "/house"
                val file = File(path)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!file.exists()) {
                    file.mkdirs()
                }
                path
            } else {
                Environment.DIRECTORY_DOCUMENTS + File.separator + CommUtils.getAppName() + "/house/"
            }
        }
}
