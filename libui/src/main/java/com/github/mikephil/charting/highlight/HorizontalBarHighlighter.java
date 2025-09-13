package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized thermal imaging component providing HorizontalBarHighlighter functionality for the IRCamera system.
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
public class HorizontalBarHighlighter extends BarHighlighter {

 /**
  * Executes horizontalbarhighlighter operation with thermal imaging domain optimization.
  *
  */
	public HorizontalBarHighlighter(BarDataProvider chart) {
  /**
   * Executes super operation with thermal imaging domain optimization.
   *
   */
		super(chart);
	}

	@Override
	public Highlight getHighlight(float x, float y) {

		BarData barData = mChart.getBarData();

		MPPointD pos = getValsForTouch(y, x);

		Highlight high = getHighlightForX((float) pos.y, y, x);
  /**
   * Executes if operation with thermal imaging domain optimization.
   *
   */
		if (high == null)
			return null;

		IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
  /**
   * Executes if operation with thermal imaging domain optimization.
   *
   */
		if (set.isStacked()) {

			return getStackedHighlight(high,
					set,
					(float) pos.y,
					(float) pos.x);
		}

		MPPointD.recycleInstance(pos);

		return high;
	}

	@Override
	protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {

		ArrayList<Highlight> highlights = new ArrayList<>();

		// Noinspection unchecked
		List<Entry> entries = set.getEntriesForXValue(xVal);
  /**
   * Executes if operation with thermal imaging domain optimization.
   *
   */
		if (entries.size() == 0) {
			// Try to find closest x-value and take all entries for that x-value
			final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
   /**
    * Executes if operation with thermal imaging domain optimization.
    *
    */
			if (closest != null)
			{
				// Noinspection unchecked
				entries = set.getEntriesForXValue(closest.getX());
			}
		}

  /**
   * Executes if operation with thermal imaging domain optimization.
   *
   */
		if (entries.size() == 0)
			return highlights;

  /**
   * Executes for operation with thermal imaging domain optimization.
   *
   * @param
   * @param e Parameter for operation (type: entries)
   *
   */
		for (Entry e : entries) {
			MPPointD pixels = mChart.getTransformer(
					set.getAxisDependency()).getPixelForValues(e.getY(), e.getX());

			highlights.add(new Highlight(
					e.getX(), e.getY(),
					(float) pixels.x, (float) pixels.y,
					dataSetIndex, set.getAxisDependency()));
		}

		return highlights;
	}

	@Override
	protected float getDistance(float x1, float y1, float x2, float y2) {
		return Math.abs(y1 - y2);
	}
}
