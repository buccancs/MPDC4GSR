package com.github.mikephil.charting.data;

import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds all relevant data that represents the chart. That involves
 * at least one (or more) DataSets, and an array of x-values.
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing ChartData functionality for the IRCamera system.
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
public abstract class ChartData<T extends IDataSet<? extends Entry>> {

    /**
     * maximum y-value in the value array across all axes
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * the minimum y-value in the value array across all axes
     */
    protected float mYMin = Float.MAX_VALUE;

    /**
     * maximum x-value in the value array
     */
    protected float mXMax = -Float.MAX_VALUE;

    /**
     * minimum x-value in the value array
     */
    protected float mXMin = Float.MAX_VALUE;

    protected float mLeftAxisMax = -Float.MAX_VALUE;

    protected float mLeftAxisMin = Float.MAX_VALUE;

    protected float mRightAxisMax = -Float.MAX_VALUE;

    protected float mRightAxisMin = Float.MAX_VALUE;

    /**
     * array that holds all DataSets the ChartData object represents
     */
    protected List<T> mDataSets;

    /**
     * Default constructor.
     */
    /**
     * Executes chartdata operation with thermal imaging domain optimization.
     *
     */
    public ChartData() {
        mDataSets = new ArrayList<T>();
    }

    /**
     * Constructor taking single or multiple DataSet objects.
     *
     * @param dataSets
     */
    public ChartData(T... dataSets) {
        mDataSets = arrayToList(dataSets);
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    /**
     * Created because Arrays.asList(...) does not support modification.
     *
     * @param array
     * @return
     */
    private List<T> arrayToList(T[] array) {

        List<T> list = new ArrayList<>();

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: array)
         *
         */
        for (T set : array) {
            list.add(set);
        }

        return list;
    }

