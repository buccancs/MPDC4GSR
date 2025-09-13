package com.infisense.usbir.tools

/**
 * Specialized thermal imaging component providing OpencvTools functionality for the IRCamera system.
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
object OpencvTools {
    // Init {
//        System.loadLibrary("opencv_java4");
//    }
//
//
// Fun mergeBitmap(backBitmap: Bitmap,frontBitmap : Bitmap) : Bitmap?{
// Val time = System.currentTimeMillis()
// If (backBitmap == null || backBitmap.isRecycled() || frontBitmap == null || frontBitmap.isRecycled()) {
// Return null
//        }
// Var backM = Mat()
// Var frontM = Mat()
// Var dst = Mat()
//        Utils.bitmapToMat(backBitmap, backM)
//        Utils.bitmapToMat(frontBitmap, frontM)
// AddWeighted(backM, 1.0, frontM, 0.0, 0.0, dst)
// Val dstBitmap = Bitmap.createBitmap(backM.width(), backM.height(), Bitmap.Config.ARGB_8888)
//        Utils.matToBitmap(dst,dstBitmap)
//        Log.w("opencvimageMerge时间耗时：","${System.currentTimeMillis() - time}")
// Return dstBitmap
//    }
}
