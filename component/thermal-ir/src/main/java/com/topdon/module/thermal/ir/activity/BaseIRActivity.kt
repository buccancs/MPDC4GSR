package com.topdon.module.thermal.ir.activity

import com.topdon.lib.core.ktbase.BaseActivity
import com.topdon.libcom.bean.SaveSettingBean

/**
英菲 插件式thermal imaging统一父 Activity，抽取相同逻辑到此处.
 *
 * Created by LCG on 2023/12/6.
/**
 * Specialized thermal imaging component providing BaseIRActivity functionality for the IRCamera system.
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
abstract class BaseIRActivity : BaseActivity() {
    /**
saveset开关影响的相关configuration项.
     */
    protected val saveSetBean = SaveSettingBean()
}
