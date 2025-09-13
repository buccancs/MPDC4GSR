package com.topdon.lib.core.http.ts004

object TS004URL {
    const val RTSP_URL = "rtsp://192.168.40.1/ch0/stream0"
    private const val BASE_URL = "http://192.168.40.1:8080"
    const val SET_PSEUDO_COLOR = "$BASE_URL/api/v1/system/setPseudoColor" // settingspseudo color样式
    const val GET_PSEUDO_COLOR = "$BASE_URL/api/v1/system/getPseudoColor" // 获取pseudo color样式
    const val SET_PANEL_PARAM = "$BASE_URL/api/v1/system/setPanelParam" // settings屏幕brightness
    const val GET_PANEL_PARAM = "$BASE_URL/api/v1/system/setPanelParam" // 获取屏幕brightness
    const val SET_PIP = "$BASE_URL/api/v1/system/setPip" // settingspicture-in-picture
    const val GET_PIP = "$BASE_URL/api/v1/system/getPip" // 获取picture-in-picturestate
    const val SET_ZOOM = "$BASE_URL/api/v1/system/setZoom" // settings放大倍数
    const val GET_ZOOM = "$BASE_URL/api/v1/system/getZoom" // 获取放大倍数
    const val SET_SNAPSHOT = "$BASE_URL/api/v1/system/snapshot" // capture
    const val GET_VRECORD = "$BASE_URL/api/v1/system/vrecord" // recording
    const val GET_RECORD_STATUS = "$BASE_URL/api/v1/system/getRecordStatus" // 获取recordingstate
    const val GET_VERSION = "$BASE_URL/api/v1/system/getVersion" // 获取versioninfo
    const val GET_DEVICE_DETAILS = "$BASE_URL/api/v1/system/getDeviceInfo" // 获取deviceinfo
    const val GET_FREE_SPACE = "$BASE_URL/api/v1/system/getFreeSpace" // 获取storage分区info
    const val GET_FORMAT_STORAGE = "$BASE_URL/api/v1/system/formatStorage" // format化storage分区
    const val GET_RESET_ALL = "$BASE_URL/api/v1/system/resetAll" // restore出厂settings
    const val GET_DELETE_FILE = "$BASE_URL/api/v1/system/deleteFile" // deletephotovideofile
    const val GET_UPGRADE_STATUS = "$BASE_URL/api/v1/system/getUpgradeStatus" // 获取firmware升级state
    const val SET_TEMPERATURE_STATE = "$BASE_URL/api/v1/system/setTemperatureState" // settingstemperature measurement开关
    const val GET_FILE_LIST = "$BASE_URL/api/v1/system/getFileList" // 获取filelist
    const val SET_DATE_TIME = "$BASE_URL/api/v1/system/setDateTime" // settings时钟
    const val GET_SEND_UPGRADE_FILE_START = "$BASE_URL/api/v1/system/sendUpgradeFileStart" // firmware升级data传输start
    const val GET_SEND_UPGRADE_FILE_DATA = "$BASE_URL/api/v1/system/sendUpgradeFileData" // firmware升级data传输
    const val GET_SEND_UPGRADE_FILE_END = "$BASE_URL/api/v1/system/sendUpgradeFileEnd" // firmware升级data传输end
    const val GET_REMOTE_UPGRADE = "$BASE_URL/api/v1/system/remoteUpgrade" // firmware升级
    const val SET_WIFI_AP_ON_OFF = "$BASE_URL/api/v1/system/setWifiAPOnOff" // settingswifi on/off
    const val GET_WIFI_AP_CONFIG = "$BASE_URL/api/v1/system/getWifiAPConfig" // 获取wificonfigurationinfo
    const val GET_FILE_COUNT = "$BASE_URL/api/v1/system/getFileCount" // 获取filequantity
    const val SET_POWER_ACTION = "$BASE_URL/api/v1/system/setPowerAction" // settings电源state
    const val SET_DO_NUC = "$BASE_URL/api/v1/system/doNuc" // nuc
    const val SET_FREEZE = "$BASE_URL/api/v1/system/setFreeze" // image冻结
    const val GET_BATTERY_INFO = "$BASE_URL/api/v1/system/getBatteryInfo" // 获取电池info
    const val SET_TISP = "$BASE_URL/api/v1/system/setTISR" // settings超分
    const val GET_TISR = "$BASE_URL/api/v1/system/getTISR" // 获取超分state
    const val GET_DATE_TIME = "$BASE_URL/api/v1/system/getDateTime" // 获取时钟
}
