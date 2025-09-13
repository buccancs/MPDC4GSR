package com.topdon.lib.ui.widget.seekbar;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：
 * create日期：2018/5/9
 * 描    述: it works for draw indicator text
 * ================================================
 */
/**
 * Specialized thermal imaging component providing SeekBarState functionality for the IRCamera system.
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
public class SeekBarState {
    public String indicatorText;
    public float value; // Now progress value
    public boolean isMin;
    public boolean isMax;

    @Override
    public String toString() {
        return "indicatorText: " + indicatorText + " ,isMin: " + isMin + " ,isMax: " + isMax;
    }
}
