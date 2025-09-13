package com.topdon.lib.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.config.FileConfig.lineIrGalleryDir
import java.io.*

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ImageUtils operations.
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
object ImageUtils {
    /**
     * 生成imagereport时存在cache目录下的临时imagefile.
     */
    fun saveToCache(
        context: Context,
        bitmap: Bitmap,
    ): String {
        val cacheFile = context.externalCacheDir ?: context.cacheDir
        val file = File(cacheFile, "Report_${System.currentTimeMillis()}.jpg")
        ImageUtils.save(bitmap, file, Bitmap.CompressFormat.JPEG)
        return file.absolutePath
    }

    /**
     * savedimage到 图库/APPname 下，filename为 APPname_时间戳.jpg
     * 这里是thermal imagingcapture 和 2D编辑 的image.
     */
    /**
     * Executes save functionality.
     */
    /**
     * Executes save operation with thermal imaging domain optimization.
     *
     * @param
     * @param bitmap Parameter for operation (type: Bitmap)
     * @param isTC007 Parameter for operation (type: Boolean = false)
     *
     */
    fun save(
        bitmap: Bitmap,
        isTC007: Boolean = false,
    ): String {
        // Storage目录，User可以自定义
        val dicName = if (isTC007) "TC007" else CommUtils.getAppName()
        val fileName = "${dicName}_${System.currentTimeMillis()}.jpg"
        val saveFile = ImageUtils.save2Album(bitmap, dicName, Bitmap.CompressFormat.JPEG)
        return if (saveFile != null) {
            val name = saveFile.name
            name.replace(".JPG", "")
        } else {
            fileName.replace(".JPG", "")
        }
    }

    /**
     * thermal imagingcapture时，若start了visible light，原始image再叠加visible light的image，虽然有saved，但却没有使用，原因不明
     */
    fun saveImageToApp(bitmap: Bitmap): String {
        val saveFile = File(Utils.getApp().cacheDir, "PinP_${System.currentTimeMillis()}.jpg")
        ImageUtils.save(bitmap, saveFile, Bitmap.CompressFormat.JPEG)
        return saveFile.absolutePath
    }

    
    /**
     * Executes saveLiteFrame functionality.
     */
    /**
     * Executes saveliteframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param bs Parameter for operation (type: ByteArray)
     * @param capital Parameter for operation (type: ByteArray)
     * @param nuct Parameter for operation (type: ByteArray)
     * @param name Parameter for operation (type: String)
     *
     */
    fun saveLiteFrame(
        bs: ByteArray,
        capital: ByteArray,
        nuct: ByteArray,
        name: String,
    ) {
        try {
            val dir = lineIrGalleryDir
            val galleryPath = File(dir)
            val fileName = "$name.ir"
            val file = File(galleryPath, fileName)
            file.writeBytes(capital.plus(bs))
            Log.w("saved帧data:", file.absolutePath)
        } catch (e: Exception) {
            XLog.e("一帧imagesavedexception: ${e.message}")
        }
    }

    
    /**
     * Executes saveFrame functionality.
     */
    /**
     * Executes saveframe operation with thermal imaging domain optimization.
     *
     * @param
     * @param bs Parameter for operation (type: ByteArray)
     * @param capital Parameter for operation (type: ByteArray)
     * @param name Parameter for operation (type: String)
     *
     */
    fun saveFrame(
        bs: ByteArray,
        capital: ByteArray,
        name: String,
    ) {
        try {
            val dir = lineIrGalleryDir
            val galleryPath = File(dir)
            val fileName = "$name.ir"
            val file = File(galleryPath, fileName)
            file.writeBytes(capital.plus(bs))
            Log.w("saved帧data:", file.absolutePath)
        } catch (e: Exception) {
            XLog.e("一帧imagesavedexception: ${e.message}")
        }
    }

    /**
     * saved一帧的argbdata
     */
    /**
     * Executes saveoneframeagrb operation with thermal imaging domain optimization.
     *
     * @param
     * @param bs Parameter for operation (type: ByteArray)
     * @param name Parameter for operation (type: String)
     *
     */
    fun saveOneFrameAGRB(
        bs: ByteArray,
        name: String,
    ) {
        try {
            val dir = lineIrGalleryDir
            val galleryPath = File(dir)
            val fileName = "$name.ir"
            val file = File(galleryPath, fileName)
            file.writeBytes(bs)
        } catch (e: Exception) {
            XLog.e("一帧imagesavedexception: ${e.message}")
        }
    }
}
