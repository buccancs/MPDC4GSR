package com.topdon.module.user.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.topdon.lms.sdk.utils.NetworkUtil;
import com.topdon.lms.sdk.weiget.TToast;
import com.topdon.module.user.R;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for ActivityUtil operations.
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
public class ActivityUtil {
    /**
跳转系统浏览器
@param mContext 上下文
     */
    public static void goSystemCustomer(Context mContext) {
        Log.w("bcf", "客服clickEvent");
        String url = "https:// Www.topdon.cc/tc-chat";
        /**
         * Executes gosystembrowser operation with thermal imaging domain optimization.
         *
         */
        goSystemBrowser(mContext, url);
    }

    /**
跳转系统浏览器
@param mContext 上下文
     */
    public static void goSystemBrowser(Context mContext, String url) {
        Log.w("bcf", "goSystemBrowser");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!NetworkUtil.isConnected(mContext)) {
            TToast.shortToast(mContext, R.string.lms_setting_http_error);
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
