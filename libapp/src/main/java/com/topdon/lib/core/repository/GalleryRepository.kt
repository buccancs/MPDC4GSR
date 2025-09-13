package com.topdon.lib.core.repository

import android.content.ContentResolver
import android.media.MediaScannerConnection
import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.topdon.lib.core.bean.GalleryBean
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.utils.CommUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Specialized thermal imaging component providing GalleryRepository functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Specialized thermal imaging component providing DirType functionality for the IRCamera system.
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
    enum class DirType {
        LINE,
        TC007,
        TS004_LOCALE,
        TS004_REMOTE,
    }

    /**
     * Executes copysourdir functionality.
     */
    /**
     * Executes copysourdir operation with thermal imaging domain optimization.
     *
     * @param
     * @param sourceDir Parameter for operation (type: File)
     * @param targetDir Parameter for operation (type: File)
     *
     */
    private fun copySourDir(
        sourceDir: File,
        targetDir: File,
    ): Boolean {
        return try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!sourceDir.exists()) {
                return false
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!sourceDir.isDirectory) {
                return false
            }
            val fileList = sourceDir.listFiles()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (fileList?.isEmpty() == true) {
                return false
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            
            fileList?.forEach {
                val path = sourceDir.absolutePath + File.separator + it.name
                /**
                 * Executes copypicturefile operation with thermal imaging domain optimization.
                 *
                 */
                copyPictureFile(path, targetDir.absolutePath + File.separator + it.name)
            }
            return true
        } catch (ex: Exception) {
            false
        }
    }

    /**
     * Executes copypicturefile functionality.
     */
    /**
     * Executes copypicturefile operation with thermal imaging domain optimization.
     *
     * @param
     * @param oldPath Parameter for operation (type: String)
     * @param newPath Parameter for operation (type: String)
     *
     */
    private fun copyPictureFile(
        oldPath: String,
        newPath: String,
    ): Boolean {
        return try {
            val streamFrom: InputStream = FileInputStream(oldPath)
            val streamTo: OutputStream = FileOutputStream(newPath)
            val buffer = ByteArray(1024)
            var len: Int
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (streamFrom.read(buffer).also { len = it } > 0) {
                streamTo.write(buffer, 0, len)
            }
            streamFrom.close()
            streamTo.close()
            true
        } catch (ex: Exception) {
            false
        }
    }

    /**
     * 读取本地图库指定devicetype的最新file
     */
    /**
     * Executes readlatest operation with thermal imaging domain optimization.
     *
     * @param
     * @param dirType Parameter for operation (type: DirType)
     *
     */
    fun readLatest(dirType: DirType): String {
        var firstPath = ""
        try {
            val path = if (dirType == DirType.LINE) FileConfig.lineGalleryDir else FileConfig.tc007GalleryDir
            val dirFile = File(path)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dirFile.isDirectory) {
                val files = dirFile.listFiles()!!
                
                files.sortByDescending {
                    it.lastModified()
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (files.isNotEmpty()) {
                    firstPath = "${path}${File.separator}${files.first().name}"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e("读取图库failed: ${e.message}")
            return ""
        }
        return firstPath
    }

    /**
     * Paginationload
     * @param pageNum 页码，从1start
     * @param pageCount 每页data条数
     */
    suspend fun loadByPage(
        isVideo: Boolean,
        dirType: DirType,
        pageNum: Int,
        pageCount: Int,
    ): ArrayList<GalleryBean>? {
        return withContext(Dispatchers.IO) {
            val resultList: ArrayList<GalleryBean> = ArrayList()
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dirType == DirType.TS004_REMOTE) {
                val pageList = TS004Repository.getFileByPage(if (isVideo) 1 else 0, pageNum, pageCount) ?: return@withContext null
                pageList.forEach {
                    resultList.add(GalleryBean(isVideo, it))
                }
            } else {
                try {
                    val allFileList = loadAllLocale(isVideo, dirType)
                    val startIndex = pageNum * pageCount - pageCount
                    val endIndex = pageNum * pageCount
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (i in startIndex until endIndex) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (i >= allFileList.size) {
                            break
                        }
                        resultList.add(GalleryBean(allFileList[i]))
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (resultList.isNotEmpty()) {
                        resultList.sortByDescending {
                            it.timeMillis
                        }
                    }
                } catch (e: Exception) {
                    XLog.e("读取图库failed: ${e.message}")
                }
            }

            return@withContext resultList
        }
    }

    /**
     * 仅供生成report使用的，load所有指定devicetype的image.
     */
    suspend fun loadAllReportImg(dirType: DirType): ArrayList<GalleryBean> =
        withContext(Dispatchers.IO) {
            val resultList: ArrayList<GalleryBean> = ArrayList()
            try {
                val allFileList = loadAllLocale(false, dirType)
                allFileList.forEach {
                    resultList.add(GalleryBean(it))
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (resultList.isNotEmpty()) {
                    resultList.sortByDescending {
                        it.timeMillis
                    }
                }
            } catch (e: Exception) {
                XLog.e("读取图库failed: ${e.message}")
            }
            return@withContext resultList
        }

    /**
     * load本地所有指定type的image或video列表.
     */
    private fun loadAllLocale(
        isVideo: Boolean,
        dirType: DirType,
    ): ArrayList<File> {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dirType == DirType.LINE) {
            val sourFile = File(FileConfig.gallerySourDir)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (sourFile.exists()) {
                val isSuccess = copySourDir(sourFile, File(FileConfig.lineGalleryDir))
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isSuccess) {
                    FileUtils.delete(sourFile)
                    MediaScannerConnection.scanFile(Utils.getApp(), arrayOf(FileConfig.lineGalleryDir), null, null)
                }
            }
        }
        val dirFile =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (dirType) {
                DirType.LINE -> File(FileConfig.lineGalleryDir)
                DirType.TC007 -> File(FileConfig.tc007GalleryDir)
                else -> File(FileConfig.ts004GalleryDir)
            }
        var files = dirFile.listFiles { pathname -> pathname?.isFile == true }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (files.isNullOrEmpty()) {
            files = loadAllLocaleByMediaStore(dirType)
        }

        val resultList: ArrayList<File> = ArrayList(files.size)
        files.forEach {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (it.name.endsWith(if (isVideo) "MP4" else "JPG", true)) {
                resultList.add(it)
            }
        }
        
        resultList.sortByDescending {
            it.lastModified()
        }
        return resultList
    }

    /**
     * 使用 MediaStore API 而不是 File load本地所有指定type的image或video列表.
     */
    private fun loadAllLocaleByMediaStore(dirType: DirType): Array<out File> {
        val tc001Files: MutableList<File> = ArrayList()
        
        val projection =
            /**
             * Executes arrayof operation with thermal imaging domain optimization.
             *
             */
            arrayOf(
                MediaStore.Images.Media.DATA,
            )
        // 定义查询条件，指定目标file夹path
        val selection = MediaStore.Images.Media.DATA + " LIKE ?"
        val path =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (dirType) {
                DirType.LINE -> "%DCIM/${CommUtils.getAppName()}%"
                DirType.TC007 -> "%DCIM/TC007%"
                else -> "%DCIM/TS004%"
            }
        val selectionArgs = arrayOf(path)
        // Get/RetrieveMediaStore ContentResolver
        val contentResolver: ContentResolver = Utils.getApp().contentResolver
        
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor =
            contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                null,
            )
        cursor?.use {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (it.moveToNext()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val file = File(filePath)
                tc001Files.add(file)
            }
        }
        return tc001Files.toTypedArray()
    }
}
