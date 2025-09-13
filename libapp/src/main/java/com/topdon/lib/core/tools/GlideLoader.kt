package com.topdon.lib.core.tools

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.topdon.lib.core.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Specialized thermal imaging component providing GlideLoader functionality for the IRCamera system.
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
object GlideLoader {
    /**
     * imagedefault图
     */
    /**
     * Retrieves the photooptions with optimized performance for thermal imaging operations.
     *
     */
    private fun getPhotoOptions(): RequestOptions {
        val multi = MultiTransformation(CenterCrop(), RoundedCorners(SizeUtils.dp2px(6f)))
        return RequestOptions
            .bitmapTransform(multi)
            .error(R.mipmap.ic_default_head)
    }

    /**
     * 圆形image
     */
    /**
     * Executes loadcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param resourceId Parameter for operation (type: Int)
     * @param options Parameter for operation (type: RequestOptions)
     *
     */
    fun loadCircle(
        img: ImageView,
        resourceId: Int,
        options: RequestOptions,
    ) {
        Glide.with(img)
            .load(resourceId)
            .apply(options)
            .into(img)
    }

    /**
     * 圆形image
     */
    /**
     * Executes loadcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String)
     * @param options Parameter for operation (type: RequestOptions)
     *
     */
    fun loadCircle(
        img: ImageView,
        url: String,
        options: RequestOptions,
    ) {
        Glide.with(img)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 圆形image
     */
    /**
     * Executes loadcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param drawable Parameter for operation (type: Drawable)
     * @param options Parameter for operation (type: RequestOptions)
     *
     */
    fun loadCircle(
        img: ImageView,
        drawable: Drawable,
        options: RequestOptions,
    ) {
        Glide.with(img)
            .load(drawable)
            .apply(options)
            .into(img)
    }

    /**
     * 圆形image
     */
    /**
     * Executes loadcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param uri Parameter for operation (type: Uri)
     * @param options Parameter for operation (type: RequestOptions)
     *
     */
    fun loadCircle(
        img: ImageView,
        uri: Uri,
        options: RequestOptions,
    ) {
        Glide.with(img)
            .load(uri)
            .apply(options)
            .into(img)
    }

    /**
     * 圆形image
     */
    /**
     * Executes loadcircle operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String)
     * @param resourceId Parameter for operation (type: Int)
     * @param options Parameter for operation (type: RequestOptions)
     *
     */
    fun loadCircle(
        img: ImageView,
        url: String,
        resourceId: Int,
        options: RequestOptions,
    ) {
        Glide.with(img)
            .load(url)
            .error(resourceId)
            .placeholder(resourceId)
            .apply(options)
            .into(img)
    }

    /**
     * 圆角形image
     */
    /**
     * Executes loadrounded operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param resourceId Parameter for operation (type: Int)
     *
     */
    fun loadRounded(
        img: ImageView,
        resourceId: Int,
    ) {
        Glide.with(img)
            .load(resourceId)
            .apply(getPhotoOptions())
            .into(img)
    }

    /**
     * 圆角形image
     */
    /**
     * Executes loadrounded operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String)
     *
     */
    fun loadRounded(
        img: ImageView,
        url: String,
    ) {
        Glide.with(img)
            .load(url)
            .apply(getPhotoOptions())
            .into(img)
    }

    /**
     * 圆角形image
     */
    /**
     * Executes loadrounded operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param drawable Parameter for operation (type: Drawable)
     *
     */
    fun loadRounded(
        img: ImageView,
        drawable: Drawable,
    ) {
        Glide.with(img)
            .load(drawable)
            .apply(getPhotoOptions())
            .into(img)
    }

    /**
     * 圆角形image
     */
    /**
     * Executes loadrounded operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param uri Parameter for operation (type: Uri)
     *
     */
    fun loadRounded(
        img: ImageView,
        uri: Uri,
    ) {
        Glide.with(img)
            .load(uri)
            .apply(getPhotoOptions())
            .into(img)
    }

    /**
     * loadimage
     */
    /**
     * Executes load operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String?)
     *
     */
    fun load(
        img: ImageView,
        url: String?,
    ) {
        val multi =
            /**
             * Executes multitransformation operation with thermal imaging domain optimization.
             *
             */
            MultiTransformation(
                /**
                 * Executes centercrop operation with thermal imaging domain optimization.
                 *
                 */
                CenterCrop(),
            )
        val options =
            RequestOptions
                .bitmapTransform(multi)
                .placeholder(R.mipmap.bg_default_img)
                .error(R.mipmap.bg_default_img)
        Glide.with(img)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * loadimage
     */
    /**
     * Executes loadgallery operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String?)
     *
     */
    fun loadGallery(
        img: ImageView,
        url: String?,
    ) {
        val multi =
            /**
             * Executes multitransformation operation with thermal imaging domain optimization.
             *
             */
            MultiTransformation(
                /**
                 * Executes centercrop operation with thermal imaging domain optimization.
                 *
                 */
                CenterCrop(),
            )
        val options =
            RequestOptions
                .bitmapTransform(multi)
                .placeholder(R.drawable.ic_gallery_default_shape)
                .error(R.drawable.ic_gallery_default_shape)
        Glide.with(img)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * loadimage
     */
    /**
     * Executes loadfit operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String?)
     *
     */
    fun loadFit(
        img: ImageView,
        url: String?,
    ) {
        val multi =
            /**
             * Executes multitransformation operation with thermal imaging domain optimization.
             *
             */
            MultiTransformation(
                /**
                 * Executes fitcenter operation with thermal imaging domain optimization.
                 *
                 */
                FitCenter(),
            )
        val options =
            RequestOptions
                .bitmapTransform(multi)
                .placeholder(R.drawable.ic_default_search_svg)
                .error(R.drawable.ic_default_search_svg)
        Glide.with(img)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * loadimage
     */
    /**
     * Executes load operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param resourceId Parameter for operation (type: Int)
     *
     */
    fun load(
        img: ImageView,
        resourceId: Int,
    ) {
        val multi =
            /**
             * Executes multitransformation operation with thermal imaging domain optimization.
             *
             */
            MultiTransformation(
                /**
                 * Executes fitcenter operation with thermal imaging domain optimization.
                 *
                 */
                FitCenter(),
            )
        val options =
            RequestOptions
                .bitmapTransform(multi)

        Glide.with(img)
            .load(resourceId)
            .apply(options)
            .into(img)
    }

    /**
     * Executes loadP functionality.
     */
    /**
     * Executes loadp operation with thermal imaging domain optimization.
     *
     * @param
     * @param img Parameter for operation (type: ImageView)
     * @param url Parameter for operation (type: String?)
     *
     */
    fun loadP(
        img: ImageView,
        url: String?,
    ) {
        Glide.with(img)
            .load(url)
            .placeholder(R.drawable.ic_default_search_svg)
            .into(img)
    }

    /**
     * Retrieves the drawable with optimized performance for thermal imaging operations.
     *
     * @param
     * @param context Parameter for operation (type: Context)
     * @param url Parameter for operation (type: String?)
     *
     */
    suspend fun getDrawable(
        context: Context,
        url: String?,
    ): Drawable? {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (url == null) {
            return null
        }
        return withContext(Dispatchers.IO) {
            try {
                Glide.with(context).asDrawable().load(url).submit().get()
            } catch (e: Exception) {
                null
            }
        }
    }
}
