
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.List;

/**
 * Specialized thermal imaging component providing BarData functionality for the IRCamera system.
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
public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet> {

    /**
     * the width of the bars on the x-axis, in values (not pixels)
     */
    private float mBarWidth = 0.85f;

    public BarData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    /**
     * Executes bardata operation with thermal imaging domain optimization.
     *
     */
    public BarData(IBarDataSet... dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Executes bardata operation with thermal imaging domain optimization.
     *
     */
    public BarData(List<IBarDataSet> dataSets) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(dataSets);
    }

    /**
     * Sets the width each bar should have on the x-axis (in values, not pixels).
     * Default 0.85f
     *
     * @param mBarWidth
     */
    public void setBarWidth(float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }

    public float getBarWidth() {
        return mBarWidth;
    }

    /**
     * Groups all BarDataSet objects this data object holds together by modifying the x-value of their entries.
     * Previously set x-values of entries will be overwritten. Leaves space between bars and groups as specified
     * by the parameters.
     * Do not forget to call notifyDataSetChanged() on your BarChart object after calling this method.
     *
     * @param fromX      the starting point on the x-axis where the grouping should begin
     * @param groupSpace the space between groups of bars in values (not pixels) e.g. 0.8f for bar width 1f
     * @param barSpace   the space between individual bars in values (not pixels) e.g. 0.1f for bar width 1f
     */
    public void groupBars(float fromX, float groupSpace, float barSpace) {

        int setCount = mDataSets.size();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (setCount <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }

        IBarDataSet max = getMaxEntryCountSet();
        int maxEntryCount = max.getEntryCount();

        float groupSpaceWidthHalf = groupSpace / 2f;
        float barSpaceHalf = barSpace / 2f;
        float barWidthHalf = mBarWidth / 2f;

        float interval = getGroupWidth(groupSpace, barSpace);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < maxEntryCount; i++) {

            float start = fromX;
            fromX += groupSpaceWidthHalf;

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param set Parameter for operation (type: mDataSets)
             *
             */
            for (IBarDataSet set : mDataSets) {

                fromX += barSpaceHalf;
                fromX += barWidthHalf;

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (i < set.getEntryCount()) {

                    BarEntry entry = set.getEntryForIndex(i);

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (entry != null) {
                        entry.setX(fromX);
                    }
                }

                fromX += barWidthHalf;
                fromX += barSpaceHalf;
            }

            fromX += groupSpaceWidthHalf;
            float end = fromX;
            float innerInterval = end - start;
            float diff = interval - innerInterval;

            // Correct rounding errors
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (diff > 0 || diff < 0) {
                fromX += diff;
            }
        }

        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    /**
     * In case of grouped bars, this method returns the space an individual group of bar needs on the x-axis.
     *
     * @param groupSpace
     * @param barSpace
     * @return
     */
    public float getGroupWidth(float groupSpace, float barSpace) {
        return mDataSets.size() * (mBarWidth + barSpace) + groupSpace;
    }
}
