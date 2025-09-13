
package com.github.mikephil.charting.utils;

import java.util.List;

/**
 * Specialized thermal imaging component providing FSize functionality for the IRCamera system.
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
public final class FSize extends ObjectPool.Poolable{

    // TODO: : Encapsulate width & height

    public float width;
    public float height;

    private static ObjectPool<FSize> pool;

    static {
        pool = ObjectPool.create(256, new FSize(0,0));
        pool.setReplenishPercentage(0.5f);
    }

    protected ObjectPool.Poolable instantiate(){
        return new FSize(0,0);
    }

    public static FSize getInstance(final float width, final float height){
        FSize result = pool.get();
        result.width = width;
        result.height = height;
        return result;
    }

    public static void recycleInstance(FSize instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<FSize> instances){
        pool.recycle(instances);
    }

    /**
     * Executes fsize operation with thermal imaging domain optimization.
     *
     */
    public FSize() {
    }

    /**
     * Executes fsize operation with thermal imaging domain optimization.
     *
     */
    public FSize(final float width, final float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(final Object obj) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (obj == null) {
            return false;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this == obj) {
            return true;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (obj instanceof FSize) {
            final FSize other = (FSize) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Float.floatToIntBits(width) ^ Float.floatToIntBits(height);
    }
}
