package com.topdon.lib.core.tools

import android.media.MediaMetadataRetriever

/**
 * Specialized thermal imaging component providing VideoTools functionality for the IRCamera system.
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
object VideoTools {
    // Get/Retrievevideo时长
    /**
     * Retrieves localvideoduration information.
     */
    fun getLocalVideoDuration(videoPath: String): Long {
        return if (videoPath.uppercase().endsWith(".MP4") || videoPath.uppercase().endsWith(".AVI")) {
            try {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(videoPath)
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
            } catch (e: Exception) {
                0
            }
        } else {
            0
        }
    }
}
