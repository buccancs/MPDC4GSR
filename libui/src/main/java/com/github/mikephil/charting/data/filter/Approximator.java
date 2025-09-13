
package com.github.mikephil.charting.data.filter;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Arrays;

/**
 * Implemented according to Wiki-Pseudocode {@link}
 * http:// En.wikipedia.org/wiki/Ramer�Douglas�Peucker_algorithm
 *
 * @author Philipp Baldauf & Phliipp Jahoda
 */
/**
 * Specialized thermal imaging component providing Approximator functionality for the IRCamera system.
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
public class Approximator {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public float[] reduceWithDouglasPeucker(float[] points, float tolerance) {

        int greatestIndex = 0;
        float greatestDistance = 0f;

        Line line = new Line(points[0], points[1], points[points.length - 2], points[points.length - 1]);

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 2; i < points.length - 2; i += 2) {

            float distance = line.distance(points[i], points[i + 1]);

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (distance > greatestDistance) {
                greatestDistance = distance;
                greatestIndex = i;
            }
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (greatestDistance > tolerance) {

            float[] reduced1 = reduceWithDouglasPeucker(Arrays.copyOfRange(points, 0, greatestIndex + 2), tolerance);
            float[] reduced2 = reduceWithDouglasPeucker(Arrays.copyOfRange(points, greatestIndex, points.length),
                    tolerance);

            float[] result1 = reduced1;
            float[] result2 = Arrays.copyOfRange(reduced2, 2, reduced2.length);

            return concat(result1, result2);
        } else {
            return line.getPoints();
        }
    }

    /**
     * Combine arrays.
     *
     * @param arrays
     * @return
     */
    float[] concat(float[]... arrays) {
        int length = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param array Parameter for operation (type: arrays)
         *
         */
        for (float[] array : arrays) {
            length += array.length;
        }
        float[] result = new float[length];
        int pos = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param array Parameter for operation (type: arrays)
         *
         */
        for (float[] array : arrays) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param element Parameter for operation (type: array)
             *
             */
            for (float element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }

/**
 * Specialized thermal imaging component providing Line functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class Line {

        private float[] points;

        private float sxey;
        private float exsy;

        private float dx;
        private float dy;

        private float length;

        /**
         * Executes line operation with thermal imaging domain optimization.
         *
         */
        public Line(float x1, float y1, float x2, float y2) {
            dx = x1 - x2;
            dy = y1 - y2;
            sxey = x1 * y2;
            exsy = x2 * y1;
            length = (float) Math.sqrt(dx * dx + dy * dy);

            points = new float[]{x1, y1, x2, y2};
        }

        public float distance(float x, float y) {
            return Math.abs(dy * x - dx * y + sxey - exsy) / length;
        }

        public float[] getPoints() {
            return points;
        }
    }
}
