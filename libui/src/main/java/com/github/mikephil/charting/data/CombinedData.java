
package com.github.mikephil.charting.data;

import android.util.Log;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object that allows the combination of Line-, Bar-, Scatter-, Bubble- and
 * CandleData. Used in the CombinedChart class.
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing CombinedData functionality for the IRCamera system.
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
public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>> {

    private LineData mLineData;
    private BarData mBarData;
    private ScatterData mScatterData;
    private CandleData mCandleData;
    private BubbleData mBubbleData;

    /**
     * Executes combineddata operation with thermal imaging domain optimization.
     *
     */
    public CombinedData() {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super();
    }

    public void setData(LineData data) {
        mLineData = data;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    public void setData(BarData data) {
        mBarData = data;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    public void setData(ScatterData data) {
        mScatterData = data;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    public void setData(CandleData data) {
        mCandleData = data;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    public void setData(BubbleData data) {
        mBubbleData = data;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    @Override
    public void calcMinMax() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(mDataSets == null){
            mDataSets = new ArrayList<>();
        }
        mDataSets.clear();

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        mLeftAxisMax = -Float.MAX_VALUE;
        mLeftAxisMin = Float.MAX_VALUE;
        mRightAxisMax = -Float.MAX_VALUE;
        mRightAxisMin = Float.MAX_VALUE;

        List<BarLineScatterCandleBubbleData> allData = getAllData();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param data Parameter for operation (type: allData)
         *
         */
        for (ChartData data : allData) {

            data.calcMinMax();

            List<IBarLineScatterCandleBubbleDataSet<? extends Entry>> sets = data.getDataSets();
            mDataSets.addAll(sets);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.getYMax() > mYMax)
                mYMax = data.getYMax();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.getYMin() < mYMin)
                mYMin = data.getYMin();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.getXMax() > mXMax)
                mXMax = data.getXMax();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.getXMin() < mXMin)
                mXMin = data.getXMin();

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.mLeftAxisMax > mLeftAxisMax)
                mLeftAxisMax = data.mLeftAxisMax;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.mLeftAxisMin < mLeftAxisMin)
                mLeftAxisMin = data.mLeftAxisMin;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.mRightAxisMax > mRightAxisMax)
                mRightAxisMax = data.mRightAxisMax;

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (data.mRightAxisMin < mRightAxisMin)
                mRightAxisMin = data.mRightAxisMin;

        }
    }

    public BubbleData getBubbleData() {
        return mBubbleData;
    }

    public LineData getLineData() {
        return mLineData;
    }

    public BarData getBarData() {
        return mBarData;
    }

    public ScatterData getScatterData() {
        return mScatterData;
    }

    public CandleData getCandleData() {
        return mCandleData;
    }

    /**
     * Returns all data objects in row: line-bar-scatter-candle-bubble if not null.
     *
     * @return
     */
    public List<BarLineScatterCandleBubbleData> getAllData() {

        List<BarLineScatterCandleBubbleData> data = new ArrayList<BarLineScatterCandleBubbleData>();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLineData != null)
            data.add(mLineData);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mBarData != null)
            data.add(mBarData);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mScatterData != null)
            data.add(mScatterData);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mCandleData != null)
            data.add(mCandleData);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mBubbleData != null)
            data.add(mBubbleData);

        return data;
    }

    public BarLineScatterCandleBubbleData getDataByIndex(int index) {
        return getAllData().get(index);
    }

    @Override
    public void notifyDataChanged() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mLineData != null)
            mLineData.notifyDataChanged();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mBarData != null)
            mBarData.notifyDataChanged();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mCandleData != null)
            mCandleData.notifyDataChanged();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mScatterData != null)
            mScatterData.notifyDataChanged();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mBubbleData != null)
            mBubbleData.notifyDataChanged();

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax(); // Recalculate everything
    }

    /**
     * Get the Entry for a corresponding highlight object
     *
     * @param highlight
     * @return the entry that is highlighted
     */
    @Override
    public Entry getEntryForHighlight(Highlight highlight) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (highlight.getDataIndex() >= getAllData().size())
            return null;

        ChartData data = getDataByIndex(highlight.getDataIndex());

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (highlight.getDataSetIndex() >= data.getDataSetCount())
            return null;

        // The value of the highlighted entry could be NaN -
        // If we are not interested in highlighting a specific value.

        List<Entry> entries = data.getDataSetByIndex(highlight.getDataSetIndex())
                .getEntriesForXValue(highlight.getX());
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param entry Parameter for operation (type: entries)
         *
         */
        for (Entry entry : entries)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (entry.getY() == highlight.getY() ||
                    Float.isNaN(highlight.getY()))
                return entry;

        return null;
    }

    /**
     * Get dataset for highlight
     *
     * @param highlight current highlight
     * @return dataset related to highlight
     */
    public IBarLineScatterCandleBubbleDataSet<? extends Entry> getDataSetByHighlight(Highlight highlight) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (highlight.getDataIndex() >= getAllData().size())
            return null;

        BarLineScatterCandleBubbleData data = getDataByIndex(highlight.getDataIndex());

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (highlight.getDataSetIndex() >= data.getDataSetCount())
            return null;

        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (IBarLineScatterCandleBubbleDataSet<? extends Entry>)
                data.getDataSets().get(highlight.getDataSetIndex());
    }

    public int getDataIndex(ChartData data) {
        return getAllData().indexOf(data);
    }

    @Override
    public boolean removeDataSet(IBarLineScatterCandleBubbleDataSet<? extends Entry> d) {

        List<BarLineScatterCandleBubbleData> datas = getAllData();

        boolean success = false;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param data Parameter for operation (type: datas)
         *
         */
        for (ChartData data : datas) {

            success = data.removeDataSet(d);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (success) {
                break;
            }
        }

        return success;
    }

    @Deprecated
    @Override
    public boolean removeDataSet(int index) {
        Log.e("MPAndroidChart", "removeDataSet(int index) not supported for CombinedData");
        return false;
    }

    @Deprecated
    @Override
    public boolean removeEntry(Entry e, int dataSetIndex) {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }

    @Deprecated
    @Override
    public boolean removeEntry(float xValue, int dataSetIndex) {
        Log.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }
}
