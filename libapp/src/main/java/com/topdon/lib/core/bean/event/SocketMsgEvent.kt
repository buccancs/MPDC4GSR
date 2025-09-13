package com.topdon.lib.core.bean.event

/**
 * 接收到远端 Socket 发送过来的一条message事件.
 * Created by LCG on 2024/4/23.
 *
 * @param text message内容
 */
data class SocketMsgEvent(val text: String)
