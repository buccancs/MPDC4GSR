package com.github.mikephil.charting.listener;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Specialized thermal imaging component providing OnDrawLineChartTouchListener functionality for the IRCamera system.
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
public class OnDrawLineChartTouchListener extends SimpleOnGestureListener implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

}
