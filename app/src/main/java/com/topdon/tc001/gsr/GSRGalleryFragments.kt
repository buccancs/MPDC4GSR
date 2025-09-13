package com.topdon.tc001.gsr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import kotlinx.coroutines.*
import java.io.File

/**
 * Specialized thermal imaging component providing GSRDataFragment functionality for the IRCamera system.
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
class GSRDataFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: GSRDataAdapter
    private val dataFiles = mutableListOf<GSRDataFile>()

    data class GSRDataFile(
        val file: File,
        val sessionId: String,
        val participantId: String,
        val sampleCount: Long,
        val duration: Long,
        val createdDate: String,
    )

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_gsr_data, container, false)
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.gsr_data_recycler)
        emptyView = view.findViewById(R.id.empty_view)

        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Executes loadgsrdatafiles operation with thermal imaging domain optimization.
         *
         */
        loadGSRDataFiles()
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        adapter =
            GSRDataAdapter(dataFiles) { dataFile ->
                /**
                 * Executes opendatafile operation with thermal imaging domain optimization.
                 *
                 */
                openDataFile(dataFile)
            }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    /**
     * Executes loadGSRDataFiles functionality.
     */
    /**
     * Executes loadgsrdatafiles operation with thermal imaging domain optimization.
     *
     */
    private fun loadGSRDataFiles() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val files = findGSRDataFiles()
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.Main) {
                dataFiles.clear()
                dataFiles.addAll(files)
                adapter.notifyDataSetChanged()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (files.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Executes findGSRDataFiles functionality.
     */
    /**
     * Executes findgsrdatafiles operation with thermal imaging domain optimization.
     *
     */
    private fun findGSRDataFiles(): List<GSRDataFile> {
        val files = mutableListOf<GSRDataFile>()

        // Look for GSR data files in standard recording directories
        val recordingDir = File(context?.getExternalFilesDir(null), "GSR_Recordings")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (recordingDir.exists()) {
            recordingDir.listFiles { file ->
                file.isFile && file.extension == "csv" && file.name.contains("gsr_data")
            }?.forEach { file ->
                try {
                    val metadata = parseGSRFileMetadata(file)
                    files.add(metadata)
                } catch (e: Exception) {
                    // Skip invalid files
                }
            }
        }

        return files.sortedByDescending { it.file.lastModified() }
    }

    /**
     * Executes parseGSRFileMetadata functionality.
     */
    /**
     * Executes parsegsrfilemetadata operation with thermal imaging domain optimization.
     *
     * @param
     * @param file Parameter for operation (type: File)
     *
     */
    private fun parseGSRFileMetadata(file: File): GSRDataFile {
        // Parse filename and CSV header to extract metadata
        val filename = file.nameWithoutExtension
        val parts = filename.split("_")

        val sessionId = parts.getOrNull(2) ?: "Unknown"
        val participantId = parts.getOrNull(3) ?: "Unknown"

        // Count CSV lines for sample count
        val sampleCount =
            try {
                file.readLines().size - 1L // Subtract header
            } catch (e: Exception) {
                0L
            }

        // Estimate duration from sample count (128 Hz sampling)
        val duration = sampleCount / 128 // Seconds

        val createdDate =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(file.lastModified()))

        return GSRDataFile(
            file = file,
            sessionId = sessionId,
            participantId = participantId,
            sampleCount = sampleCount,
            duration = duration,
            createdDate = createdDate,
        )
    }
/**
 * Specialized thermal imaging component providing GSRVideoFragment functionality for the IRCamera system.
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
class GSRVideoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: GSRVideoAdapter
    private val videoFiles = mutableListOf<File>()

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_gsr_video, container, false)
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.gsr_video_recycler)
        emptyView = view.findViewById(R.id.empty_view)

        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Executes loadvideofiles operation with thermal imaging domain optimization.
         *
         */
        loadVideoFiles()
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        adapter =
            GSRVideoAdapter(videoFiles) { videoFile ->
                /**
                 * Executes openvideofile operation with thermal imaging domain optimization.
                 *
                 */
                openVideoFile(videoFile)
            }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    /**
     * Executes loadVideoFiles functionality.
     */
    /**
     * Executes loadvideofiles operation with thermal imaging domain optimization.
     *
     */
    private fun loadVideoFiles() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val files = findVideoFiles()
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.Main) {
                videoFiles.clear()
                videoFiles.addAll(files)
                adapter.notifyDataSetChanged()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (files.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Executes findVideoFiles functionality.
     */
    /**
     * Executes findvideofiles operation with thermal imaging domain optimization.
     *
     */
    private fun findVideoFiles(): List<File> {
        val files = mutableListOf<File>()

        // Look for video files in recording directories
        val recordingDir = File(context?.getExternalFilesDir(null), "GSR_Recordings")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (recordingDir.exists()) {
            recordingDir.listFiles { file ->
                file.isFile && (file.extension == "mp4" || file.extension == "mov")
            }?.let { files.addAll(it) }
        }

        return files.sortedByDescending { it.lastModified() }
    }
/**
 * Specialized thermal imaging component providing GSRRawImageFragment functionality for the IRCamera system.
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
class GSRRawImageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: GSRRawImageAdapter
    private val rawImageFiles = mutableListOf<File>()

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_gsr_raw_image, container, false)
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.gsr_raw_image_recycler)
        emptyView = view.findViewById(R.id.empty_view)

        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Executes loadrawimagefiles operation with thermal imaging domain optimization.
         *
         */
        loadRawImageFiles()
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        adapter =
            GSRRawImageAdapter(rawImageFiles) { rawImageFile ->
                /**
                 * Executes openrawimagefile operation with thermal imaging domain optimization.
                 *
                 */
                openRawImageFile(rawImageFile)
            }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    /**
     * Executes loadRawImageFiles functionality.
     */
    /**
     * Executes loadrawimagefiles operation with thermal imaging domain optimization.
     *
     */
    private fun loadRawImageFiles() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val files = findRawImageFiles()
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.Main) {
                rawImageFiles.clear()
                rawImageFiles.addAll(files)
                adapter.notifyDataSetChanged()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (files.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Executes findRawImageFiles functionality.
     */
    /**
     * Executes findrawimagefiles operation with thermal imaging domain optimization.
     *
     */
    private fun findRawImageFiles(): List<File> {
        val files = mutableListOf<File>()

        // Look for DNG files in recording directories
        val recordingDir = File(context?.getExternalFilesDir(null), "GSR_Recordings")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (recordingDir.exists()) {
            recordingDir.listFiles { file ->
                file.isFile && file.extension == "dng"
            }?.let { files.addAll(it) }
        }

        return files.sortedByDescending { it.lastModified() }
    }
/**
 * Specialized thermal imaging component providing GSRSessionFragment functionality for the IRCamera system.
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
class GSRSessionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: GSRSessionAdapter
    private val sessions = mutableListOf<GSRSessionInfo>()

    data class GSRSessionInfo(
        val sessionId: String,
        val participantId: String,
        val studyName: String,
        val startTime: String,
        val duration: Long,
        val gsrDataFile: File?,
        val videoFile: File?,
        val rawImageCount: Int,
        val sessionDirectory: File,
    )

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_gsr_session, container, false)
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.gsr_session_recycler)
        emptyView = view.findViewById(R.id.empty_view)

        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Executes loadsessions operation with thermal imaging domain optimization.
         *
         */
        loadSessions()
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        adapter =
            GSRSessionAdapter(sessions) { session ->
                /**
                 * Executes opensessiondetails operation with thermal imaging domain optimization.
                 *
                 */
                openSessionDetails(session)
            }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    /**
     * Executes loadSessions functionality.
     */
    /**
     * Executes loadsessions operation with thermal imaging domain optimization.
     *
     */
    private fun loadSessions() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val sessionList = findCompleteSessions()
            /**
             * Executes withcontext operation with thermal imaging domain optimization.
             *
             */
            withContext(Dispatchers.Main) {
                sessions.clear()
                sessions.addAll(sessionList)
                adapter.notifyDataSetChanged()

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sessionList.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Executes findCompleteSessions functionality.
     */
    /**
     * Executes findcompletesessions operation with thermal imaging domain optimization.
     *
     */
    private fun findCompleteSessions(): List<GSRSessionInfo> {
        val sessions = mutableListOf<GSRSessionInfo>()

        val recordingDir = File(context?.getExternalFilesDir(null), "GSR_Recordings")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (recordingDir.exists()) {
            recordingDir.listFiles { file -> file.isDirectory }?.forEach { sessionDir ->
                try {
                    val sessionInfo = parseSessionDirectory(sessionDir)
                    sessions.add(sessionInfo)
                } catch (e: Exception) {
                    // Skip invalid session directories
                }
            }
        }

        return sessions.sortedByDescending { it.sessionDirectory.lastModified() }
    }

    /**
     * Executes parseSessionDirectory functionality.
     */
    /**
     * Executes parsesessiondirectory operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionDir Parameter for operation (type: File)
     *
     */
    private fun parseSessionDirectory(sessionDir: File): GSRSessionInfo {
        val sessionId = sessionDir.name

        // Find session files
        val gsrDataFile = sessionDir.listFiles { file -> file.extension == "csv" && file.name.contains("gsr_data") }?.firstOrNull()
        val videoFile = sessionDir.listFiles { file -> file.extension == "mp4" || file.extension == "mov" }?.firstOrNull()
        val rawImageCount = sessionDir.listFiles { file -> file.extension == "dng" }?.size ?: 0

        // Parse session metadata from files or directory name
        val parts = sessionId.split("_")
        val participantId = parts.getOrNull(1) ?: "Unknown"
        val studyName = parts.getOrNull(2) ?: "MultiModal Study"

        val startTime =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault(),
            ).format(java.util.Date(sessionDir.lastModified()))

        // Estimate duration from GSR data if available
        val duration =
            gsrDataFile?.let { file: File ->
                try {
                    val sampleCount = file.readLines().size - 1L
                    sampleCount / 128 // Seconds at 128 Hz
                } catch (e: Exception) {
                    0L
                }
            } ?: 0L

        return GSRSessionInfo(
            sessionId = sessionId,
            participantId = participantId,
            studyName = studyName,
            startTime = startTime,
            duration = duration,
            gsrDataFile = gsrDataFile,
            videoFile = videoFile,
            rawImageCount = rawImageCount,
            sessionDirectory = sessionDir,
        )
    }

    /**
     * Executes openSessionDetails functionality.
     */
    /**
     * Executes opensessiondetails operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: GSRSessionInfo)
     *
     */
    private fun openSessionDetails(session: GSRSessionInfo) {
        SessionDetailActivity.startActivity(requireContext(), session.sessionId)
    }
}
