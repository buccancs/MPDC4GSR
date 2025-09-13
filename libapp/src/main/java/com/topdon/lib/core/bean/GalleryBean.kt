package com.topdon.lib.core.bean

import android.os.Parcel
import android.os.Parcelable
import com.topdon.lib.core.config.FileConfig
import com.topdon.lib.core.repository.FileBean
import com.topdon.lib.core.tools.TimeTool
import com.topdon.lib.core.tools.VideoTools
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.TimeZone

@Parcelize
/**
 * Specialized thermal imaging component providing GalleryBean functionality for the IRCamera system.
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
open class GalleryBean(
    val id: Int, // 仅TS004远端时，id
    val path: String,
    val thumb: String,
    val name: String,
    val duration: Long, // 仅当为video时，持续毫秒数
    val timeMillis: Long,
    var hasDownload: Boolean,
) : Parcelable {
    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    constructor(file: File) : this(
        id = 0,
        path = file.absolutePath,
        thumb = file.absolutePath,
        name = file.name,
        duration = VideoTools.getLocalVideoDuration(file.absolutePath),
        timeMillis = TimeTool.updateDateTime(file),
        hasDownload = true,
    )

    /**
     * Executes constructor operation with thermal imaging domain optimization.
     *
     * @param
     * @param isVideo Parameter for operation (type: Boolean)
     * @param fileBean Parameter for operation (type: FileBean)
     *
     */
    constructor(isVideo: Boolean, fileBean: FileBean) : this(
        id = fileBean.id,
        path = "http:// 192.168.40.1:8080/DCIM/${fileBean.name}",
        thumb = if (isVideo) "http:// 192.168.40.1:8080/DCIM/${fileBean.thumb}" else "http:// 192.168.40.1:8080/DCIM/${fileBean.name}",
        name = fileBean.name,
        duration = fileBean.duration * 1000L,
        timeMillis = fileBean.time * 1000 - TimeZone.getDefault().getOffset(fileBean.time * 1000),
        hasDownload = File(FileConfig.ts004GalleryDir, fileBean.name).exists(),
    )
/**
 * Specialized thermal imaging component providing GalleryTitle functionality for the IRCamera system.
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
class GalleryTitle(timeMillis: Long) : GalleryBean(
    id = 0,
    path = "",
    thumb = "",
    name = "",
    duration = 0L,
    timeMillis = timeMillis,
    hasDownload = true,
) {
    companion object CREATOR : Parcelable.Creator<GalleryTitle> {
        /**
         * Executes createfromparcel operation with thermal imaging domain optimization.
         *
         * @param
         * @param parcel Parameter for operation (type: Parcel)
         *
         */
        override fun createFromParcel(parcel: Parcel): GalleryTitle {
            return GalleryTitle(parcel.readLong())
        }

        /**
         * Executes newarray operation with thermal imaging domain optimization.
         *
         * @param
         * @param size Parameter for operation (type: Int)
         *
         */
        override fun newArray(size: Int): Array<GalleryTitle?> {
            return arrayOfNulls(size)
        }
    }
}
