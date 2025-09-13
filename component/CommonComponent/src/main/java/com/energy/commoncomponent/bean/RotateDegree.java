package com.energy.commoncomponent.bean;

/**
 * Created by fengjibo on 2023/7/4.
 */
public enum RotateDegree {

    /**
     * Executes degree 0 operation with thermal imaging domain optimization.
     *
     */
    DEGREE_0(0),
    //
    /**
     * Executes degree 90 operation with thermal imaging domain optimization.
     *
     */
    DEGREE_90(1),
    //
    /**
     * Executes degree 180 operation with thermal imaging domain optimization.
     *
     */
    DEGREE_180(2),
    //
    /**
     * Executes degree 270 operation with thermal imaging domain optimization.
     *
     */
    DEGREE_270(3);

    private final int value;

    /**
     * Executes rotatedegree operation with thermal imaging domain optimization.
     *
     */
    RotateDegree(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RotateDegree valueOf(int value) {
        RotateDegree[] types = RotateDegree.values();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param type Parameter for operation (type: types)
         *
         */
        for(RotateDegree type: types){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (type.value == value) {
                return type;
            }
        }
        return DEGREE_0;
    }
}