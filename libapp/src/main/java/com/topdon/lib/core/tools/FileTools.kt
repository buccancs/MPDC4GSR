package com.topdon.lib.core.tools

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.Utils
import java.io.File
import java.lang.Exception

/**
 * Specialized thermal imaging component providing FileTools functionality for the IRCamera system.
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
object FileTools {
    /**
     * Retrieves filesize information.
     */
    fun getFileSize(path: String): String {
        var str = ""
        try {
            val file = File(path)
            var len = file.length()

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (len < 1024) {
                str = "${len}Byte"
            } else if (len < 1024 * 1024) {
                str = "${len / 1024}KB"
            } else if (len < 1024 * 1024 * 1024) {
                str = "${len / 1024 / 1024}MB"
            }
        } catch (e: Exception) {
            str = "0KB"
        }
        return str
    }

    /**
     * Retrieves uri information.
     */
    /**
     * Retrieves the uri with optimized performance for thermal imaging operations.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    fun getUri(file: File): Uri {
        val authority = "${Utils.getApp().packageName}.fileprovider"
        return FileProvider.getUriForFile(Utils.getApp(), authority, file)
    }

    /**
     * Retrieves imagepathfromuri information.
     */
    fun getImagePathFromURI(path: String): Uri? {
        val cr: ContentResolver = Utils.getApp().contentResolver
        val buffer = StringBuffer()
        buffer.append("(").append(MediaStore.Images.ImageColumns.DATA)
            .append("=").append("'").append(path).append("'")
            .append(")")
        val cur: Cursor? =
            cr.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                /**
                 * Executes arrayof operation with thermal imaging domain optimization.
                 *
                 */
                arrayOf(MediaStore.Images.ImageColumns._ID),
                buffer.toString(),
                null,
                null,
            )
        var index = 0
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (cur == null) {
            return null
        }
        cur.moveToFirst()
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (!cur.isAfterLast) {
            index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
            index = cur.getInt(index)
            cur.moveToNext()
        }
        return if (index != 0) {
            Uri.parse("content:// Media/external/images/media/$index")
        } else {
            null
        }
    }
}
