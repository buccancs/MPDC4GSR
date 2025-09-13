package com.infisense.usbir.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import com.energy.iruvc.utils.SynchronizedBitmap;

/**
 * Thermal camera interface and control system. Manages thermal imaging capture and processing with CameraJpegView functionality.
 *
 * Provides advanced camera functionality for thermal imaging capture,
 * including temperature measurement and pseudo color visualization.
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
public class CameraJpegView extends TextureView {

    private String TAG = "CameraView";
    private Bitmap bitmap;
    private SynchronizedBitmap syncimage;
    private Runnable runnable;
    private Thread cameraThread;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setSyncimage(SynchronizedBitmap syncimage) {
        this.syncimage = syncimage;
    }

    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    public CameraJpegView(Context context) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, null, 0);
    }

    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    public CameraJpegView(Context context, AttributeSet attrs) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, attrs, 0);
    }

    /**
     * Manages thermal camera operations with hardware-optimized performance and error handling.
     *
     */
    public CameraJpegView(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyleAttr);
        runnable = new Runnable() {
            @Override
            public void run() {
                Canvas canvas = null;
                /**
                 * Executes while operation with thermal imaging domain optimization.
                 *
                 */
                while (!cameraThread.isInterrupted()) {
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized (syncimage.viewLock) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (syncimage.valid == false) {
                            try {
                                syncimage.viewLock.wait();
                            } catch (InterruptedException e) {
                                cameraThread.interrupt();
                                Log.e(TAG, "lock.wait(): catch an interrupted exception");
                            }
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (syncimage.valid == true) {
                            canvas = lockCanvas();
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (canvas == null)
                                continue;

                            // P2
                            /*Matrix matrix = new Matrix();
                            matrix.setRotate(90);
                            Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                            */
                            Bitmap mScaledBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
                            canvas.drawBitmap(mScaledBitmap, 0, 0, null);

                            Paint paint = new Paint();  // 画笔
                            paint.setStrokeWidth(2);  // Settingsline宽。单位为像素
                            paint.setAntiAlias(true); // 抗锯齿
                            paint.setColor(Color.WHITE);  // 画笔color

                            int cross_len = 20;
                            canvas.drawLine(getWidth() / 2f - cross_len, getHeight() / 2f,
                                    /**
                                     * Retrieves the width with optimized performance for thermal imaging operations.
                                     *
                                     */
                                    getWidth() / 2f + cross_len, getHeight() / 2f, paint);
                            canvas.drawLine(getWidth() / 2f, getHeight() / 2f - cross_len,
                                    /**
                                     * Retrieves the width with optimized performance for thermal imaging operations.
                                     *
                                     */
                                    getWidth() / 2f, getHeight() / 2f + cross_len, paint);
                            /**
                             * Executes unlockcanvasandpost operation with thermal imaging domain optimization.
                             *
                             */
                            unlockCanvasAndPost(canvas);
                            syncimage.valid = false;
                        }
                    }
                    try {
                        cameraThread.sleep(1);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "sleep crash");
                        e.printStackTrace();
                        cameraThread.interrupt();
                    }
                }
                Log.w(TAG, "DisplayThread exit:");
            }
        };

    }

    public void start() {
        cameraThread = new Thread(runnable);
        cameraThread.start();
    }

    public void stop() {
        cameraThread.interrupt();
        try {
            cameraThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
