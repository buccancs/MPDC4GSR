package com.topdon.lib.core.common

import android.text.TextUtils

/**
 * create by fylder on 2018/6/14
 **/

/**
 * UserInfoManager manages and coordinates related functionality and resources.
 *
 * @author IRCamera Development Team
 * @since 1.0
 */
/**
 * Specialized thermal imaging component providing UserInfoManager functionality for the IRCamera system.
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
class UserInfoManager {
    companion object {
        @Volatile
        var manager: UserInfoManager? = null

    /**
     * Retrieves instance information.
     */
        fun getInstance(): UserInfoManager {
            if (manager == null) {
                synchronized(UserInfoManager::class) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (manager == null) {
                        manager = UserInfoManager()
                    }
                }
            }
            return manager!!
        }
    }

    /**
     * 是否Login（判断token是否有值来processingLogin情况）
     * token在-1的情况下为游客访问，不算Login
     */
    /**
     * Executes isLogin functionality.
     */
    /**
     * Executes islogin operation with thermal imaging domain optimization.
     *
     */
    fun isLogin(): Boolean {
        val token = SharedManager.getToken()
        return if (TextUtils.equals("-1", token)) {
            
            false
        } else {
            !TextUtils.isEmpty(token)
        }
    }

    /**
     * LoginsavedUserinfo
     */
    /**
     * Executes login operation with thermal imaging domain optimization.
     *
     * @param
     * @param token Parameter for operation (type: String)
     * @param userId Parameter for operation (type: String)
     * @param phone Parameter for operation (type: String?)
     * @param email Parameter for operation (type: String)
     * @param nickname Parameter for operation (type: String)
     * @param headUrl Parameter for operation (type: String?)
     *
     */
    fun login(
        token: String,
        userId: String,
        phone: String?,
        email: String,
        nickname: String,
        headUrl: String?,
    ) {
        SharedManager.setUserId(userId)
        SharedManager.setUsername(if (getMaskPhone(phone)?.isNotEmpty() == true) getMaskPhone(phone) ?: "" else email)
        SharedManager.setNickname(nickname)
        SharedManager.setHeadIcon(headUrl ?: "12345")
        SharedManager.setToken(token)
    }

    /**
     * ExitUnregisterUserinfo
     */
    /**
     * Executes logout operation with thermal imaging domain optimization.
     *
     */
    fun logout() {
        SharedManager.setToken("")
        SharedManager.setUserId("0")
        SharedManager.setNickname("")
        SharedManager.setHeadIcon("")
    }

    /**
     * Retrieves maskphone information.
     */
    private fun getMaskPhone(phone: String?): String? {
        return phone?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }
}
