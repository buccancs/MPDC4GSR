
package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import com.github.mikephil.charting.utils.Utils;

/**
 * Class representing one entry in the chart. Might contain multiple values.
 * Might only contain a single value depending on the used constructor.
 * 
 * @author Philipp Jahoda
 */
/**
 * Specialized thermal imaging component providing Entry functionality for the IRCamera system.
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
public class Entry extends BaseEntry implements Parcelable {

    /** the x value */
    private float x = 0f;

    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    public Entry() {

    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    public Entry(float x, float y) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(y);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param data Spot for additional data this Entry represents.
     */
    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    public Entry(float x, float y, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(y, data);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     */
    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    public Entry(float x, float y, Drawable icon) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(y, icon);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     * @param data Spot for additional data this Entry represents.
     */
    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    public Entry(float x, float y, Drawable icon, Object data) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(y, icon, data);
        this.x = x;
    }

    /**
     * Returns the x-value of this Entry object.
     * 
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this Entry object.
     * 
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * returns an exact copy of the entry
     * 
     * @return
     */
    public Entry copy() {
        Entry e = new Entry(x, getY(), getData());
        return e;
    }

    /**
     * Compares value, xIndex and data of the entries. Returns true if entries
     * are equal in those points, false if not. Does not check by hash-code like
     * it's done by the "equals" method.
     * 
     * @param e
     * @return
     */
    public boolean equalTo(Entry e) {

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
        if (e.getData() != this.getData())
            return false;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Math.abs(e.x - this.x) > Utils.FLOAT_EPSILON)
            return false;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Math.abs(e.getY() - this.getY()) > Utils.FLOAT_EPSILON)
            return false;

        return true;
    }

    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (getData() != null) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (getData() instanceof Parcelable) {
                dest.writeInt(1);
                dest.writeParcelable((Parcelable) this.getData(), flags);
            } else {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Executes entry operation with thermal imaging domain optimization.
     *
     */
    protected Entry(Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (in.readInt() == 1) {
            this.setData(in.readParcelable(Object.class.getClassLoader()));
        }
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
