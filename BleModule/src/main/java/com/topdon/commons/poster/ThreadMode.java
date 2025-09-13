package com.topdon.commons.poster;

/**
 * line程策略
 * <p>
 * date: 2019/8/2 23:53
 * author: chuanfeng.bi
 */
public enum ThreadMode {
    /**
     * 和调用者同一line程
     */
    POSTING,
    /**
     * 主line程，UIline程
     */
    MAIN,
    /**
     * 后台line程，同步的
     */
    BACKGROUND,
    /**
     * 异步line程
     */
    ASYNC,
    /**
     * 未指定
     */
    UNSPECIFIED
}
