package com.shuyu.gsyvideoplayer.video.base

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Minimal stub implementation of GSYVideoPlayer
 * This is a temporary placeholder to resolve build dependencies
 * TODO: Replace with actual GSY Video Player library when available
 */
open class GSYVideoPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    open fun startPlayLogic() {
        // Stub implementation
    }
    
    open fun release() {
        // Stub implementation
    }
    
    open fun onBackFullscreen() {
        // Stub implementation
    }
    
    open fun getCurrentState(): Int = 0
    
    companion object {
        const val CURRENT_STATE_NORMAL = 0
        const val CURRENT_STATE_PLAYING = 1
        const val CURRENT_STATE_PAUSE = 2
        const val CURRENT_STATE_ERROR = 3
    }
}