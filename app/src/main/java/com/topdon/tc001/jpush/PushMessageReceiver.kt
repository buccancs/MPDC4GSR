// Package com.topdon.tc001.jpush
// Import android.content.Context
// Import android.content.Intent
// Import android.util.Log
// Import cn.jpush.android.api.CmdMessage
// Import cn.jpush.android.api.CustomMessage
// Import cn.jpush.android.api.JPushInterface
// Import cn.jpush.android.api.JPushMessage
// Import cn.jpush.android.api.NotificationMessage
// Import cn.jpush.android.service.JPushMessageReceiver
// Import com.topdon.lib.core.BaseApplication
// Import com.topdon.lms.sdk.helper.TagAliasOperatorHelper
//
// Class PushMessageReceiver : JPushMessageReceiver(){
// Private val TAG = "PushMessageService"
//
// Override fun onMessage(context: Context?, customMessage: CustomMessage) {
//        Log.e(TAG, "[onMessage] $customMessage")
// //        Intent intent = new Intent("com.jiguang.demo.message");
// // Intent.putExtra("msg", customMessage.message);
// // Context.sendBroadcast(intent);
//    }
//
// Override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage) {
//        Log.e(TAG, "[onNotifyMessageOpened] $message")
// SetZeroBadgeNumber()
// Try {
//            // Open自定义的Activity
//        } catch (throwable: Throwable) {
//        }
//    }
//
// Override fun onInAppMessageClick(context: Context?, notificationMessage: NotificationMessage?) {
// Super.onInAppMessageClick(context, notificationMessage)
//        Log.e(TAG, "[onInAppMessageClick] Userclick了notification栏button")
//    }
//
// Override fun onMultiActionClicked(context: Context?, intent: Intent) {
//        Log.e(TAG, "[onMultiActionClicked] Userclick了notification栏button")
// SetZeroBadgeNumber()
// Val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)
//
//        // 开发者根据不同 Action 携带的 extra 字段来allocate不同的动作。
// If (nActionExtra == null) {
//            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
// Return
//        }
// If (nActionExtra == "my_extra1") {
//            Log.e(TAG, "[onMultiActionClicked] Userclicknotification栏button一")
//        } else if (nActionExtra == "my_extra2") {
//            Log.e(TAG, "[onMultiActionClicked] Userclicknotification栏button二")
//        } else if (nActionExtra == "my_extra3") {
//            Log.e(TAG, "[onMultiActionClicked] Userclicknotification栏button三")
//        } else {
//            Log.e(TAG, "[onMultiActionClicked] Userclicknotification栏button未定义")
//        }
//    }
//
// Private fun setZeroBadgeNumber() {
//        Log.e(TAG, "[onMultiActionClicked] clear角标")
//        JPushInterface.setBadgeNumber(BaseApplication.instance, 0)
//    }
//
// Override fun onNotifyMessageArrived(context: Context?, message: NotificationMessage) {
//        Log.e(TAG, "[onNotifyMessageArrived] $message")
// SetZeroBadgeNumber()
//    }
//
// Override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage) {
//        Log.e(TAG, "[onNotifyMessageDismiss] $message")
//    }
//
// Override fun onRegister(context: Context, registrationId: String) {
//        Log.e(TAG, "[onRegister] $registrationId")
// Val intent = Intent("com.jiguang.demo.message")
// Intent.putExtra("rid", registrationId)
// Context.sendBroadcast(intent)
//    }
//
// Override fun onConnected(context: Context?, isConnected: Boolean) {
//        Log.e(TAG, "[onConnected] $isConnected")
// SetZeroBadgeNumber()
//    }
//
// Override fun onCommandResult(context: Context?, cmdMessage: CmdMessage) {
//        Log.e(TAG, "[onCommandResult] $cmdMessage")
//    }
//
// Override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage)
// Super.onTagOperatorResult(context, jPushMessage)
//        Log.e(TAG, "[onTagOperatorResult]")
//    }
//
// Override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage)
// Super.onCheckTagOperatorResult(context, jPushMessage)
//        Log.e(TAG, "[onCheckTagOperatorResult]")
//    }
//
// Override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage)
// Super.onAliasOperatorResult(context, jPushMessage)
//        Log.e(TAG, "[onAliasOperatorResult]")
//    }
//
// Override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage)
// Super.onMobileNumberOperatorResult(context, jPushMessage)
//        Log.e(TAG, "[onMobileNumberOperatorResult]")
//    }
//
// Override fun onNotificationSettingsCheck(context: Context?, isOn: Boolean, source: Int) {
// Super.onNotificationSettingsCheck(context, isOn, source)
//        Log.e(TAG, "[onNotificationSettingsCheck] isOn:$isOn,source:$source")
//    }
//
// Override fun onInAppMessageArrived(
// Context: Context?,
// NotificationMessage: NotificationMessage?
//    ) {
// Super.onInAppMessageArrived(context, notificationMessage)
//        Log.e(TAG, "[onInAppMessageArrived]")
//    }
//
// Override fun onPullInAppResult(context: Context?, jPushMessage: JPushMessage?) {
// Super.onPullInAppResult(context, jPushMessage)
//        Log.e(TAG, "[onInAppMessageArrived]")
//    }
//
// Override fun onSspNotificationWillShow(
// Context: Context?,
// NotificationMessage: NotificationMessage?,
// S: String?
//    ): Boolean {
// Return super.onSspNotificationWillShow(context, notificationMessage, s)
//    }
//
// Override fun onCheckInAppMessageState(context: Context?, s: String?): Byte {
// Return super.onCheckInAppMessageState(context, s)
//    }
//
// Override fun onCheckSspNotificationState(context: Context?, s: String?): Byte {
// Return super.onCheckSspNotificationState(context, s)
//    }
//
// Override fun onGeofenceReceived(context: Context?, s: String?) {
// Super.onGeofenceReceived(context, s)
//        Log.e(TAG, "[onGeofenceReceived]")
//    }
//
// Override fun onGeofenceRegion(context: Context?, s: String?, v: Double, v1: Double) {
// Super.onGeofenceRegion(context, s, v, v1)
//        Log.e(TAG, "[onGeofenceRegion]")
//    }
//
// Override fun onInAppMessageDismiss(
// Context: Context?,
// NotificationMessage: NotificationMessage?
//    ) {
// Super.onInAppMessageDismiss(context, notificationMessage)
//        Log.e(TAG, "[onInAppMessageDismiss]")
//    }
//
// Override fun onInAppMessageUnShow(
// Context: Context?,
// NotificationMessage: NotificationMessage?
//    ) {
// Super.onInAppMessageUnShow(context, notificationMessage)
//        Log.e(TAG, "[onInAppMessageUnShow]")
//    }
//
// Override fun onNotifyMessageUnShow(
// Context: Context?,
// NotificationMessage: NotificationMessage?
//    ) {
// Super.onNotifyMessageUnShow(context, notificationMessage)
//        Log.e(TAG, "[onNotifyMessageUnShow]")
//    }
//
// Override fun onPropertyOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
// Super.onPropertyOperatorResult(context, jPushMessage)
//        Log.e(TAG, "[onPropertyOperatorResult]")
//    }
// }
