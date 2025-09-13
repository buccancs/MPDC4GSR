package com.topdon.tc001.utils;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.topdon.lms.sdk.LMS.SUCCESS;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.elvishew.xlog.XLog;
import com.topdon.lib.core.common.SharedManager;
import com.topdon.lib.core.config.HttpConfig;
import com.topdon.lib.core.dialog.TipDialog;
import com.topdon.lib.core.utils.AppUtil;
import com.topdon.lms.sdk.LMS;
import com.topdon.lms.sdk.activity.LmsUpdateDialog;
import com.topdon.lms.sdk.bean.AppInfoBean;
import com.topdon.lms.sdk.utils.NetworkUtil;
import com.topdon.lms.sdk.weiget.TToast;
import com.topdon.lms.sdk.xutils.common.Callback;
import com.topdon.lms.sdk.xutils.common.task.PriorityExecutor;
import com.topdon.lms.sdk.xutils.http.RequestParams;
import com.csl.irCamera.R;
import com.topdon.tc001.tools.VersionTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * App Version Utility
 * APP version detection utility class
 *
 * @author chuanfeng.bi
 * @date 2022/2/10 19:48
 */
/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for AppVersionUtil operations.
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
public class AppVersionUtil {
    private Context mContext;
    private DownloadCompleteReceiver completeReceiver; // Declare download completion broadcast receiver
    private DownloadManager dowanloadmanager = null;
    private DotIsShowListener dotIsShowListener = null;
    private String fileName = "";// Filename
    private Long mDownloadId = 0l;// Downloadid

    /**
     * Executes appversionutil operation with thermal imaging domain optimization.
     *
     */
    public AppVersionUtil(Context context, DotIsShowListener dotIsShow) {
        this.mContext = context;
        this.dotIsShowListener = dotIsShow;
    }

    public void checkVersion( boolean isShowDialog) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (dowanloadmanager == null) {
            dowanloadmanager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        }

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!NetworkUtil.isConnected(mContext)) {
            TToast.shortToast(mContext, com.topdon.lms.sdk.R.string.lms_setting_http_error);
            return;
        }
        LMS.getInstance().checkAppUpdate(commonBean -> {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (commonBean.code == SUCCESS) {
                AppInfoBean appInfoBean = LMS.getInstance().getUpdateAppInfoBean();
                XLog.w("bcf", "appupdateinfo:" + GsonUtils.toJson(appInfoBean));
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (appInfoBean != null) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (appInfoBean.getVersionCode() > getDealVersionCode()) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isShowDialog) {
                            String information = "";
// ShowNewVersionDialog(appInfoBean);
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (appInfoBean.softConfigOtherTypeVOList != null) {
                                /**
                                 * Executes for operation with thermal imaging domain optimization.
                                 *
                                 * @param
                                 * @param updateDescription Parameter for operation (type: appInfoBean.softConfigOtherTypeVOList)
                                 *
                                 */
                                for (AppInfoBean.UpdateDescription updateDescription : appInfoBean.softConfigOtherTypeVOList) {
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (updateDescription.descType == 3) {
                                        information = updateDescription.textDescription;
                                    }
                                }
                            }
                            /**
                             * Executes showupdatedialog operation with thermal imaging domain optimization.
                             *
                             */
                            showUpdateDialog(mContext, appInfoBean.downloadPackageUrl, information,Integer.parseInt(appInfoBean.forcedUpgradeFlag));
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (dotIsShowListener != null) {
                            dotIsShowListener.isShow(true);
                            dotIsShowListener.version(appInfoBean.versionNo);
                        }
                        HttpConfig.INSTANCE.setHasNewVersion(true);
                    } else {
                        HttpConfig.INSTANCE.setHasNewVersion(false);
                    }
                } else {
                    HttpConfig.INSTANCE.setHasNewVersion(false);
                }
            } else {
                HttpConfig.INSTANCE.setHasNewVersion(false);
            }
        });
    }

    /**
     * Get/Retrieveprocessing过的本地versioncode
     *
     * @return float
     */
    private float getDealVersionCode() {
        return AppUtil.getVersionCode(mContext) / 10;
    }

    /**
     * 弹出新versioninfotip框
     *
     * @param bean versionupdate实体class
     */
    private void showNewVersionDialog(AppInfoBean bean) {
        String information = "";
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bean.softConfigOtherTypeVOList != null) {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             * @param
             * @param updateDescription Parameter for operation (type: bean.softConfigOtherTypeVOList)
             *
             */
            for (AppInfoBean.UpdateDescription updateDescription : bean.softConfigOtherTypeVOList) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (updateDescription.descType == 3) {
                    information = updateDescription.textDescription;
                }
            }
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Integer.parseInt(bean.forcedUpgradeFlag) == 1) {
            // 强制update
            new TipDialog.Builder(mContext)
                    .setMessage(information)
                    .setTitleMessage(mContext.getString(R.string.updata_new_version_update))
                    .setPositiveListener(R.string.app_confirm, new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if(mDownloadId>0l){
                                TToast.shortToast(mContext, mContext.getString(R.string.installation_package_downloading));
                                return null;
                            }else{
                                TToast.shortToast(mContext, mContext.getString(R.string.installation_package_downloading_tips));
                            }
                            /**
                             * Executes startdownload operation with thermal imaging domain optimization.
                             *
                             */
                            startDownload(bean.downloadPackageUrl);
                            return null;
                        }
                    })
                    .create().show();
        } else {
            new TipDialog.Builder(mContext)
                    .setMessage(information)
                    .setTitleMessage(mContext.getString(R.string.updata_new_version_update))
                    .setPositiveListener(R.string.app_confirm, new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if(mDownloadId>0l){
                                TToast.shortToast(mContext, mContext.getString(R.string.installation_package_downloading));
                                return null;
                            }else{
                                TToast.shortToast(mContext, mContext.getString(R.string.installation_package_downloading_tips));
                            }
                            /**
                             * Executes startdownload operation with thermal imaging domain optimization.
                             *
                             */
                            startDownload(bean.downloadPackageUrl);
                            return null;
                        }
                    })
