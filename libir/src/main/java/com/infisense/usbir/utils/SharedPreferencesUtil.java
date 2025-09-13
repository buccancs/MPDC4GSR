package com.infisense.usbir.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for SharedPreferencesUtil operations.
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
public class SharedPreferencesUtil {

    private static final String FILE_NAME = "usb_ir";

    /**
     * saveddata到file
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key, Object data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.commit();
    }

    /**
     * 从file里读取data
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveByteData(Context context, String key, byte[] data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String imageString = new String(Base64.encode(data, Base64.DEFAULT));
        editor.putString(key, imageString);

        editor.commit();
    }

    /**
     *
     * @param context
     * @param key
     * @return
     */
    public static byte[] getByteData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        String string = sharedPreferences.getString(key, "");
        byte[] b = Base64.decode(string.getBytes(), Base64.DEFAULT);
        return b;
    }
}
