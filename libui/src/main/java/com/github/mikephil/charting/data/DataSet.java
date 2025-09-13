
package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;

/**
 * The DataSet class represents one group or type of entries (Entry) in the
 * Chart that belong together. It is designed to logically separate different
 * groups of values inside the Chart (e.g. the values for a specific line in the
 * LineChart, or the values of a specific group of bars in the BarChart).
 *
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing DataSet functionality for the IRCamera system.
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
public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {

    /**
     * the entries that this DataSet represents / holds together
     */
    protected List<T> mValues = null;

    /**
     * maximum y-value in the value array
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * minimum y-value in the value array
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

    /**
     * Creates a new DataSet object with the given values (entries) it represents. Also, a
     * label that describes the DataSet can be specified. The label can also be
     * used to retrieve the DataSet from a ChartData object.
     *
     * @param values
     * @param label
     */
    /**
     * Executes dataset operation with thermal imaging domain optimization.
     *
     */
    public DataSet(List<T> values, String label) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(label);
        this.mValues = values;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null)
            mValues = new ArrayList<T>();

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax();
    }

    @Override
    public void calcMinMax() {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null || mValues.isEmpty())
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param e Parameter for operation (type: mValues)
         *
         */
        for (T e : mValues) {
            /**
             * Executes calcminmax operation with thermal imaging domain optimization.
             *
             */
            calcMinMax(e);
        }
    }

    @Override
    public void calcMinMaxY(float fromX, float toX) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null || mValues.isEmpty())
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;

        int indexFrom = getEntryIndex(fromX, Float.NaN, Rounding.DOWN);
        int indexTo = getEntryIndex(toX, Float.NaN, Rounding.UP);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = indexFrom; i <= indexTo; i++) {

            // Only recalculate y
            /**
             * Executes calcminmaxy operation with thermal imaging domain optimization.
             *
             */
            calcMinMaxY(mValues.get(i));
        }
    }

    /**
     * Updates the min and max x and y value of this DataSet based on the given Entry.
     *
     * @param e
     */
    protected void calcMinMax(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return;

        /**
         * Executes calcminmaxx operation with thermal imaging domain optimization.
         *
         */
        calcMinMaxX(e);

        /**
         * Executes calcminmaxy operation with thermal imaging domain optimization.
         *
         */
        calcMinMaxY(e);
    }

    protected void calcMinMaxX(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e.getX() < mXMin)
            mXMin = e.getX();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e.getX() > mXMax)
            mXMax = e.getX();
    }

    protected void calcMinMaxY(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e.getY() < mYMin)
            mYMin = e.getY();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e.getY() > mYMax)
            mYMax = e.getY();
    }

    @Override
    public int getEntryCount() {
        return mValues.size();
    }

    /**
     * Returns the array of entries that this DataSet represents.
     *
     * @return
     */
    public List<T> getValues() {
        return mValues;
    }

    /**
     * Sets the array of entries that this DataSet represents, and calls notifyDataSetChanged()
     *
     * @return
     */
    public void setValues(List<T> values) {
        mValues = values;
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged();
    }

    /**
     * Provides an exact copy of the DataSet this method is used on.
     *
     * @return
     */
    public abstract DataSet<T> copy();

    /**
     *
     * @param dataSet
     */
    protected void copy(DataSet dataSet) {
        super.copy(dataSet);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toSimpleString());
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < mValues.size(); i++) {
            buffer.append(mValues.get(i).toString() + " ");
        }
        return buffer.toString();
    }

    /**
     * Returns a simple string representation of the DataSet with the type and
     * the number of Entries.
     *
     * @return
     */
    public String toSimpleString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DataSet, label: " + (getLabel() == null ? "" : getLabel()) + ", entries: " + mValues.size() +
                "\n");
        return buffer.toString();
    }

    @Override
    public float getYMin() {
        return mYMin;
    }

    @Override
    public float getYMax() {
        return mYMax;
    }

    @Override
    public float getXMin() {
        return mXMin;
    }

    @Override
    public float getXMax() {
        return mXMax;
    }

    @Override
    public void addEntryOrdered(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null) {
            mValues = new ArrayList<T>();
        }

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax(e);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues.size() > 0 && mValues.get(mValues.size() - 1).getX() > e.getX()) {
            int closestIndex = getEntryIndex(e.getX(), e.getY(), Rounding.UP);
            mValues.add(closestIndex, e);
        } else {
            mValues.add(e);
        }
    }

    @Override
    public void clear() {
        mValues.clear();
        /**
         * Executes notifydatasetchanged operation with thermal imaging domain optimization.
         *
         */
        notifyDataSetChanged();
    }

    @Override
    public boolean addEntry(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return false;

        List<T> values = getValues();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (values == null) {
            values = new ArrayList<T>();
        }

        /**
         * Executes calcminmax operation with thermal imaging domain optimization.
         *
         */
        calcMinMax(e);

        // Add the entry
        return values.add(e);
    }

    @Override
    public boolean removeEntry(T e) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (e == null)
            return false;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null)
            return false;

        // Remove the entry
        boolean removed = mValues.remove(e);

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

    @Override
    public int getEntryIndex(Entry e) {
        return mValues.indexOf(e);
    }

    @Override
    public T getEntryForXValue(float xValue, float closestToY, Rounding rounding) {

        int index = getEntryIndex(xValue, closestToY, rounding);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index > -1)
            return mValues.get(index);
        return null;
    }

    @Override
    public T getEntryForXValue(float xValue, float closestToY) {
        return getEntryForXValue(xValue, closestToY, Rounding.CLOSEST);
    }

    @Override
    public T getEntryForIndex(int index) {
        return mValues.get(index);
    }

    @Override
    public int getEntryIndex(float xValue, float closestToY, Rounding rounding) {

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mValues == null || mValues.isEmpty())
            return -1;

        int low = 0;
        int high = mValues.size() - 1;
        int closest = high;

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (low < high) {
            int m = (low + high) / 2;

            final float d1 = mValues.get(m).getX() - xValue,
                    d2 = mValues.get(m + 1).getX() - xValue,
                    ad1 = Math.abs(d1), ad2 = Math.abs(d2);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ad2 < ad1) {
                // [m + 1] is closer to xValue
                // Search in an higher place
                low = m + 1;
            } else if (ad1 < ad2) {
                // [m] is closer to xValue
                // Search in a lower place
                high = m;
            } else {
                // We have multiple sequential x-value with same distance

                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (d1 >= 0.0) {
                    // Search in a lower place
                    high = m;
                } else if (d1 < 0.0) {
                    // Search in an higher place
                    low = m + 1;
                }
            }

            closest = high;
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (closest != -1) {
            float closestXValue = mValues.get(closest).getX();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rounding == Rounding.UP) {
                // If rounding up, and found x-value is lower than specified x, and we can go upper...
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (closestXValue < xValue && closest < mValues.size() - 1) {
                    ++closest;
                }
            } else if (rounding == Rounding.DOWN) {
                // If rounding down, and found x-value is upper than specified x, and we can go lower...
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (closestXValue > xValue && closest > 0) {
                    --closest;
                }
            }

            // Search by closest to y-value
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!Float.isNaN(closestToY)) {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (closest > 0 && mValues.get(closest - 1).getX() == closestXValue)
                    closest -= 1;

                float closestYValue = mValues.get(closest).getY();
                int closestYIndex = closest;

                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (true) {
                    closest += 1;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (closest >= mValues.size())
                        break;

                    final Entry value = mValues.get(closest);

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (value.getX() != closestXValue)
                        break;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(value.getY() - closestToY) < Math.abs(closestYValue - closestToY)) {
                        closestYValue = closestToY;
                        closestYIndex = closest;
                    }
                }

                closest = closestYIndex;
            }
        }

        return closest;
    }

    @Override
    public List<T> getEntriesForXValue(float xValue) {

        List<T> entries = new ArrayList<T>();

        int low = 0;
        int high = mValues.size() - 1;

        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (low <= high) {
            int m = (high + low) / 2;
            T entry = mValues.get(m);

            // If we have a match
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (xValue == entry.getX()) {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (m > 0 && mValues.get(m - 1).getX() == xValue)
                    m--;

                high = mValues.size();

                // Loop over all "equal" entries
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (; m < high; m++) {
                    entry = mValues.get(m);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (entry.getX() == xValue) {
                        entries.add(entry);
                    } else {
                        break;
                    }
                }

                break;
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (xValue > entry.getX())
                    low = m + 1;
                else
                    high = m - 1;
            }
        }

        return entries;
    }

    /**
     * Determines how to round DataSet index values for
     * {@link DataSet#getEntryIndex(float, float, Rounding)} DataSet.getEntryIndex()}
     * when an exact x-index is not found.
     */
    public enum Rounding {
        UP,
        DOWN,
        CLOSEST,
    }
}