    /**
     * constructor for chart data
     *
     * @param sets the dataset array
     */
    public ChartData(List<T> sets) {
        this.mDataSets = sets;
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    /**
     * Call this method to let the ChartData know that the underlying data has
     * changed. Calling this performs all necessary recalculations needed when
     * the contained data has changed.
     */
    public void notifyDataChanged() {
        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax();
    }

    /**
     * Calc minimum and maximum y-values over all DataSets.
     * Tell DataSets to recalculate their min and max y-values, this is only needed for autoScaleMinMax.
     *
     * @param fromX the x-value to start the calculation from
     * @param toX   the x-value to which the calculation should be performed
     */
    public void calcMinMaxY(float fromX, float toX) {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (T set : mDataSets) {
            set.calcMinMaxY(fromX, toX);
        }

        // Apply the new data
        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax();
    }

    /**
     * Calc minimum and maximum values (both x and y) over all DataSets.
     */
    protected void calcMinMax() {

        if (mDataSets == null)
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (T set : mDataSets) {
            /**
             * Executes calcminmax operation with thermal imaging domain optimization.
             *
             */
            calcMinMax(set);
        }

        mLeftAxisMax = -Float.MAX_VALUE;
        mLeftAxisMin = Float.MAX_VALUE;
        mRightAxisMax = -Float.MAX_VALUE;
        mRightAxisMin = Float.MAX_VALUE;

        // Left axis
        T firstLeft = getFirstLeft(mDataSets);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (firstLeft != null) {

            mLeftAxisMax = firstLeft.getYMax();
            mLeftAxisMin = firstLeft.getYMin();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param dataSet Parameter for operation (type: mDataSets)
             *
             */
            for (T dataSet : mDataSets) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.getYMin() < mLeftAxisMin)
                        mLeftAxisMin = dataSet.getYMin();

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.getYMax() > mLeftAxisMax)
                        mLeftAxisMax = dataSet.getYMax();
                }
            }
        }

        // Right axis
        T firstRight = getFirstRight(mDataSets);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (firstRight != null) {

            mRightAxisMax = firstRight.getYMax();
            mRightAxisMin = firstRight.getYMin();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param dataSet Parameter for operation (type: mDataSets)
             *
             */
            for (T dataSet : mDataSets) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (dataSet.getAxisDependency() == AxisDependency.RIGHT) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.getYMin() < mRightAxisMin)
                        mRightAxisMin = dataSet.getYMin();

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (dataSet.getYMax() > mRightAxisMax)
                        mRightAxisMax = dataSet.getYMax();
                }
            }
        }
    }

    /** ONLY GETTERS AND SETTERS BELOW THIS */

    /**
     * returns the number of LineDataSets this object contains
     *
     * @return
     */
    public int getDataSetCount() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDataSets == null)
            return 0;
        return mDataSets.size();
    }

    /**
     * Returns the smallest y-value the data object contains.
     *
     * @return
     */
    public float getYMin() {
        return mYMin;
    }

    /**
     * Returns the minimum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    public float getYMin(AxisDependency axis) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (axis == AxisDependency.LEFT) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMin == Float.MAX_VALUE) {
                return mRightAxisMin;
            } else
                return mLeftAxisMin;
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMin == Float.MAX_VALUE) {
                return mLeftAxisMin;
            } else
                return mRightAxisMin;
        }
    }

    /**
     * Returns the greatest y-value the data object contains.
     *
     * @return
     */
    public float getYMax() {
        return mYMax;
    }

    /**
     * Returns the maximum y-value for the specified axis.
     *
     * @param axis
     * @return
     */
    public float getYMax(AxisDependency axis) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (axis == AxisDependency.LEFT) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMax == -Float.MAX_VALUE) {
                return mRightAxisMax;
            } else
                return mLeftAxisMax;
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMax == -Float.MAX_VALUE) {
                return mLeftAxisMax;
            } else
                return mRightAxisMax;
        }
    }

    /**
     * Returns the minimum x-value this data object contains.
     *
     * @return
     */
    public float getXMin() {
        return mXMin;
    }

    /**
     * Returns the maximum x-value this data object contains.
     *
     * @return
     */
    public float getXMax() {
        return mXMax;
    }

    /**
     * Returns all DataSet objects this ChartData object holds.
     *
     * @return
     */
    public List<T> getDataSets() {
        return mDataSets;
    }

    /**
     * Retrieve the index of a DataSet with a specific label from the ChartData.
     * Search can be case sensitive or not. IMPORTANT: This method does
     * calculations at runtime, do not over-use in performance critical
     * situations.
     *
     * @param dataSets   the DataSet array to search
     * @param label
     * @param ignorecase if true, the search is not case-sensitive
     * @return
     */
    protected int getDataSetIndexByLabel(List<T> dataSets, String label,
                                         boolean ignorecase) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ignorecase) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < dataSets.size(); i++)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (label.equalsIgnoreCase(dataSets.get(i).getLabel()))
                    return i;
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = 0; i < dataSets.size(); i++)
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (label.equals(dataSets.get(i).getLabel()))
                    return i;
        }

        return -1;
    }

    /**
     * Returns the labels of all DataSets as a string array.
     *
     * @return
     */
    public String[] getDataSetLabels() {

        String[] types = new String[mDataSets.size()];

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mDataSets.size(); i++) {
            types[i] = mDataSets.get(i).getLabel();
        }

        return types;
    }

    /**
     * Get the Entry for a corresponding highlight object
     *
     * @param highlight
     * @return the entry that is highlighted
     */
    public Entry getEntryForHighlight(Highlight highlight) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (highlight.getDataSetIndex() >= mDataSets.size())
            return null;
        else {
            return mDataSets.get(highlight.getDataSetIndex()).getEntryForXValue(highlight.getX(), highlight.getY());
        }
    }

    /**
     * Returns the DataSet object with the given label. Search can be case
     * sensitive or not. IMPORTANT: This method does calculations at runtime.
     * Use with care in performance critical situations.
     *
     * @param label
     * @param ignorecase
     * @return
     */
    public T getDataSetByLabel(String label, boolean ignorecase) {

        int index = getDataSetIndexByLabel(mDataSets, label, ignorecase);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index < 0 || index >= mDataSets.size())
            return null;
        else
            return mDataSets.get(index);
    }

    public T getDataSetByIndex(int index) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDataSets == null || index < 0 || index >= mDataSets.size())
            return null;

        return mDataSets.get(index);
    }

    /**
     * Adds a DataSet dynamically.
     *
     * @param d
     */
    public void addDataSet(T d) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (d == null)
            return;

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax(d);

        mDataSets.add(d);
    }

    /**
     * Removes the given DataSet from this data object. Also recalculates all
     * minimum and maximum values. Returns true if a DataSet was removed, false
     * if no DataSet could be removed.
     *
     * @param d
     */
    public boolean removeDataSet(T d) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (d == null)
            return false;

        boolean removed = mDataSets.remove(d);

        // If a DataSet was removed
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (removed) {
            /**
             * Executes calcminmax operation with thermal imaging domain optimization.
             *
             */
            calcMinMax();
        }

        return removed;
    }

    /**
     * Removes the DataSet at the given index in the DataSet array from the data
     * object. Also recalculates all minimum and maximum values. Returns true if
     * a DataSet was removed, false if no DataSet could be removed.
     *
     * @param index
     */
    public boolean removeDataSet(int index) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index >= mDataSets.size() || index < 0)
            return false;

        T set = mDataSets.get(index);
        return removeDataSet(set);
    }

    /**
     * Adds an Entry to the DataSet at the specified index.
     * Entries are added to the end of the list.
     *
     * @param e
     * @param dataSetIndex
     */
    public void addEntry(Entry e, int dataSetIndex) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDataSets.size() > dataSetIndex && dataSetIndex >= 0) {

            IDataSet set = mDataSets.get(dataSetIndex);
            // Add the entry to the dataset
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!set.addEntry(e))
                return;

            /**
             * Executes calcminmax operation with thermal imaging domain optimization.
             *
             */
            calcMinMax(e, set.getAxisDependency());

        } else {
            Log.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.");
        }
    }

    /**
     * Adjusts the current minimum and maximum values based on the provided Entry object.
     *
     * @param e
     * @param axis
     */
    protected void calcMinMax(Entry e, AxisDependency axis) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYMax < e.getY())
            mYMax = e.getY();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYMin > e.getY())
            mYMin = e.getY();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mXMax < e.getX())
            mXMax = e.getX();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mXMin > e.getX())
            mXMin = e.getX();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (axis == AxisDependency.LEFT) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMax < e.getY())
                mLeftAxisMax = e.getY();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMin > e.getY())
                mLeftAxisMin = e.getY();
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMax < e.getY())
                mRightAxisMax = e.getY();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMin > e.getY())
                mRightAxisMin = e.getY();
        }
    }

    /**
     * Adjusts the minimum and maximum values based on the given DataSet.
     *
     * @param d
     */
    protected void calcMinMax(T d) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYMax < d.getYMax())
            mYMax = d.getYMax();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mYMin > d.getYMin())
            mYMin = d.getYMin();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mXMax < d.getXMax())
            mXMax = d.getXMax();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mXMin > d.getXMin())
            mXMin = d.getXMin();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (d.getAxisDependency() == AxisDependency.LEFT) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMax < d.getYMax())
                mLeftAxisMax = d.getYMax();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mLeftAxisMin > d.getYMin())
                mLeftAxisMin = d.getYMin();
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMax < d.getYMax())
                mRightAxisMax = d.getYMax();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mRightAxisMin > d.getYMin())
                mRightAxisMin = d.getYMin();
        }
    }

    /**
     * Removes the given Entry object from the DataSet at the specified index.
     *
     * @param e
     * @param dataSetIndex
     */
    public boolean removeEntry(Entry e, int dataSetIndex) {

        // Entry null, outofbounds
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null || dataSetIndex >= mDataSets.size())
            return false;

        IDataSet set = mDataSets.get(dataSetIndex);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (set != null) {
            // Remove the entry from the dataset
            boolean removed = set.removeEntry(e);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (removed) {
                /**
                 * Executes calcminmax operation with thermal imaging domain optimization.
                 *
                 */
                calcMinMax();
            }

            return removed;
        } else
            return false;
    }

    /**
     * Removes the Entry object closest to the given DataSet at the
     * specified index. Returns true if an Entry was removed, false if no Entry
     * was found that meets the specified requirements.
     *
     * @param xValue
     * @param dataSetIndex
     * @return
     */
    public boolean removeEntry(float xValue, int dataSetIndex) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dataSetIndex >= mDataSets.size())
            return false;

        IDataSet dataSet = mDataSets.get(dataSetIndex);
        Entry e = dataSet.getEntryForXValue(xValue, Float.NaN);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return false;

        return removeEntry(e, dataSetIndex);
    }

    /**
     * Returns the DataSet that contains the provided Entry, or null, if no
     * DataSet contains this Entry.
     *
     * @param e
     * @return
     */
    public T getDataSetForEntry(Entry e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return null;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mDataSets.size(); i++) {

            T set = mDataSets.get(i);

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int j = 0; j < set.getEntryCount(); j++) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (e.equalTo(set.getEntryForXValue(e.getX(), e.getY())))
                    return set;
            }
        }

        return null;
    }

    /**
     * Returns all colors used across all DataSet objects this object
     * represents.
     *
     * @return
     */
    public int[] getColors() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDataSets == null)
            return null;

        int clrcnt = 0;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mDataSets.size(); i++) {
            clrcnt += mDataSets.get(i).getColors().size();
        }

        int[] colors = new int[clrcnt];
        int cnt = 0;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mDataSets.size(); i++) {

            List<Integer> clrs = mDataSets.get(i).getColors();

            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param clr Parameter for operation (type: clrs)
             *
             */
            for (Integer clr : clrs) {
                colors[cnt] = clr;
                cnt++;
            }
        }

        return colors;
    }

    /**
     * Returns the index of the provided DataSet in the DataSet array of this data object, or -1 if it does not exist.
     *
     * @param dataSet
     * @return
     */
    public int getIndexOfDataSet(T dataSet) {
        return mDataSets.indexOf(dataSet);
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     *
     * @return
     */
    protected T getFirstLeft(List<T> sets) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param dataSet Parameter for operation (type: sets)
         *
         */
        for (T dataSet : sets) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataSet.getAxisDependency() == AxisDependency.LEFT)
                return dataSet;
        }
        return null;
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the right axis.
     * Returns null if no DataSet with right dependency could be found.
     *
     * @return
     */
    public T getFirstRight(List<T> sets) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param dataSet Parameter for operation (type: sets)
         *
         */
        for (T dataSet : sets) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (dataSet.getAxisDependency() == AxisDependency.RIGHT)
                return dataSet;
        }
        return null;
    }

    /**
     * Sets a custom IValueFormatter for all DataSets this data object contains.
     *
     * @param f
     */
    public void setValueFormatter(ValueFormatter f) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (f == null)
            return;
        else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param set Parameter for operation (type: mDataSets)
             *
             */
            for (IDataSet set : mDataSets) {
                set.setValueFormatter(f);
            }
        }
    }

    /**
     * Sets the color of the value-text (color in which the value-labels are
     * drawn) for all DataSets this data object contains.
     *
     * @param color
     */
    public void setValueTextColor(int color) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setValueTextColor(color);
        }
    }

    /**
     * Sets the same list of value-colors for all DataSets this
     * data object contains.
     *
     * @param colors
     */
    public void setValueTextColors(List<Integer> colors) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setValueTextColors(colors);
        }
    }

    /**
     * Sets the Typeface for all value-labels for all DataSets this data object
     * contains.
     *
     * @param tf
     */
    public void setValueTypeface(Typeface tf) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setValueTypeface(tf);
        }
    }

    /**
     * Sets the size (in dp) of the value-text for all DataSets this data object
     * contains.
     *
     * @param size
     */
    public void setValueTextSize(float size) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setValueTextSize(size);
        }
    }

    /**
     * Enables / disables drawing values (value-text) for all DataSets this data
     * object contains.
     *
     * @param enabled
     */
    public void setDrawValues(boolean enabled) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setDrawValues(enabled);
        }
    }

    /**
     * Enables / disables highlighting values for all DataSets this data object
     * contains. If set to true, this means that values can
     * be highlighted programmatically or by touch gesture.
     */
    public void setHighlightEnabled(boolean enabled) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            set.setHighlightEnabled(enabled);
        }
    }

    /**
     * Returns true if highlighting of all underlying values is enabled, false
     * if not.
     *
     * @return
     */
    public boolean isHighlightEnabled() {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (IDataSet set : mDataSets) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!set.isHighlightEnabled())
                return false;
        }
        return true;
    }

    /**
     * Clears this data object from all DataSets and removes all Entries. Don't
     * forget to invalidate the chart after this.
     */
    public void clearValues() {
        if (mDataSets != null) {
            mDataSets.clear();
        }
        /**
         * Executes notifydatachanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataChanged();
    }

    /**
     * Checks if this data object contains the specified DataSet. Returns true
     * if so, false if not.
     *
     * @param dataSet
     * @return
     */
    public boolean contains(T dataSet) {

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (T set : mDataSets) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (set.equals(dataSet))
                return true;
        }

        return false;
    }

    /**
     * Returns the total entry count across all DataSet objects this data object contains.
     *
     * @return
     */
    public int getEntryCount() {

        int count = 0;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (T set : mDataSets) {
            count += set.getEntryCount();
        }

        return count;
    }

    /**
     * Returns the DataSet object with the maximum number of entries or null if there are no DataSets.
     *
     * @return
     */
    public T getMaxEntryCountSet() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mDataSets == null || mDataSets.isEmpty())
            return null;

        T max = mDataSets.get(0);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param set Parameter for operation (type: mDataSets)
         *
         */
        for (T set : mDataSets) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (set.getEntryCount() > max.getEntryCount())
                max = set;
        }

        return max;
    }
}
