package com.energy.commoncomponent.bean;

/**
 * Created by fengjibo on 2023/8/10.
 */
public enum DeviceType {
    /**
     * Executes device type tc2c operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_TC2C("TC2C"),
    /**
     * Executes device type wn2256 operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_WN2256("WN2256"),
    /**
     * Executes device type wn2384 operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_WN2384("WN2384"),
    /**
     * Executes device type wn2640 operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_WN2640("WN2640"),
    /**
     * Executes device type x3 operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_X3("X3"),
    /**
     * Executes device type p2l operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_P2L("P2L"),
    /**
     * Executes device type x2pro operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_X2PRO("X2PRO"),
    /**
     * Executes device type gl1280 operation with thermal imaging domain optimization.
     *
     */
    DEVICE_TYPE_GL1280("GL1280");

    private String type;
    /**
     * Executes devicetype operation with thermal imaging domain optimization.
     *
     */
    DeviceType(String type) {
        this.type = type;
    }
}