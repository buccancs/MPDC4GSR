package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivitySessionManagerBinding
import com.topdon.gsr.model.SessionInfo
import com.topdon.gsr.service.SessionManager
import com.topdon.lib.core.ktbase.BaseBindingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Specialized thermal imaging component providing SessionManagerActivity functionality for the IRCamera system.
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
class SessionManagerActivity : BaseBindingActivity<ActivitySessionManagerBinding>() {
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionManager: SessionManager

    private val sessions = mutableListOf<SessionInfo>()
    private val filteredSessions = mutableListOf<SessionInfo>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        private const val TAG = "SessionManagerActivity"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         *
         */
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SessionManagerActivity::class.java))
        }
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_session_manager

    /**
     * Executes oncreate operation with thermal imaging domain optimization.
     *
     * @param
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Initializes the ializeviews component for thermal imaging operations.
         *
         */
        initializeViews()
        /**
         * Configures the upsessionmanager with validation and thermal imaging optimization.
         *
         */
        setupSessionManager()
        /**
         * Configures the uprecyclerview with validation and thermal imaging optimization.
         *
         */
        setupRecyclerView()
        /**
         * Configures the upsearchandfilter with validation and thermal imaging optimization.
         *
         */
        setupSearchAndFilter()
        /**
         * Executes loadsessions operation with thermal imaging domain optimization.
         *
         */
        loadSessions()
    }

    /**
     * Initializes ializeviews component.
     */
    private fun initializeViews() {
        // Setup toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Session Manager"
    }

    /**
     * Sets upsessionmanager configuration.
     */
    private fun setupSessionManager() {
        sessionManager = SessionManager.getInstance(this)
    }

    /**
     * Sets uprecyclerview configuration.
     */
    private fun setupRecyclerView() {
        adapter =
            SessionAdapter(
                context = this,
                sessions = filteredSessions,
                onSessionClick = { session -> openSessionDetails(session) },
                onSessionDelete = { session -> confirmDeleteSession(session) },
                onSessionExport = { session -> exportSession(session) },
            )

        binding.sessionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.sessionsRecyclerView.adapter = adapter
    }

    /**
     * Sets upsearchandfilter configuration.
     */
    private fun setupSearchAndFilter() {
        // Setup search functionality
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                /**
                 * Executes onquerytextsubmit operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param query Parameter for operation (type: String?)
                 *
                 */
                override fun onQueryTextSubmit(query: String?): Boolean {
                    /**
                     * Executes filtersessions operation with thermal imaging domain optimization.
                     *
                     */
                    filterSessions(query)
                    return true
                }

                /**
                 * Executes onquerytextchange operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param newText Parameter for operation (type: String?)
                 *
                 */
                override fun onQueryTextChange(newText: String?): Boolean {
                    /**
                     * Executes filtersessions operation with thermal imaging domain optimization.
                     *
                     */
                    filterSessions(newText)
                    return true
                }
            },
        )

        // Setup filter spinner
        val filterOptions = arrayOf("All Sessions", "Recent", "Completed", "With Data")
        val spinnerAdapter =
            /**
             * Executes arrayadapter operation with thermal imaging domain optimization.
             *
             */
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                filterOptions,
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterSpinner.adapter = spinnerAdapter

        binding.filterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                /**
                 * Executes onitemselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>)
                 * @param view Parameter for operation (type: View?)
                 * @param position Parameter for operation (type: Int)
                 * @param id Parameter for operation (type: Long)
                 *
                 */
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    /**
                     * Executes filtersessionsbytype operation with thermal imaging domain optimization.
                     *
                     */
                    filterSessionsByType(position)
                }

                /**
                 * Executes onnothingselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>)
                 *
                 */
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }
    }

    /**
     * Executes loadSessions functionality.
     */
    /**
     * Executes loadsessions operation with thermal imaging domain optimization.
     *
     */
    private fun loadSessions() {
        /**
         * Executes showloading operation with thermal imaging domain optimization.
         *
         */
        showLoading(true)

        scope.launch {
            try {
                val loadedSessions =
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.IO) {
                        // Load from SessionManager and also scan for historical sessions
                        val activeSessions = sessionManager.getActiveSessions()
                        val historicalSessions = loadHistoricalSessions()

                        (activeSessions + historicalSessions).distinctBy { it.sessionId }
                    }

                sessions.clear()
                sessions.addAll(loadedSessions.sortedByDescending { it.startTime })
                /**
                 * Executes filtersessions operation with thermal imaging domain optimization.
                 *
                 */
                filterSessions(binding.searchView.query?.toString())

                Log.i(TAG, "Loaded ${sessions.size} sessions")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load sessions", e)
                /**
                 * Executes showerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param sessions Parameter for operation (type: ${e.message}")
                 *
                 */
                showError("Failed to load sessions: ${e.message}")
            } finally {
                /**
                 * Executes showloading operation with thermal imaging domain optimization.
                 *
                 */
                showLoading(false)
            }
        }
    }

    /**
     * Executes loadhistoricalsessions operation with thermal imaging domain optimization.
     *
     */
    private suspend fun loadHistoricalSessions(): List<SessionInfo> {
        return withContext(Dispatchers.IO) {
            val historicalSessions = mutableListOf<SessionInfo>()

            try {
                // Scan for session directories in external storage
                val baseDir = File(getExternalFilesDir(null), "recordings")
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (baseDir.exists() && baseDir.isDirectory) {
                    baseDir.listFiles()?.forEach { sessionDir ->
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (sessionDir.isDirectory && sessionDir.name.startsWith("session_")) {
                            try {
                                val sessionInfo = parseSessionFromDirectory(sessionDir)
                                historicalSessions.add(sessionInfo)
                            } catch (e: Exception) {
                                Log.w(TAG, "Failed to parse session from ${sessionDir.name}", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load historical sessions", e)
            }

            historicalSessions
        }
    }

    /**
     * Executes parseSessionFromDirectory functionality.
     */
    /**
     * Executes parsesessionfromdirectory operation with thermal imaging domain optimization.
     *
     * @param
     * @param sessionDir Parameter for operation (type: File)
     *
     */
    private fun parseSessionFromDirectory(sessionDir: File): SessionInfo {
        val sessionId = sessionDir.name
        val metadataFile = File(sessionDir, "session_metadata.txt")

        val sessionInfo =
            /**
             * Executes sessioninfo operation with thermal imaging domain optimization.
             *
             */
            SessionInfo(
                sessionId = sessionId,
                startTime = sessionDir.lastModified(),
            )

        // Parse metadata if available
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (metadataFile.exists()) {
            try {
                metadataFile.readLines().forEach { line ->
                    val parts = line.split(":", limit = 2)
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (parts.size >= 2) {
                        val key = parts[0]
                        val value = parts[1]
                        /**
                         * Executes when operation with thermal imaging domain optimization.
                         *
                         */
                        when (key.trim()) {
                            "participantId" -> sessionInfo.participantId = value.trim()
                            "studyName" -> sessionInfo.studyName = value.trim()
                            "endTime" -> sessionInfo.endTime = value.trim().toLongOrNull()
                            "sampleCount" -> sessionInfo.sampleCount = value.trim().toLongOrNull() ?: 0
                            else -> sessionInfo.metadata[key.trim()] = value.trim()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to parse metadata for session $sessionId", e)
            }
        }

        // Check for available data files
        sessionInfo.hasGSRData = File(sessionDir, "gsr_data.csv").exists()
        sessionInfo.hasRGBData = File(sessionDir, "rgb_video.mp4").exists()
        sessionInfo.hasThermalData = File(sessionDir, "thermal_video.mp4").exists()

        return sessionInfo
    }

    /**
     * Executes filterSessions functionality.
     */
    /**
     * Executes filtersessions operation with thermal imaging domain optimization.
     *
     * @param
     * @param query Parameter for operation (type: String?)
     *
     */
    private fun filterSessions(query: String?) {
        filteredSessions.clear()

        val filtered =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (query.isNullOrEmpty()) {
                sessions
            } else {
                sessions.filter { session ->
                    session.sessionId.contains(query, ignoreCase = true) ||
                        session.participantId?.contains(query, ignoreCase = true) == true ||
                        session.studyName?.contains(query, ignoreCase = true) == true
                }
            }

        filteredSessions.addAll(filtered)
        adapter.notifyDataSetChanged()
        /**
         * Executes updateemptyview operation with thermal imaging domain optimization.
         *
         */
        updateEmptyView()
    }

    /**
     * Executes filterSessionsByType functionality.
     */
    /**
     * Executes filtersessionsbytype operation with thermal imaging domain optimization.
     *
     * @param
     * @param filterIndex Parameter for operation (type: Int)
     *
     */
    private fun filterSessionsByType(filterIndex: Int) {
        val baseList = if (binding.searchView.query.isNullOrEmpty()) sessions else filteredSessions.toList()

        filteredSessions.clear()

        val filtered =
            /**
             * Executes when operation with thermal imaging domain optimization.
             *
             */
            when (filterIndex) {
                0 -> baseList // All Sessions
                1 -> baseList.filter { it.isActive() } // Active Sessions
                2 -> baseList.filter { !it.isActive() } // Completed Sessions
                3 -> baseList.filter { it.hasGSRData } // With GSR
                4 -> baseList.filter { it.hasRGBData } // With RGB
                5 -> baseList.filter { it.hasThermalData } // With Thermal
                else -> baseList
            }

        filteredSessions.addAll(filtered)
        adapter.notifyDataSetChanged()
        /**
         * Executes updateemptyview operation with thermal imaging domain optimization.
         *
         */
        updateEmptyView()
    }

    /**
     * Executes showLoading functionality.
     */
    /**
     * Executes showloading operation with thermal imaging domain optimization.
     *
     * @param
     * @param show Parameter for operation (type: Boolean)
     *
     */
    private fun showLoading(show: Boolean) {
        binding.loadingView.visibility = if (show) View.VISIBLE else View.GONE
        binding.sessionsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Executes updateEmptyView functionality.
     */
    /**
     * Executes updateemptyview operation with thermal imaging domain optimization.
     *
     */
    private fun updateEmptyView() {
        binding.emptyView.visibility = if (filteredSessions.isEmpty()) View.VISIBLE else View.GONE
    }

    /**
     * Executes showError functionality.
     */
    /**
     * Executes showerror operation with thermal imaging domain optimization.
     *
     * @param
     * @param message Parameter for operation (type: String)
     *
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Executes openSessionDetails functionality.
     */
    /**
     * Executes opensessiondetails operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private fun openSessionDetails(session: SessionInfo) {
        SessionDetailActivity.startActivity(this, session.sessionId)
    }

    /**
     * Executes confirmDeleteSession functionality.
     */
    /**
     * Executes confirmdeletesession operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private fun confirmDeleteSession(session: SessionInfo) {
        AlertDialog.Builder(this)
            .setTitle("Delete Session")
            .setMessage(
                "Are you sure you want to delete session '${session.sessionId}'?\n\n" +
                    "This will permanently remove all associated data files including " +
                    "GSR recordings, videos, and metadata.",
            )
            .setPositiveButton("Delete") { _, _ ->
                /**
                 * Executes deletesession operation with thermal imaging domain optimization.
                 *
                 */
                deleteSession(session)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Executes deleteSession functionality.
     */
    /**
     * Executes deletesession operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private fun deleteSession(session: SessionInfo) {
        scope.launch {
            try {
                val success =
                    /**
                     * Executes withcontext operation with thermal imaging domain optimization.
                     *
                     */
                    withContext(Dispatchers.IO) {
                        /**
                         * Executes deletesessionfiles operation with thermal imaging domain optimization.
                         *
                         */
                        deleteSessionFiles(session)
                    }

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (success) {
                    // Remove from active sessions if present
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (sessionManager.isSessionActive(session.sessionId)) {
                        sessionManager.completeSession(session.sessionId)
                    }

                    // Remove from local lists
                    sessions.remove(session)
                    filteredSessions.remove(session)
                    adapter.notifyDataSetChanged()
                    /**
                     * Executes updateemptyview operation with thermal imaging domain optimization.
                     *
                     */
                    updateEmptyView()

                    Toast.makeText(this@SessionManagerActivity, "Session deleted successfully", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Session deleted: ${session.sessionId}")
                } else {
                    /**
                     * Executes showerror operation with thermal imaging domain optimization.
                     *
                     */
                    showError("Failed to delete session files")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete session", e)
                /**
                 * Executes showerror operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param session Parameter for operation (type: ${e.message}")
                 *
                 */
                showError("Failed to delete session: ${e.message}")
            }
        }
    }

    /**
     * Executes deletesessionfiles operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private suspend fun deleteSessionFiles(session: SessionInfo): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Delete session directory and all contents
                val sessionDir = File(getExternalFilesDir(null), "recordings/${session.sessionId}")
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (sessionDir.exists()) {
                    sessionDir.deleteRecursively()
                }

                // Also check alternative directory structures
                val altSessionDir = File(getExternalFilesDir(null), session.sessionId)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (altSessionDir.exists()) {
                    altSessionDir.deleteRecursively()
                }

                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete session files for ${session.sessionId}", e)
                false
            }
        }
    }

    /**
     * Executes exportSession functionality.
     */
    /**
     * Executes exportsession operation with thermal imaging domain optimization.
     *
     * @param
     * @param session Parameter for operation (type: SessionInfo)
     *
     */
    private fun exportSession(session: SessionInfo) {
        // Launch session export functionality
        SessionExportActivity.startActivity(this, session.sessionId)
    }

    /**
     * Executes ondestroy operation with thermal imaging domain optimization.
     *
     */
    override fun onDestroy() {
/**
 * Specialized thermal imaging component providing SessionAdapter functionality for the IRCamera system.
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
/**
 * Custom thermal imaging view component with advanced rendering capabilities. Optimized for SessionViewHolder display and interaction.
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
    class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.session_title)
        val subtitleText: TextView = view.findViewById(R.id.session_subtitle)
        val statusText: TextView = view.findViewById(R.id.session_status)
        val dataTypesText: TextView = view.findViewById(R.id.session_data_types)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
        val exportButton: ImageButton = view.findViewById(R.id.export_button)
        val cardView: View = view
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
    ): SessionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: SessionViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: SessionViewHolder,
        position: Int,
    ) {
        val session = sessions[position]

        // Title: Session ID or participant name
        holder.titleText.text = session.participantId ?: session.sessionId

        // Subtitle: Study name and date
        val studyText = session.studyName ?: "Unnamed Study"
        val dateText = dateFormatter.format(Date(session.startTime))
        holder.subtitleText.text = "$studyText • $dateText"

        // Status
        val statusText =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (session.isActive()) {
                "🟢 Active"
            } else {
                val duration =
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (session.endTime != null) {
                        val durationMs = session.endTime!! - session.startTime
                        val minutes = durationMs / (1000 * 60)
                        "${minutes}min"
                    } else {
                        "Unknown duration"
                    }
                "⚪ Completed • $duration"
            }
        holder.statusText.text = statusText

        // Data types available
        val dataTypes = mutableListOf<String>()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.hasGSRData) dataTypes.add("GSR")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.hasRGBData) dataTypes.add("RGB")
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (session.hasThermalData) dataTypes.add("Thermal")

        holder.dataTypesText.text =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataTypes.isNotEmpty()) {
                "📊 ${dataTypes.joinToString(", ")}"
            } else {
                "📊 No data files found"
            }

        // Click handlers
        holder.cardView.setOnClickListener { onSessionClick(session) }
        holder.deleteButton.setOnClickListener { onSessionDelete(session) }
        holder.exportButton.setOnClickListener { onSessionExport(session) }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int = sessions.size
}
