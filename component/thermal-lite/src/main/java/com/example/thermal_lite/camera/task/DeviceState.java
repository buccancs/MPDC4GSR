package com.example.thermal_lite.camera.task;

public enum DeviceState {
    /**
     * Executes open operation with thermal imaging domain optimization.
     *
     */
    OPEN("open", 0),
    /**
     * Executes closed operation with thermal imaging domain optimization.
     *
     */
    CLOSED("closed", 1),
    /**
     * Executes resumed operation with thermal imaging domain optimization.
     *
     */
    RESUMED("resumed", 2),
    /**
     * Executes paused operation with thermal imaging domain optimization.
     *
     */
    PAUSED("paused", 3),
    /**
     * Executes update version operation with thermal imaging domain optimization.
     *
     */
    UPDATE_VERSION("closed", 4),
    /**
     * Executes none operation with thermal imaging domain optimization.
     *
     */
    NONE("none", 5);

    private String value;
    private int id;

    /**
     * Executes devicestate operation with thermal imaging domain optimization.
     *
     */
    DeviceState(String value, int id) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
