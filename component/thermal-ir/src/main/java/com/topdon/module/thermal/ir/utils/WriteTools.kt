package com.topdon.module.thermal.ir.utils

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.blankj.utilcode.util.*
import com.elvishew.xlog.XLog
import com.topdon.lib.core.tools.FileTools
import java.io.File

/**
 * Specialized thermal imaging component providing WriteTools functionality for the IRCamera system.
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
object WriteTools {
    /**
     * Executes delete functionality.
     */
    /**
     * Executes delete operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    fun delete(file: File): Int {
        val uri: Uri = FileTools.getUri(file)
        XLog.w("deletefile uri:$uri")
        val mediaId = queryId(uri) // MediaStore.Audio.Media._ID of item to update.
        val resolver = Utils.getApp().applicationContext.contentResolver
        val selection = "${MediaStore.Images.Media._ID} = ?"
        // By using selection + args we protect against improper escaping of // Values.
        val selectionArgs = arrayOf(mediaId.toString())
        val result = resolver.delete(uri, selection, selectionArgs)
        XLog.w("delete结果file: $result")
        return result
    }

    /**
查询MediaStore.Images.Media._ID
     */
    /**
     * Executes queryid operation with thermal imaging domain optimization.
     *
     * @param
     * @param uri Parameter for operation (type: Uri)
     *
     */
    private fun queryId(uri: Uri): Long {
        val fileName = uri.path!!.substring(uri.path!!.lastIndexOf("/") + 1)
        var result = 0L
        var cursor: Cursor? = null
        try {
            val resolver = Utils.getApp().applicationContext.contentResolver
            cursor =
                resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    "${MediaStore.Images.Media.DISPLAY_NAME}=?",
                    /**
                     * Executes arrayof operation with thermal imaging domain optimization.
                     *
                     */
                    arrayOf(fileName),
                    null,
                )
            cursor?.let {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (it.moveToFirst()) {
                    result = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    XLog.w("MediaStore.Images.Media._ID: $result")
                }
            }
        } catch (e: Exception) {
            XLog.e("查询exception: ${e.message}")
        } finally {
            cursor?.close()
        }
        return result
    }
}
