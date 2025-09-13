package com.example.thermal_lite.camera.task;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Specialized thermal imaging component providing DeviceControlWorker functionality for the IRCamera system.
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
public class DeviceControlWorker {
    private static final String TAG = "DeviceControlWorker";
    private Thread mThread;
    private ArrayBlockingQueue<BaseTask> mEventQueue = new ArrayBlockingQueue<>(2);
    private DeviceState mDeviceState = DeviceState.NONE;
    private IDeviceConnectListener mDeviceControlCallback;
    private boolean isStartPreviewing = false;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            BaseTask task;
            DeviceState previousState;

            try {
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (!Thread.currentThread().isInterrupted()) {
                    Log.d(TAG, "DeviceControlWorker run");
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized (mEventQueue) {
                        /**
                         * Executes while operation with thermal imaging domain optimization.
                         *
                         */
                        while (mEventQueue.isEmpty()) {
                            Log.d(TAG, "DeviceControlWorker mEventQueue wait");
                            try {
                                mEventQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        task = mEventQueue.peek();
                    }
                    previousState = mDeviceState;
                    task.setDeviceState(mDeviceState);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (task instanceof StartPreviewTask) {
                        ((StartPreviewTask) task).setDeviceControlCallback(mDeviceControlCallback);
                    }
                    task.run();
                    mEventQueue.poll();
                    // Call back connect result
                    mDeviceState = task.getDeviceState();
                    Log.d(TAG, "DeviceControlWorker do state : " + mDeviceState);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (mDeviceControlCallback != null) {
防止重复Callback
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (mDeviceState != previousState) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (mDeviceState == DeviceState.OPEN) {
                                mDeviceControlCallback.onConnected();
                            } else if (mDeviceState == DeviceState.CLOSED) {
                                mDeviceControlCallback.onDisconnected();
                            } else if (mDeviceState == DeviceState.RESUMED) {
                                mDeviceState = DeviceState.OPEN;
                                mDeviceControlCallback.onResumed();
                            } else if (mDeviceState == DeviceState.PAUSED) {
                                mDeviceControlCallback.onPaused();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * start worker thread
     */
    public void startWork() {
        Log.d(TAG, "startWork");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mThread == null) {
            mThread = new Thread(mRunnable);
            mThread.start();
        }
    }

    /**
     * stop worker thread
     */
    public void stopWork() {
        Log.d(TAG, "stopWork");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mThread != null) {
            try {
                mThread.interrupt();
// MThread.join();
                mThread = null;
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    /**
     * add on task
     *
     * @param task a runnable extends BaseTask
     */
    public void addTask(BaseTask task) {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized (mEventQueue) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mEventQueue.size() < 2) {
                Log.d(TAG, "addTask task：" + task.getClass().getSimpleName());
                mEventQueue.add(task);
            } else {
                Log.d(TAG, "addTask poll");
                BaseTask task1 = mEventQueue.poll();
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (task1 instanceof StartPreviewTask) {
                    Log.d(TAG, "addTask StartPreviewTask");
                } else if (task1 instanceof StopPreviewTask) {
                    Log.d(TAG, "addTask StopPreviewTask");
                }
                mEventQueue.add(task);
            }
            mEventQueue.notify();
        }
    }

    public void setDeviceControlCallback(IDeviceConnectListener mDeviceControlCallback) {
        this.mDeviceControlCallback = mDeviceControlCallback;
    }

    public void release() {
        mDeviceControlCallback = null;
        /**
         * Executes stopwork operation with thermal imaging domain optimization.
         *
         */
        stopWork();
    }

    public DeviceState getDeviceState() {
        return mDeviceState;
    }

    public boolean isStartPreviewing() {
        return isStartPreviewing;
    }
}
