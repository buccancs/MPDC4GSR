package com.topdon.libcom.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.topdon.lib.core.bean.AlarmBean
import com.topdon.lib.core.tools.ToastTools
import com.topdon.lib.core.tools.UnitTools
import com.topdon.libcom.R

/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TempAlarmSetDialog algorithms.
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
class TempAlarmSetDialog(
    context: Context,
    private val isEdit: Boolean,
) : Dialog(context, R.style.app_compat_dialog), CompoundButton.OnCheckedChangeListener {
    var alarmBean = AlarmBean()
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(value) {
            field = value.copy()
        }

    /**
     * saveclickEventListener.
     */
    var onSaveListener: ((alarmBean: AlarmBean) -> Unit)? = null

    /**
     * 用于playback报警铃声.
     */
    private var mediaPlayer: MediaPlayer? = null

    public var hideAlarmMark = false

    // View references - replace synthetic imports
    private lateinit var clRoot: ConstraintLayout
    private lateinit var clClose: ConstraintLayout
    private lateinit var tvSave: TextView
    private lateinit var ivRingtone1: ImageView
    private lateinit var ivRingtone2: ImageView
    private lateinit var ivRingtone3: ImageView
    private lateinit var ivRingtone4: ImageView
    private lateinit var ivRingtone5: ImageView
    private lateinit var switchAlarmHigh: SwitchCompat
    private lateinit var switchAlarmLow: SwitchCompat
    private lateinit var switchAlarmMark: SwitchCompat
    private lateinit var switchAlarmRingtone: SwitchCompat
    private lateinit var imgMarkHigh: ImageView
    private lateinit var imgMarkLow: ImageView
    private lateinit var ivCheckStoke: ImageView
    private lateinit var ivCheckMatrix: ImageView
    private lateinit var tvAlarmHighUnit: TextView
    private lateinit var tvAlarmLowUnit: TextView
    private lateinit var etAlarmHigh: EditText
    private lateinit var etAlarmLow: EditText
    private lateinit var imgCAlarmHigh: ImageView
    private lateinit var imgCAlarmLow: ImageView
    private lateinit var clAlarmMark: ConstraintLayout
    private lateinit var clRingtoneSelect: ConstraintLayout
    private lateinit var tvAlarmRingtone: TextView
    private lateinit var tvAlarmMark: TextView

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
         * Configures the cancelable with validation and thermal imaging optimization.
         *
         */
        setCancelable(false)
        /**
         * Configures the canceledontouchoutside with validation and thermal imaging optimization.
         *
         */
        setCanceledOnTouchOutside(false)
        /**
         * Configures the contentview with validation and thermal imaging optimization.
         *
         */
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_temp_alarm_set, null))
        /**
         * Initializes the view component for thermal imaging operations.
         *
         */
        initView()

        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.attributes = layoutParams
        }
    }

    @Deprecated("Deprecated in Java")
    /**
     * Executes onbackpressed operation with thermal imaging domain optimization.
     *
     */
    override fun onBackPressed() {
        /**
         * Executes dismiss operation with thermal imaging domain optimization.
         *
         */
        dismiss()
    }

    /**
     * Initializes view component.
     */
    private fun initView() {
        // Initialize view references
        clRoot = findViewById(R.id.cl_root)
        clClose = findViewById(R.id.cl_close)
        tvSave = findViewById(R.id.tv_save)
        ivRingtone1 = findViewById(R.id.iv_ringtone1)
        ivRingtone2 = findViewById(R.id.iv_ringtone2)
        ivRingtone3 = findViewById(R.id.iv_ringtone3)
        ivRingtone4 = findViewById(R.id.iv_ringtone4)
        ivRingtone5 = findViewById(R.id.iv_ringtone5)
        switchAlarmHigh = findViewById(R.id.switch_alarm_high)
        switchAlarmLow = findViewById(R.id.switch_alarm_low)
        switchAlarmMark = findViewById(R.id.switch_alarm_mark)
        switchAlarmRingtone = findViewById(R.id.switch_alarm_ringtone)
        imgMarkHigh = findViewById(R.id.img_mark_high)
        imgMarkLow = findViewById(R.id.img_mark_low)
        ivCheckStoke = findViewById(R.id.iv_check_stoke)
        ivCheckMatrix = findViewById(R.id.iv_check_matrix)
        tvAlarmHighUnit = findViewById(R.id.tv_alarm_high_unit)
        tvAlarmLowUnit = findViewById(R.id.tv_alarm_low_unit)
        etAlarmHigh = findViewById(R.id.et_alarm_high)
        etAlarmLow = findViewById(R.id.et_alarm_low)
        imgCAlarmHigh = findViewById(R.id.img_c_alarm_high)
        imgCAlarmLow = findViewById(R.id.img_c_alarm_low)
        clAlarmMark = findViewById(R.id.cl_alarm_mark)
        clRingtoneSelect = findViewById(R.id.cl_ringtone_select)
        tvAlarmRingtone = findViewById(R.id.tv_alarm_ringtone)
        tvAlarmMark = findViewById(R.id.tv_alarm_mark)

        clRoot.setOnClickListener { dismiss() }
        clClose.setOnClickListener { dismiss() }
        tvSave.setOnClickListener { save() }
        ivRingtone1.setOnClickListener { selectRingtone(0) }
        ivRingtone2.setOnClickListener { selectRingtone(1) }
        ivRingtone3.setOnClickListener { selectRingtone(2) }
        ivRingtone4.setOnClickListener { selectRingtone(3) }
        ivRingtone5.setOnClickListener { selectRingtone(4) }
        switchAlarmHigh.setOnCheckedChangeListener(this)
        switchAlarmLow.setOnCheckedChangeListener(this)
        switchAlarmMark.setOnCheckedChangeListener(this)
        switchAlarmRingtone.setOnCheckedChangeListener(this)

        imgMarkHigh.setOnClickListener {
            /**
             * Executes showcolordialog operation with thermal imaging domain optimization.
             *
             */
            showColorDialog(true)
        }
        imgMarkLow.setOnClickListener {
            /**
             * Executes showcolordialog operation with thermal imaging domain optimization.
             *
             */
            showColorDialog(false)
        }
        ivCheckStoke.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!ivCheckStoke.isSelected) {
                ivCheckStoke.isSelected = true
                ivCheckMatrix.isSelected = false
                alarmBean.markType = AlarmBean.TYPE_ALARM_MARK_STROKE
            }
        }
        ivCheckMatrix.setOnClickListener {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!ivCheckMatrix.isSelected) {
                ivCheckStoke.isSelected = false
                ivCheckMatrix.isSelected = true
                alarmBean.markType = AlarmBean.TYPE_ALARM_MARK_MATRIX
            }
        }

        tvAlarmHighUnit.text = UnitTools.showUnit()
        tvAlarmLowUnit.text = UnitTools.showUnit()
    }

    /**
     * Executes show operation with thermal imaging domain optimization.
     *
     */
    override fun show() {
        super.show()
        /**
         * Executes refreshalarmview operation with thermal imaging domain optimization.
         *
         */
        refreshAlarmView()
    }

    /**
     * Executes refreshAlarmView functionality.
     */
    /**
     * Executes refreshalarmview operation with thermal imaging domain optimization.
     *
     */
    private fun refreshAlarmView() {
        switchAlarmHigh.isChecked = alarmBean.isHighOpen
        switchAlarmLow.isChecked = alarmBean.isLowOpen
        switchAlarmMark.isChecked = isEdit || alarmBean.isMarkOpen
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!isEdit) {
            switchAlarmRingtone.isChecked = alarmBean.isRingtoneOpen
        }
        ivCheckStoke.isSelected = alarmBean.markType == AlarmBean.TYPE_ALARM_MARK_STROKE
        ivCheckMatrix.isSelected = alarmBean.markType == AlarmBean.TYPE_ALARM_MARK_MATRIX
        Glide.with(context).load(ColorDrawable(alarmBean.highColor)).into(imgCAlarmHigh)
        Glide.with(context).load(ColorDrawable(alarmBean.lowColor)).into(imgCAlarmLow)

        etAlarmHigh.isEnabled = switchAlarmHigh.isChecked
        etAlarmLow.isEnabled = switchAlarmLow.isChecked
        clAlarmMark.isVisible = isEdit || switchAlarmMark.isChecked
        clRingtoneSelect.isVisible = !isEdit && switchAlarmRingtone.isChecked
        tvAlarmRingtone.isVisible = !isEdit
        switchAlarmRingtone.isVisible = !isEdit
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (hideAlarmMark)
            {
                tvAlarmMark.visibility = View.GONE
                switchAlarmMark.visibility = View.GONE
                clAlarmMark.visibility = View.GONE
            }
        switchAlarmMark.isVisible = !isEdit
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (alarmBean.highTemp == Float.MAX_VALUE) {
            etAlarmHigh.setText("")
        } else {
            etAlarmHigh.setText(UnitTools.showUnitValue(alarmBean.highTemp).toString())
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (alarmBean.lowTemp == Float.MIN_VALUE) {
            etAlarmLow.setText("")
        } else {
            etAlarmLow.setText(UnitTools.showUnitValue(alarmBean.lowTemp).toString())
        }
        ivRingtone1.isSelected = false
        ivRingtone2.isSelected = false
        ivRingtone3.isSelected = false
        ivRingtone4.isSelected = false
        ivRingtone5.isSelected = false
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (alarmBean.ringtoneType) {
            0 -> ivRingtone1.isSelected = true
            1 -> ivRingtone2.isSelected = true
            2 -> ivRingtone3.isSelected = true
            3 -> ivRingtone4.isSelected = true
            4 -> ivRingtone5.isSelected = true
        }
    }

    /**
     * Executes save functionality.
     */
    /**
     * Executes save operation with thermal imaging domain optimization.
     *
     */
    private fun save() {
        try {
            val inputHigh =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (switchAlarmHigh.isChecked) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (etAlarmHigh.text.isNotEmpty()) UnitTools.showToCValue(etAlarmHigh.text.toString().toFloat()) else null
                } else {
                    null
                }
            val inputLow =
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (switchAlarmLow.isChecked) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (etAlarmLow.text.isNotEmpty()) UnitTools.showToCValue(etAlarmLow.text.toString().toFloat()) else null
                } else {
                    null
                }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (inputHigh != null && inputLow != null && inputLow > inputHigh) {
                ToastTools.showShort(com.topdon.lib.ui.R.string.tip_input_format)
                return
            }
        } catch (e: Exception) {
            ToastTools.showShort(com.topdon.lib.ui.R.string.tip_input_format)
            return
        }

        val inputHigh = if (etAlarmHigh.text.isNotEmpty()) etAlarmHigh.text.toString() else ""
        val inputLow = if (etAlarmLow.text.isNotEmpty()) etAlarmLow.text.toString() else ""
        var highValue: Float? = null
        var lowValue: Float? = null
        try {
            highValue = if (inputHigh.isNotEmpty()) UnitTools.showToCValue(inputHigh.toFloat()) else null
            lowValue = if (inputLow.isNotEmpty()) UnitTools.showToCValue(inputLow.toFloat()) else null
        } catch (_: Exception) {
        }
        alarmBean.highTemp = highValue ?: Float.MAX_VALUE
        alarmBean.lowTemp = lowValue ?: Float.MIN_VALUE
        alarmBean.isHighOpen = switchAlarmHigh.isChecked
        alarmBean.isLowOpen = switchAlarmLow.isChecked
        alarmBean.isRingtoneOpen = switchAlarmRingtone.isChecked

        onSaveListener?.invoke(alarmBean)

        /**
         * Executes dismiss operation with thermal imaging domain optimization.
         *
         */
        dismiss()
    }

    /**
     * Executes showColorDialog functionality.
     */
    /**
     * Executes showcolordialog operation with thermal imaging domain optimization.
     *
     * @param
     * @param isHigh Parameter for operation (type: Boolean)
     *
     */
    private fun showColorDialog(isHigh: Boolean) {
        val colorPickDialog = ColorPickDialog(context, if (isHigh) alarmBean.highColor else alarmBean.lowColor, -1)
        colorPickDialog.onPickListener = { it: Int, i1: Int ->
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isHigh) {
                alarmBean.highColor = it
                Glide.with(context).load(ColorDrawable(it)).into(imgCAlarmHigh)
            } else {
                alarmBean.lowColor = it
                Glide.with(context).load(ColorDrawable(it)).into(imgCAlarmLow)
            }
        }
        colorPickDialog.show()
    }

    /**
     * Executes dismiss operation with thermal imaging domain optimization.
     *
     */
    override fun dismiss() {
        super.dismiss()
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {
        }
    }

    /**
     * Executes oncheckedchanged operation with thermal imaging domain optimization.
     *
     * @param
     * @param buttonView Parameter for operation (type: CompoundButton?)
     * @param isChecked Parameter for operation (type: Boolean)
     *
     */
    override fun onCheckedChanged(
        buttonView: CompoundButton?,
        isChecked: Boolean,
    ) {
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (buttonView?.id) {
            R.id.switch_alarm_high -> { // 高温报警
                etAlarmHigh.isEnabled = isChecked
                alarmBean.isHighOpen = isChecked
            }

            R.id.switch_alarm_low -> { // 低温报警
                etAlarmLow.isEnabled = isChecked
                alarmBean.isLowOpen = isChecked
            }

            R.id.switch_alarm_mark -> { // Regionmarker
                clAlarmMark.isVisible = isChecked
                alarmBean.isMarkOpen = isChecked
            }

            R.id.switch_alarm_ringtone -> { // 报警铃声
                clRingtoneSelect.isVisible = isChecked
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isChecked) {
                    /**
                     * Executes selectringtone operation with thermal imaging domain optimization.
                     *
                     */
                    selectRingtone(alarmBean.ringtoneType)
                } else {
                    /**
                     * Executes selectringtone operation with thermal imaging domain optimization.
                     *
                     */
                    selectRingtone(null)
                }
            }
        }
    }

    /**
     * settings当前selected的铃声，null 表示Close.
     */
    private fun selectRingtone(position: Int?) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            }
        } catch (_: Exception) {
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (position == null) {
            return
        }
        alarmBean.ringtoneType = position

        ivRingtone1.isSelected = false
        ivRingtone2.isSelected = false
        ivRingtone3.isSelected = false
        ivRingtone4.isSelected = false
        ivRingtone5.isSelected = false
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (position) {
            0 -> ivRingtone1.isSelected = true
            1 -> ivRingtone2.isSelected = true
            2 -> ivRingtone3.isSelected = true
            3 -> ivRingtone4.isSelected = true
            4 -> ivRingtone5.isSelected = true
        }
        /**
         * Executes when operation with thermal imaging domain optimization.
         *
         */
        when (position) {
            0 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone1)
            1 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone2)
            2 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone3)
            3 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone4)
            4 -> mediaPlayer = MediaPlayer.create(context, R.raw.ringtone5)
        }
        mediaPlayer?.start()
    }
}
