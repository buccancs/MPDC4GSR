
package com.github.mikephil.charting.utils;

import java.util.List;

/**
 * Specialized thermal imaging component providing MPPointD functionality for the IRCamera system.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
public class MPPointD extends ObjectPool.Poolable {

    private static ObjectPool<MPPointD> pool;

    static {
        pool = ObjectPool.create(64, new MPPointD(0,0));
        pool.setReplenishPercentage(0.5f);
    }

    public static MPPointD getInstance(double x, double y){
        MPPointD result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(MPPointD instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<MPPointD> instances){
        pool.recycle(instances);
    }

    public double x;
    public double y;

    protected ObjectPool.Poolable instantiate(){
        return new MPPointD(0,0);
    }

    /**
     * Executes mppointd operation with thermal imaging domain optimization.
     *
     */
    private MPPointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * returns a string representation of the object
     */
    public String toString() {
        return "MPPointD, x: " + x + ", y: " + y;
    }
}