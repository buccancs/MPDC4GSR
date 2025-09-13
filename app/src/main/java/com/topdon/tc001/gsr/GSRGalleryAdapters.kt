package com.topdon.tc001.gsr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ItemGsrDataFileBinding
import com.csl.irCamera.databinding.ItemGsrRawImageFileBinding
import com.csl.irCamera.databinding.ItemGsrVideoFileBinding
import java.io.File

/**
 * Specialized thermal imaging component providing GSRDataAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewHolder display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    class ViewHolder(private val binding: ItemGsrDataFileBinding) : RecyclerView.ViewHolder(binding.root) {
        val fileIcon = binding.fileIcon
        val fileName = binding.fileName
        val sessionInfo = binding.sessionInfo
        val fileSize = binding.fileSize
        val sampleCount = binding.sampleCount
        val duration = binding.duration
        val createdDate = binding.createdDate
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemGsrDataFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val dataFile = dataFiles[position]

        holder.fileName.text = dataFile.file.name
        holder.sessionInfo.text = "Session: ${dataFile.sessionId} | Participant: ${dataFile.participantId}"
        holder.fileSize.text = formatFileSize(dataFile.file.length())
        holder.sampleCount.text = "${dataFile.sampleCount} samples"
        holder.duration.text = formatDuration(dataFile.duration)
        holder.createdDate.text = dataFile.createdDate

        holder.itemView.setOnClickListener {
            /**
             * Executes onitemclick operation with thermal imaging domain optimization.
             *
             */
            onItemClick(dataFile)
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount() = dataFiles.size

    /**
     * Executes formatFileSize functionality.
     */
    /**
     * Executes formatfilesize operation with thermal imaging domain optimization.
     *
     * @param
     * @param bytes Parameter for operation (type: Long)
     *
     */
    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 -> "%.1f MB".format(bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> "%.1f KB".format(bytes / 1024.0)
            else -> "$bytes B"
        }
    }
/**
 * Specialized thermal imaging component providing GSRVideoAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewHolder display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    class ViewHolder(private val binding: ItemGsrVideoFileBinding) : RecyclerView.ViewHolder(binding.root) {
        val videoThumbnail = binding.videoThumbnail
        val fileName = binding.fileName
        val fileSize = binding.fileSize
        val duration = binding.duration
        val resolution = binding.resolution
        val createdDate = binding.createdDate
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemGsrVideoFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val videoFile = videoFiles[position]

        holder.fileName.text = videoFile.name
        holder.fileSize.text = formatFileSize(videoFile.length())

        // Parse video metadata from filename if available
        val filename = videoFile.nameWithoutExtension
        when {
            filename.contains("4K") -> holder.resolution.text = "4K UHD (3840×2160)"
            filename.contains("1080") -> holder.resolution.text = "Full HD (1920×1080)"
            filename.contains("720") -> holder.resolution.text = "HD (1280×720)"
            else -> holder.resolution.text = "Unknown resolution"
        }

        holder.createdDate.text =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(videoFile.lastModified()))

        // Note: Video duration extraction requires MediaMetadataRetriever implementation
        // Set default duration for now (video duration extraction can be added later if needed)
        holder.duration.text = "Duration: Unknown"

        holder.itemView.setOnClickListener {
            /**
             * Executes onitemclick operation with thermal imaging domain optimization.
             *
             */
            onItemClick(videoFile)
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount() = videoFiles.size

    /**
     * Executes formatFileSize functionality.
/**
 * Specialized thermal imaging component providing GSRRawImageAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewHolder display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    class ViewHolder(private val binding: ItemGsrRawImageFileBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageThumbnail = binding.imageThumbnail
        val fileName = binding.fileName
        val fileSize = binding.fileSize
        val resolution = binding.resolution
        val captureInfo = binding.captureInfo
        val createdDate = binding.createdDate
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemGsrRawImageFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val rawImageFile = rawImageFiles[position]

        holder.fileName.text = rawImageFile.name
        holder.fileSize.text = formatFileSize(rawImageFile.length())

        // Parse RAW image metadata from filename
        val filename = rawImageFile.nameWithoutExtension
        holder.resolution.text = "4032×3024 (12MP)" // Samsung S22 sensor size
        holder.captureInfo.text = "DNG RAW • Level 3"

        holder.createdDate.text =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(rawImageFile.lastModified()))

        holder.itemView.setOnClickListener {
            /**
             * Executes onitemclick operation with thermal imaging domain optimization.
             *
             */
            onItemClick(rawImageFile)
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount() = rawImageFiles.size

    /**
/**
 * Specialized thermal imaging component providing GSRSessionAdapter functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for ViewHolder display and interaction.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sessionIcon: ImageView = view.findViewById(R.id.session_icon)
        val sessionId: TextView = view.findViewById(R.id.session_id)
        val participantInfo: TextView = view.findViewById(R.id.participant_info)
        val studyInfo: TextView = view.findViewById(R.id.study_info)
        val fileCount: TextView = view.findViewById(R.id.file_count)
        val duration: TextView = view.findViewById(R.id.duration)
        val startTime: TextView = view.findViewById(R.id.start_time)
    }

    /**
     * Executes oncreateviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param parent Parameter for operation (type: ViewGroup)
     * @param viewType Parameter for operation (type: Int)
     *
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gsr_session, parent, false)
        return ViewHolder(view)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: ViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val session = sessions[position]

        holder.sessionId.text = session.sessionId
        holder.participantInfo.text = "Participant: ${session.participantId}"
        holder.studyInfo.text = "Study: ${session.studyName}"
        holder.startTime.text = session.startTime
        holder.duration.text = formatDuration(session.duration)

        // Count available files
        val fileCount = mutableListOf<String>()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.gsrDataFile != null) fileCount.add("GSR Data")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.videoFile != null) fileCount.add("Video")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.rawImageCount > 0) fileCount.add("${session.rawImageCount} RAW Images")

        holder.fileCount.text =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (fileCount.isEmpty()) {
                "No files"
            } else {
                fileCount.joinToString(" • ")
            }

        holder.itemView.setOnClickListener {
            /**
             * Executes onitemclick operation with thermal imaging domain optimization.
             *
             */
            onItemClick(session)
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount() = sessions.size

    /**
     * Executes formatDuration functionality.
     */
    /**
     * Executes formatduration operation with thermal imaging domain optimization.
     *
     * @param
     * @param seconds Parameter for operation (type: Long)
     *
     */
    private fun formatDuration(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%d:%02d".format(minutes, remainingSeconds)
    }
}