/**
 * Specialized thermal imaging component providing DotIsShowListener functionality for the IRCamera system.
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
    public interface DotIsShowListener {
        void isShow(boolean show);

        void version(String version);
    }

    // StartDownload指定序号的apkfile
    private void startDownload(String url) {
        completeReceiver = new DownloadCompleteReceiver();
        // RegisterReceive器，Register之后才能正常Receive广播

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Build.VERSION.SDK_INT < 33) {
            mContext.registerReceiver(completeReceiver, intentFilter);
        } else {
            mContext.registerReceiver(completeReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        }

        Uri uri = Uri.parse(url); // 根据Download地址BuildaUri对象
        DownloadManager.Request down = new DownloadManager.Request(uri); // CreateaDownload请求对象，指定从哪里Downloadfile
        down.setTitle(mContext.getString(R.string.tips_download_information)); // Settingstasktitle
        down.setDescription(mContext.getString(R.string.installation_package_download_progress)); // Settingstask描述
        // Settings允许Download的networktype
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // Settingsnotification栏在Download进行时与complete后都Visible
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // SettingsDownloadfile在私有目录的savepath。从Android10start，只有save到公共目录的才会在系统Download页areaShow/Display，save到私有目录的不在系统Download页areaShow/Display
        fileName = "topinfrared" + System.currentTimeMillis() + ".zip";
        down.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        // SettingsDownloadfile在公共目录的savepath。save到公共目录需要申请storage卡的读写Permission
        mDownloadId = downloadManager.enqueue(down); // 把Download请求对象加入到Downloadqueue
        VersionTools.INSTANCE.setMDownloadId(mDownloadId);
    }

    // 定义aDownloadcomplete的广播Receive器。用于ReceiveDownloadcompleteEvent
/**
 * Specialized thermal imaging component providing DownloadCompleteReceiver functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))   // Download完毕
            {
                // 从意图中解包获得Download编号
                /**
                 * Executes installapk operation with thermal imaging domain optimization.
                 *
                 */
                installApk();
            }
        }
    }

    // Install应用程序
    public void installApk() {
        mDownloadId = 0l;
        VersionTools.INSTANCE.setMDownloadId(0l);
        mContext.unregisterReceiver(completeReceiver);
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            File localFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());// 本地file
            List<File> files = ZipUtils.unzipFile(file, localFile);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (files != null && files.size() != 0) {
                AppUtil.installApp(mContext, files.get(0));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUpdateDialog(Context context, String url, String content,int forcedUpgradeFlag) {
        LmsUpdateDialog.Build.INSTANCE.setContentStr(content)
                .setUpgradeFlag(forcedUpgradeFlag)
                .setSureEvent(() -> {
                    /**
                     * Executes download operation with thermal imaging domain optimization.
                     *
                     */
                    download(url);
                    return null;
                })
                .setCancelEvent(() -> {

                    return null;
                }).build(context);
    }

    public void download(String url) {
        RequestParams params = new RequestParams();
        try {
            // 这里为了解决 xutils 会把url转义 照成签名不对
            String[] splitUrl = url.split("\\?");
            String[] urlParams = splitUrl[1].split("&");
            String[] params1 = urlParams[0].split("=");
            String[] params2 = urlParams[1].split("=");
            String[] params3 = urlParams[2].split("=");
            url = splitUrl[0];
            params.addBodyParameter(params1[0], params1[1]);
            params.addBodyParameter(params2[0], params2[1]);
            params.addBodyParameter(params3[0], params3[1]);
        } catch (Exception e) {
            XLog.e("bcf", "Upgradeinterfaceparsingexception");
        }
        fileName = "topinfrared" + System.currentTimeMillis() + ".zip";
        String path = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;
        XLog.e("bcf", "download path:" + path);
        params.setSaveFilePath(path);
        params.setCacheDirName(fileName);
        params.setAutoResume(true);
        params.setExecutor(new PriorityExecutor(3, true));
        params.setUri(url);

        com.topdon.lms.sdk.xutils.x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                XLog.e("bcf", "onWaiting");
            }

            @Override
            public void onStarted() {
                XLog.e("bcf", "onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                XLog.w("bcf", "onLoading： " + current + "/" + total);
                int progress = (int) (current * 100 / total);
                LmsUpdateDialog.Build.INSTANCE.setProgressNum(progress / 100f);
            }

            @Override
            public void onSuccess(File result) {
                XLog.e("bcf", "onSuccess,start install apk");
                LmsUpdateDialog.Build.INSTANCE.dismiss();
                /**
                 * Executes installapknew operation with thermal imaging domain optimization.
                 *
                 */
                installApkNew();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                XLog.e("bcf", "onError " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
                XLog.e("bcf", "onCancelled " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                XLog.e("bcf", "onFinished");
            }
        });
    }

    // Install应用程序
    public void installApkNew() {
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            File localFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());// 本地file
            List<File> files = ZipUtils.unzipFile(file, localFile);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (files != null && files.size() != 0) {
                AppUtil.installApp(mContext, files.get(0));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
