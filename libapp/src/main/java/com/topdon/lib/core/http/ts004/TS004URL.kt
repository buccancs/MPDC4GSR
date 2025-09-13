package com.topdon.lib.core.http.ts004

/**
 * Specialized thermal imaging component providing TS004URL functionality for the IRCamera system.
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
object TS004URL {
    const val RTSP_URL = "rtsp:// 192.168.40.1/ch0/stream0"
    private const val BASE_URL = "http:// 192.168.40.1:8080"
    const val SET_PSEUDO_COLOR = "$BASE_URL/api/v1/system/setPseudoColor" // Settingspseudo color样式
    const val GET_PSEUDO_COLOR = "$BASE_URL/api/v1/system/getPseudoColor" // Get/Retrievepseudo color样式
    const val SET_PANEL_PARAM = "$BASE_URL/api/v1/system/setPanelParam" // Settings屏幕brightness
    const val GET_PANEL_PARAM = "$BASE_URL/api/v1/system/setPanelParam" // Get/Retrieve屏幕brightness
    const val SET_PIP = "$BASE_URL/api/v1/system/setPip" // Settingspicture-in-picture
    const val GET_PIP = "$BASE_URL/api/v1/system/getPip" // Get/Retrievepicture-in-picturestate
    const val SET_ZOOM = "$BASE_URL/api/v1/system/setZoom" // Settings放大倍数
    const val GET_ZOOM = "$BASE_URL/api/v1/system/getZoom" // Get/Retrieve放大倍数
    const val SET_SNAPSHOT = "$BASE_URL/api/v1/system/snapshot" // Capture
    const val GET_VRECORD = "$BASE_URL/api/v1/system/vrecord" // Recording
    const val GET_RECORD_STATUS = "$BASE_URL/api/v1/system/getRecordStatus" // Get/Retrieverecordingstate
    const val GET_VERSION = "$BASE_URL/api/v1/system/getVersion" // Get/Retrieveversioninfo
    const val GET_DEVICE_DETAILS = "$BASE_URL/api/v1/system/getDeviceInfo" // Get/Retrievedeviceinfo
    const val GET_FREE_SPACE = "$BASE_URL/api/v1/system/getFreeSpace" // Get/Retrievestorage分区info
    const val GET_FORMAT_STORAGE = "$BASE_URL/api/v1/system/formatStorage" // Format化storage分区
    const val GET_RESET_ALL = "$BASE_URL/api/v1/system/resetAll" // Restore出厂settings
    const val GET_DELETE_FILE = "$BASE_URL/api/v1/system/deleteFile" // Deletephotovideofile
    const val GET_UPGRADE_STATUS = "$BASE_URL/api/v1/system/getUpgradeStatus" // Get/RetrievefirmwareUpgradestate
    const val SET_TEMPERATURE_STATE = "$BASE_URL/api/v1/system/setTemperatureState" // Settingstemperature measurement开关
    const val GET_FILE_LIST = "$BASE_URL/api/v1/system/getFileList" // Get/Retrievefilelist
    const val SET_DATE_TIME = "$BASE_URL/api/v1/system/setDateTime" // Settings时钟
    const val GET_SEND_UPGRADE_FILE_START = "$BASE_URL/api/v1/system/sendUpgradeFileStart" // FirmwareUpgradedata传输start
    const val GET_SEND_UPGRADE_FILE_DATA = "$BASE_URL/api/v1/system/sendUpgradeFileData" // FirmwareUpgradedata传输
    const val GET_SEND_UPGRADE_FILE_END = "$BASE_URL/api/v1/system/sendUpgradeFileEnd" // FirmwareUpgradedata传输end
    const val GET_REMOTE_UPGRADE = "$BASE_URL/api/v1/system/remoteUpgrade" // FirmwareUpgrade
    const val SET_WIFI_AP_ON_OFF = "$BASE_URL/api/v1/system/setWifiAPOnOff" // Settingswifi on/off
    const val GET_WIFI_AP_CONFIG = "$BASE_URL/api/v1/system/getWifiAPConfig" // Get/Retrievewificonfigurationinfo
    const val GET_FILE_COUNT = "$BASE_URL/api/v1/system/getFileCount" // Get/Retrievefilequantity
    const val SET_POWER_ACTION = "$BASE_URL/api/v1/system/setPowerAction" // Settings电源state
    const val SET_DO_NUC = "$BASE_URL/api/v1/system/doNuc" // Nuc
    const val SET_FREEZE = "$BASE_URL/api/v1/system/setFreeze" // Image冻结
    const val GET_BATTERY_INFO = "$BASE_URL/api/v1/system/getBatteryInfo" // Get/Retrieve电池info
    const val SET_TISP = "$BASE_URL/api/v1/system/setTISR" // Settings超分
    const val GET_TISR = "$BASE_URL/api/v1/system/getTISR" // Get/Retrieve超分state
    const val GET_DATE_TIME = "$BASE_URL/api/v1/system/getDateTime" // Get/Retrieve时钟
}
