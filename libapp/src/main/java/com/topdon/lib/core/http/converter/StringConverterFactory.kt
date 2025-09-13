package com.topdon.lib.core.http.converter

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Specialized thermal imaging component providing StringConverterFactory functionality for the IRCamera system.
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
class StringConverterFactory : Converter.Factory() {
    /**
     * Executes responsebodyconverter operation with thermal imaging domain optimization.
     *
     * @param
     * @param type Parameter for operation (type: Type)
     * @param annotations Parameter for operation (type: Array<Annotation>)
     * @param retrofit Parameter for operation (type: Retrofit)
     *
     */
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        return if (String::class.java == type) {
            Converter<ResponseBody, String> { value -> value.string() }
        } else {
            null
        }
    }

    /**
     * Executes requestbodyconverter operation with thermal imaging domain optimization.
     *
     * @param
     * @param type Parameter for operation (type: Type)
     * @param parameterAnnotations Parameter for operation (type: Array<Annotation>)
     * @param methodAnnotations Parameter for operation (type: Array<Annotation>)
     * @param retrofit Parameter for operation (type: Retrofit)
     *
     */
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody>? {
        return if (String::class.java == type) {
            Converter<String, RequestBody> { value -> value.toRequestBody(MEDIA_TYPE) }
        } else {
            null
        }
    }

    companion object {
        private val MEDIA_TYPE = "text/plain".toMediaTypeOrNull()

    /**
     * Executes create functionality.
     */
        /**
         * Executes create operation with thermal imaging domain optimization.
         *
         */
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }
}
