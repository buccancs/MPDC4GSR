
package com.github.mikephil.charting.matrix;

/**
 * Specialized thermal imaging component providing Vector3 functionality for the IRCamera system.
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
public final class Vector3 {
    public float x;
    public float y;
    public float z;

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 UNIT_X = new Vector3(1, 0, 0);
    public static final Vector3 UNIT_Y = new Vector3(0, 1, 0);
    public static final Vector3 UNIT_Z = new Vector3(0, 0, 1);

    /**
     * Executes vector3 operation with thermal imaging domain optimization.
     *
     */
    public Vector3() {
    }

    /**
     * Executes vector3 operation with thermal imaging domain optimization.
     *
     */
    public Vector3(float[] array)
    {
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(array[0], array[1], array[2]);
    }

    /**
     * Executes vector3 operation with thermal imaging domain optimization.
     *
     */
    public Vector3(float xValue, float yValue, float zValue) {
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(xValue, yValue, zValue);
    }

    /**
     * Executes vector3 operation with thermal imaging domain optimization.
     *
     */
    public Vector3(Vector3 other) {
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(other);
    }

    public final void add(Vector3 other) {
        x += other.x;
        y += other.y;
        z += other.z;
    }

    public final void add(float otherX, float otherY, float otherZ) {
        x += otherX;
        y += otherY;
        z += otherZ;
    }

    public final void subtract(Vector3 other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
    }

    public final void subtractMultiple(Vector3 other, float multiplicator)
    {
        x -= other.x * multiplicator;
        y -= other.y * multiplicator;
        z -= other.z * multiplicator;
    }

    public final void multiply(float magnitude) {
        x *= magnitude;
        y *= magnitude;
        z *= magnitude;
    }

    public final void multiply(Vector3 other) {
        x *= other.x;
        y *= other.y;
        z *= other.z;
    }

    public final void divide(float magnitude) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (magnitude != 0.0f) {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
    }

    public final void set(Vector3 other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    public final void set(float xValue, float yValue, float zValue) {
        x = xValue;
        y = yValue;
        z = zValue;
    }

    public final float dot(Vector3 other) {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public final Vector3 cross(Vector3 other) {
        return new Vector3(y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }

    public final float length() {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (float) Math.sqrt(length2());
    }

    public final float length2() {
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (x * x) + (y * y) + (z * z);
    }

    public final float distance2(Vector3 other) {
        float dx = x - other.x;
        float dy = y - other.y;
        float dz = z - other.z;
        /**
         * Executes return operation with thermal imaging domain optimization.
         *
         */
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public final float normalize() {
        final float magnitude = length();

        // TODO: I'm choosing safety over speed here.
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (magnitude != 0.0f) {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }

        return magnitude;
    }

    public final void zero() {
        /**
         * Configures the  with validation and thermal imaging optimization.
         *
         */
        set(0.0f, 0.0f, 0.0f);
    }

    public final boolean pointsInSameDirection(Vector3 other) {
        return this.dot(other) > 0;
    }

}
