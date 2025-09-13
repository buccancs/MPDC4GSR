package com.example.thermal_lite.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.energy.irutilslibrary.LibIRParse;

import java.io.IOException;
import java.io.InputStream;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for CommonUtil operations.
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
public class CommonUtil {
    public static final String TAG = "CommonUtil";

    public static byte[] getAssetData(Context mContext, String assetTauName) {
        byte[] tau_data = null;
        //
        AssetManager am = mContext.getAssets();
        InputStream is = null;
        try {
根据不同的high/low gainload不同的等效大气透过率表
            is = am.open(assetTauName);
            int lenth = is.available();
            tau_data = new byte[lenth];
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (is.read((tau_data)) != lenth) {
                // "read file fail "
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tau_data;
    }

    public static int convertArrayY16ToY14(byte[] y16_data, int pixel_num, byte[] y14_data) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (y16_data != null && y14_data != null) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (pixel_num <= 0) {
                return LibIRParse.IrparseResult.IRPARSE_ERROR_PARAM.getValue();
            } else {
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int i = 0; i < pixel_num; ++i) {
                    y14_data[i] = (byte) (y16_data[i] >> 2);
                }

                return LibIRParse.IrparseResult.IRPARSE_SUCCESS.getValue();
            }
        } else {
            return LibIRParse.IrparseResult.IRPARSE_ERROR_PARAM.getValue();
        }
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static float round(float value, int places) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (places < 0) throw new IllegalArgumentException();

        final float factor = (float) Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }

frame率展示
    public static int mCount = 0;
    private static long mTimeStart = 0;
    private static double mFps = 0;
    public static double showFps() {
        mCount++;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mCount == 100) {
            mCount = 0;
            long currentTimeMillis = System.currentTimeMillis();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mTimeStart != 0) {
                long timeuse = currentTimeMillis - mTimeStart;
                mFps = 100 * 1000 / (timeuse + 0.0);
            }
            mTimeStart = currentTimeMillis;
        }

        return mFps;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param b Parameter for operation (type: bytes)
         *
         */
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b & 0xFF));
        }

        String hexString = sb.toString().trim();
        return hexString;
    }

}
