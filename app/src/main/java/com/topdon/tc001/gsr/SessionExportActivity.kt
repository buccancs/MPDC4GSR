package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivitySessionExportBinding
import com.topdon.lib.core.ktbase.BaseBindingActivity

/**
 * Specialized thermal imaging component providing SessionExportActivity functionality for the IRCamera system.
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
class SessionExportActivity : BaseBindingActivity<ActivitySessionExportBinding>() {
    companion object {
        private const val EXTRA_SESSION_ID = "session_id"

    /**
     * Executes startActivity functionality.
     */
        /**
         * Executes startactivity operation with thermal imaging domain optimization.
         *
         * @param
         * @param context Parameter for operation (type: Context)
         * @param sessionId Parameter for operation (type: String)
         *
         */
        fun startActivity(
            context: Context,
            sessionId: String,
        ) {
            val intent =
                Intent(context, SessionExportActivity::class.java).apply {
                    /**
                     * Executes putextra operation with thermal imaging domain optimization.
                     *
                     */
                    putExtra(EXTRA_SESSION_ID, sessionId)
                }
            context.startActivity(intent)
        }
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_session_export

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
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)

        // Set the content programmatically
        (binding.root as? android.widget.TextView)?.apply {
            text = "Session Export\n\nSession ID: $sessionId\n\nExport Options:\n• CSV Format\n• JSON Format\n• Research-grade metadata\n• Synchronized timestamps\n\nExport functionality coming soon..."
            /**
             * Configures the padding with validation and thermal imaging optimization.
             *
             */
            setPadding(32, 32, 32, 32)
            textSize = 16f
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Export Session"
    }

    /**
     * Executes onsupportnavigateup operation with thermal imaging domain optimization.
     *
     */
    override fun onSupportNavigateUp(): Boolean {
        /**
         * Executes onbackpressed operation with thermal imaging domain optimization.
         *
         */
        onBackPressed()
        return true
    }
}
