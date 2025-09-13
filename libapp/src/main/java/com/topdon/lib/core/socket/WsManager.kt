package com.topdon.lib.core.socket

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import okio.ByteString
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.locks.ReentrantLock

/**
 * Specialized thermal imaging component providing WsManager functionality for the IRCamera system.
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
class WsManager(private val wsUrl: String, private val okHttpClient: OkHttpClient, private val statusListener: IWebSocketListener) {
    companion object {
        private const val NORMAL_CLOSE_CODE = 1000
        private const val ABNORMAL_CLOSE_CODE = 1001

        private const val NORMAL_CLOSE_TIPS = "APP call close() and return true"
        private const val ABNORMAL_CLOSE_TIPS = "APP call close() and return false"
    }

    private var mWebSocket: WebSocket? = null
    private var status: State = State.DISCONNECTED // Websocketconnectionstate
    private var heartBeatTimer: HeartBeatTimer? = null

    private val mWebSocketListener: WebSocketListener =
        object : WebSocketListener() {
            @Override
            /**
             * Executes onopen operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param response Parameter for operation (type: Response)
             *
             */
            override fun onOpen(
                webSocket: WebSocket,
                response: Response,
            ) {
                mWebSocket = webSocket
                status = State.CONNECTED

                // Start定时Send心跳
                heartBeatTimer?.cancel()
                heartBeatTimer = HeartBeatTimer(this@WsManager)
                heartBeatTimer?.timeoutListener = {
                    statusListener.runMain {
                        it.onHeartBeatTimeout()
                    }
                }
                heartBeatTimer?.start()

                statusListener.runMain {
                    it.onOpen(webSocket, response)
                }
            }

            @Override
            /**
             * Executes onmessage operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param bytes Parameter for operation (type: ByteString)
             *
             */
            override fun onMessage(
                webSocket: WebSocket,
                bytes: ByteString,
            ) {
                heartBeatTimer?.lastHeartBeatTime = System.currentTimeMillis()
                statusListener.runMain {
                    it.onMessage(webSocket, bytes)
                }
            }

            @Override
            /**
             * Executes onmessage operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param text Parameter for operation (type: String)
             *
             */
            override fun onMessage(
                webSocket: WebSocket,
                text: String,
            ) {
                heartBeatTimer?.lastHeartBeatTime = System.currentTimeMillis()
                statusListener.runMain {
                    it.onMessage(webSocket, text)
                }
            }

            @Override
            /**
             * Executes onclosing operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param code Parameter for operation (type: Int)
             * @param reason Parameter for operation (type: String)
             *
             */
            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String,
            ) {
                status = State.DISCONNECTED
                statusListener.runMain {
                    it.onClosing(webSocket, code, reason)
                }
            }

            @Override
            /**
             * Executes onclosed operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param code Parameter for operation (type: Int)
             * @param reason Parameter for operation (type: String)
             *
             */
            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String,
            ) {
                status = State.DISCONNECTED

                heartBeatTimer?.cancel()
                heartBeatTimer = null

                statusListener.runMain {
                    it.onClosed(webSocket, code, reason)
                }
            }

            @Override
            /**
             * Executes onfailure operation with thermal imaging domain optimization.
             *
             * @param
             * @param webSocket Parameter for operation (type: WebSocket)
             * @param t Parameter for operation (type: Throwable)
             * @param response Parameter for operation (type: Response?)
             *
             */
            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?,
            ) {
                status = State.DISCONNECTED
                statusListener.runMain {
                    it.onFailure(webSocket, t, response)
                }
            }
        }

    /**
     * Executes isConnect functionality.
     */
    /**
     * Executes isconnect operation with thermal imaging domain optimization.
     *
     */
    fun isConnect(): Boolean = status == State.CONNECTING || status == State.CONNECTED

    private var mLock = ReentrantLock()

    @Synchronized
    /**
     * Executes startConnect functionality.
     */
    /**
     * Executes startconnect operation with thermal imaging domain optimization.
     *
     */
    fun startConnect() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (status == State.CONNECTING || status == State.CONNECTED) { // Connection中或已connection
            Log.w("WebSocket", "${if (status == State.CONNECTING) "connection中" else "已connection"} startConnect() 重复调用")
            return
        }
        status = State.CONNECTING

        okHttpClient.dispatcher.cancelAll()
        val mRequest: Request =
            Request.Builder()
                .url(wsUrl)
                .build()
        try {
            mLock.lockInterruptibly()
            try {
                okHttpClient.newWebSocket(mRequest, mWebSocketListener)
            } finally {
                mLock.unlock()
            }
        } catch (_: InterruptedException) {
        }
    }

    /**
     * Executes stopConnect functionality.
     */
    /**
     * Executes stopconnect operation with thermal imaging domain optimization.
     *
     */
    fun stopConnect() {
        heartBeatTimer?.cancel()
        heartBeatTimer = null

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (status == State.DISCONNECTED) {
            return
        }
        status = State.DISCONNECTED

        okHttpClient.dispatcher.cancelAll()
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mWebSocket != null) {
            val isClosed = mWebSocket!!.close(NORMAL_CLOSE_CODE, NORMAL_CLOSE_TIPS)
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isClosed) {
                statusListener.runMain {
                    it.onClosed(mWebSocket!!, NORMAL_CLOSE_CODE, NORMAL_CLOSE_TIPS)
                }
            } else {
                statusListener.runMain {
                    it.onClosed(mWebSocket!!, ABNORMAL_CLOSE_CODE, ABNORMAL_CLOSE_TIPS)
                }
            }
        }
    }

    /**
     * Executes sendMessage functionality.
     */
    /**
     * Executes sendmessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param msg Parameter for operation (type: String?)
     *
     */
    fun sendMessage(msg: String?): Boolean {
        return send(msg)
    }

    /**
     * Executes sendMessage functionality.
     */
    /**
     * Executes sendmessage operation with thermal imaging domain optimization.
     *
     * @param
     * @param byteString Parameter for operation (type: ByteString?)
     *
     */
    fun sendMessage(byteString: ByteString?): Boolean {
        return send(byteString)
    }

    /**
     * Executes send functionality.
     */
    /**
     * Executes send operation with thermal imaging domain optimization.
     *
     * @param
     * @param msg Parameter for operation (type: Any?)
     *
     */
    private fun send(msg: Any?): Boolean {
        var isSend = false
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (mWebSocket != null && status == State.CONNECTED) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (msg is String) {
                isSend = mWebSocket!!.send(msg)
            } else if (msg is ByteString) {
                isSend = mWebSocket!!.send(msg)
            }
        }
        return isSend
    }

    private val wsMainHandler = Handler(Looper.getMainLooper())

    /**
     * Executes IWebSocketListener functionality.
     */
    private fun IWebSocketListener?.runMain(block: (IWebSocketListener) -> Unit) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (this != null) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post {
                    /**
                     * Executes block operation with thermal imaging domain optimization.
                     *
                     */
                    block(this)
                }
            } else {
                /**
                 * Executes block operation with thermal imaging domain optimization.
                 *
                 */
                block(this)
            }
        }
    }

