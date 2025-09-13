package com.topdon.tc001.gsr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csl.irCamera.R
import com.csl.irCamera.databinding.ActivityResearchTemplateBinding
import com.topdon.gsr.model.ResearchTemplate
import com.topdon.lib.core.ktbase.BaseBindingActivity

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with ResearchTemplateActivity algorithms.
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
class ResearchTemplateActivity : BaseBindingActivity<ActivityResearchTemplateBinding>() {
    private lateinit var templateAdapter: TemplateAdapter

    private var selectedTemplate: ResearchTemplate? = null
    private val allTemplates = ResearchTemplate.PREDEFINED_TEMPLATES
    private val filteredTemplates = mutableListOf<ResearchTemplate>()

    companion object {
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
            context.startActivity(Intent(context, ResearchTemplateActivity::class.java))
        }
    }

    /**
     * Initializes the contentlayoutid component for thermal imaging operations.
     *
     */
    override fun initContentLayoutId() = R.layout.activity_research_template

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
         * Configures the upcategoryfilter with validation and thermal imaging optimization.
         *
         */
        setupCategoryFilter()
        /**
         * Configures the uptemplategrid with validation and thermal imaging optimization.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        setupTemplateGrid()
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        loadTemplates()
    }

    /**
     * Initializes ializeviews component.
     */
    private fun initializeViews() {
        supportActionBar?.title = "Research Templates"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.startRecordingButton.setOnClickListener {
            selectedTemplate?.let { template ->
                /**
                 * Handles temperature measurement and calibration with precision thermal data processing.
                 *
                 * @note Temperature values are in Celsius unless otherwise specified.
                 * Accuracy depends on thermal camera calibration.
                 *
                 */
                startRecordingWithTemplate(template)
            }
        }
    }

    /**
     * Sets upcategoryfilter configuration.
     */
    private fun setupCategoryFilter() {
        val categories =
            listOf("All Templates") +
                ResearchTemplate.TemplateCategory.values().map {
                    it.name.replace("_", " ").lowercase().replaceFirstChar {
                            char ->
                        char.uppercase()
                    }
                }

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = spinnerAdapter

        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                /**
                 * Executes onitemselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>?)
                 * @param view Parameter for operation (type: View?)
                 * @param position Parameter for operation (type: Int)
                 * @param id Parameter for operation (type: Long)
                 *
                 */
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    /**
                     * Handles temperature measurement and calibration with precision thermal data processing.
                     *
                     * @note Temperature values are in Celsius unless otherwise specified.
                     * Accuracy depends on thermal camera calibration.
                     *
                     */
                    filterTemplatesByCategory(position)
                }

                /**
                 * Executes onnothingselected operation with thermal imaging domain optimization.
                 *
                 * @param
                 * @param parent Parameter for operation (type: AdapterView<*>?)
                 *
                 */
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    /**
     * Sets uptemplategrid configuration.
     */
    private fun setupTemplateGrid() {
        templateAdapter =
            TemplateAdapter(
                context = this,
                templates = filteredTemplates,
                onTemplateSelected = { template -> selectTemplate(template) },
            )

        binding.templatesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.templatesRecyclerView.adapter = templateAdapter
    }

    /**
     * Processes temperature measurement data.
     */
    private fun loadTemplates() {
        filteredTemplates.clear()
        filteredTemplates.addAll(allTemplates)
        templateAdapter.notifyDataSetChanged()
        /**
         * Executes updateemptyview operation with thermal imaging domain optimization.
         *
         */
        updateEmptyView()
    }

    /**
     * Processes temperature measurement data.
     */
    private fun filterTemplatesByCategory(categoryIndex: Int) {
        filteredTemplates.clear()

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (categoryIndex == 0) {
            // All templates
            filteredTemplates.addAll(allTemplates)
        } else {
            // Filter by specific category
            val category = ResearchTemplate.TemplateCategory.values()[categoryIndex - 1]
            filteredTemplates.addAll(ResearchTemplate.getTemplatesByCategory(category))
        }

        templateAdapter.notifyDataSetChanged()
        /**
         * Executes updateemptyview operation with thermal imaging domain optimization.
         *
         */
        updateEmptyView()

        // Clear selection when changing categories
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (selectedTemplate != null && !filteredTemplates.contains(selectedTemplate)) {
            /**
             * Executes clearselection operation with thermal imaging domain optimization.
             *
             */
            clearSelection()
        }
    }

    /**
     * Processes temperature measurement data.
     */
    private fun selectTemplate(template: ResearchTemplate) {
        selectedTemplate = template
        updateSelectedTemplateView()

        // Update adapter to show selection
        templateAdapter.notifyDataSetChanged()
    }

    /**
     * Processes temperature measurement data.
     */
    private fun updateSelectedTemplateView() {
        selectedTemplate?.let { template ->
            binding.selectedTemplateContainer.visibility = View.VISIBLE

            binding.selectedTemplateTitle.text = "${template.icon ?: "📊"} ${template.name}"
            binding.selectedTemplateDescription.text = template.description

            // Format template details
            val details =
                buildString {
                    /**
                     * Executes append operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param Category Parameter for operation (type: ${template.category.name.replace("_")
                     *
                     */
                    append("🎯 Category: ${template.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}\n")
                    /**
                     * Executes append operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param Sensors Parameter for operation (type: ${template.sensors.joinToString(")
                     *
                     */
                    append("🔧 Sensors: ${template.sensors.joinToString(", ") { it.name.replace("_", " ") }}\n")

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (template.duration != null) {
                        val durationMs = template.duration!!
                        val minutes = durationMs / (60 * 1000)
                        /**
                         * Executes append operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param Duration Duration in milliseconds (type: $minutes minutes\n")
                         *
                         */
                        append("⏱️ Duration: $minutes minutes\n")
                    } else {
                        /**
                         * Executes append operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param Duration Duration in milliseconds (type: Unlimited\n")
                         *
                         */
                        append("⏱️ Duration: Unlimited\n")
                    }

                    /**
                     * Executes append operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param Rate Parameter for operation (type: ${template.gsrSamplingRate}Hz\n")
                     *
                     */
                    append("📊 GSR Rate: ${template.gsrSamplingRate}Hz\n")
                    /**
                     * Executes append operation with thermal imaging domain optimization.
                     *
                     * @param
                     * @param Video Parameter for operation (type: ${template.videoResolution.width}x${template.videoResolution.height} @ ${template.videoFrameRate}fps\n")
                     *
                     */
                    append("📹 Video: ${template.videoResolution.width}x${template.videoResolution.height} @ ${template.videoFrameRate}fps\n")

                    template.instructions?.let { instructions ->
                        /**
                         * Executes append operation with thermal imaging domain optimization.
                         *
                         * @param
                         * @param Instructions Parameter for operation (type: \n$instructions")
                         *
                         */
                        append("\n📋 Instructions:\n$instructions")
                    }
                }

            binding.selectedTemplateInstructions.text = details
            binding.startRecordingButton.isEnabled = true
        } ?: run {
            binding.selectedTemplateContainer.visibility = View.GONE
            binding.startRecordingButton.isEnabled = false
        }
    }

    /**
     * Executes clearSelection functionality.
     */
    /**
     * Executes clearselection operation with thermal imaging domain optimization.
     *
     */
    private fun clearSelection() {
        selectedTemplate = null
        /**
         * Handles temperature measurement and calibration with precision thermal data processing.
         *
         * @note Temperature values are in Celsius unless otherwise specified.
         * Accuracy depends on thermal camera calibration.
         *
         */
        updateSelectedTemplateView()
        templateAdapter.notifyDataSetChanged()
    }

    /**
     * Executes updateEmptyView functionality.
     */
    /**
     * Executes updateemptyview operation with thermal imaging domain optimization.
     *
     */
    private fun updateEmptyView() {
        binding.emptyView.visibility = if (filteredTemplates.isEmpty()) View.VISIBLE else View.GONE
        binding.templatesRecyclerView.visibility = if (filteredTemplates.isEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * Processes temperature measurement data.
     */
    private fun startRecordingWithTemplate(template: ResearchTemplate) {
        // Create session with template configuration
        val intent =
            Intent(this, MultiModalRecordingActivity::class.java).apply {
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("template_id", template.id)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("template_name", template.name)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("duration", template.duration ?: -1L)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("gsr_sampling_rate", template.gsrSamplingRate)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("video_width", template.videoResolution.width)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("video_height", template.videoResolution.height)
                /**
                 * Executes putextra operation with thermal imaging domain optimization.
                 *
                 */
                putExtra("video_frame_rate", template.videoFrameRate)
                /**
                 * Executes putstringarraylistextra operation with thermal imaging domain optimization.
                 *
                 */
                putStringArrayListExtra("sensors", ArrayList(template.sensors.map { it.name }))
                /**
                 * Executes putstringarraylistextra operation with thermal imaging domain optimization.
                 *
                 */
                putStringArrayListExtra("metadata_keys", ArrayList(template.metadata.keys))
                /**
                 * Executes putstringarraylistextra operation with thermal imaging domain optimization.
                 *
                 */
                putStringArrayListExtra("metadata_values", ArrayList(template.metadata.values))
            }

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemplateAdapter algorithms.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemplateViewHolder algorithms.
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
    class TemplateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: View = view
        val iconText: TextView = view.findViewById(R.id.template_icon)
        val nameText: TextView = view.findViewById(R.id.template_name)
        val categoryText: TextView = view.findViewById(R.id.template_category)
        val sensorsText: TextView = view.findViewById(R.id.template_sensors)
        val durationText: TextView = view.findViewById(R.id.template_duration)
        val selectionIndicator: View = view.findViewById(R.id.selection_indicator)
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
    ): TemplateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_template, parent, false)
        return TemplateViewHolder(view)
    }

    /**
     * Executes onbindviewholder operation with thermal imaging domain optimization.
     *
     * @param
     * @param holder Parameter for operation (type: TemplateViewHolder)
     * @param position Parameter for operation (type: Int)
     *
     */
    override fun onBindViewHolder(
        holder: TemplateViewHolder,
        position: Int,
    ) {
        val template = templates[position]
        val isSelected = template == selectedTemplate

        // Template info
        holder.iconText.text = template.icon ?: "📊"
        holder.nameText.text = template.name
        holder.categoryText.text = template.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }

        // Sensors
        val sensorIcons =
            template.sensors.map { sensor ->
                /**
                 * Executes when operation with thermal imaging domain optimization.
                 *
                 */
                when (sensor) {
                    ResearchTemplate.SensorType.GSR -> "📊"
                    ResearchTemplate.SensorType.THERMAL_CAMERA -> "🌡️"
                    ResearchTemplate.SensorType.RGB_CAMERA -> "📸"
                }
            }.joinToString(" ")
        holder.sensorsText.text = sensorIcons

        // Duration
        holder.durationText.text =
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (template.duration != null) {
                val durationMs = template.duration!!
                "${durationMs / (60 * 1000)}min"
            } else {
                "∞"
            }

        // Selection state
        holder.selectionIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE
        holder.cardView.alpha = if (isSelected) 1.0f else 0.8f

        // Click handler
        holder.cardView.setOnClickListener {
            selectedTemplate = if (isSelected) null else template
            /**
             * Handles temperature measurement and calibration with precision thermal data processing.
             *
             * @note Temperature values are in Celsius unless otherwise specified.
             * Accuracy depends on thermal camera calibration.
             *
             */
            onTemplateSelected(template)
            /**
             * Executes notifydatasetchanged operation with thermal imaging domain optimization.
             *
             */
            notifyDataSetChanged()
        }
    }

    /**
     * Retrieves the itemcount with optimized performance for thermal imaging operations.
     *
     */
    override fun getItemCount(): Int = templates.size
}