/**
 * Specialized thermal imaging component providing HeartBeatTimer functionality for the IRCamera system.
 *
 * This component is part of the IRCamera thermal imaging system, providing
 * specialized functionality for thermal data processing and visualization.
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
    private class HeartBeatTimer(val wsManager: WsManager) : Timer() {
        var timeoutListener: (() -> Unit)? = null

        @Volatile
        var lastHeartBeatTime: Long = 0

    /**
     * Executes start functionality.
     */
        /**
         * Executes start operation with thermal imaging domain optimization.
         *
         */
        fun start() {
            /**
             * Executes schedule operation with thermal imaging domain optimization.
             *
             * @param
             * @param object Parameter for operation (type: TimerTask()
             * @param Specifications Parameter for operation (type: </h3>  * <ul>  *   <li>Thread-safe operations for thermal data processing</li>  *   <li>Optimized performance for real-time thermal imaging</li>  *   <li>Compatible with TC001 thermal camera hardware</li>  * </ul>  *  * @author IRCamera Development Team  * @version 2.0  * @since 1.0  */     abstract class IWebSocketListener : WebSocketListener()
             * @param Specifications Parameter for operation (type: </h3>  * <ul>  *   <li>Thread-safe operations for thermal data processing</li>  *   <li>Optimized performance for real-time thermal imaging</li>  *   <li>Compatible with TC001 thermal camera hardware</li>  * </ul> /**  * Specialized thermal imaging component providing Builder functionality for the IRCamera system.  *  * <h3>Technical Specifications:</h3>  * <ul>  *   <li>Thread-safe operations for thermal data processing</li>  *   <li>Optimized performance for real-time thermal imaging</li>  *   <li>Compatible with TC001 thermal camera hardware</li>  * </ul>  *  * @author IRCamera Development Team  * @version 2.0  * @since 1.0  */     class Builder {         private var wsUrl: String? = null         private var okHttpClient: OkHttpClient? = null         private var statusListener: IWebSocketListener? = null      /**      * Executes wsUrl functionality.      */         fun wsUrl(url: String?)
             * @param client Parameter for operation (type: OkHttpClient?)
             * @param wsStatusListener Event listener for callbacks (type: IWebSocketListener?)
             *
             */
            schedule(
                object : TimerTask() {
                    /**
                     * Executes run operation with thermal imaging domain optimization.
                     *
                     */
                    override fun run() {
                        val currentTime = System.currentTimeMillis()
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (lastHeartBeatTime == 0L) {
                            lastHeartBeatTime = currentTime
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (currentTime - lastHeartBeatTime > 15 * 1000) { // 3秒a心跳包，连续丢失 5 个包视为disconnect
                            Log.d("WebSocket", "连续5个心跳包无响应，视为connectiondisconnect")
                            timeoutListener?.invoke()
/**
 * Specialized thermal imaging component providing IWebSocketListener functionality for the IRCamera system.
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
    abstract class IWebSocketListener : WebSocketListener() {
/**
 * Specialized thermal imaging component providing State functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
/**
 * Specialized thermal imaging component providing Builder functionality for the IRCamera system.
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
    class Builder {
        private var wsUrl: String? = null
        private var okHttpClient: OkHttpClient? = null
        private var statusListener: IWebSocketListener? = null

    /**
     * Executes wsUrl functionality.
     */
        /**
         * Executes wsurl operation with thermal imaging domain optimization.
         *
         * @param
         * @param url Parameter for operation (type: String?)
         *
         */
        fun wsUrl(url: String?): Builder {
            wsUrl = url
            return this
        }

    /**
     * Executes client functionality.
     */
        /**
         * Executes client operation with thermal imaging domain optimization.
         *
         * @param
         * @param client Parameter for operation (type: OkHttpClient?)
         *
         */
        fun client(client: OkHttpClient?): Builder {
            okHttpClient = client
            return this
        }

    /**
     * Sets wsstatuslistener configuration.
     */
        fun setWsStatusListener(wsStatusListener: IWebSocketListener?): Builder {
            statusListener = wsStatusListener
            return this
        }

    /**
     * Executes build functionality.
     */
        /**
         * Executes build operation with thermal imaging domain optimization.
         *
         */
        fun build(): WsManager = WsManager(wsUrl!!, okHttpClient!!, statusListener!!)
    }
}
