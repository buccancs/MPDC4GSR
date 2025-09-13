package com.topdon.lib.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.topdon.lib.core.config.FileConfig;
import com.topdon.lib.core.listener.BitmapViewListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for BitmapUtils operations.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
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
public class BitmapUtils {

    public static Bitmap mirror(Bitmap rawBitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1f, 1f);
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.getWidth(), rawBitmap.getHeight(), matrix, true);
    }

    public static Bitmap rotateBitmap(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据rotation angle，生成rotation矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始image按照rotation矩阵进行rotation，并得到新的image
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (returnBm == null) {
            returnBm = bm;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 将bitmapconversion成bytes
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, int quality) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (bitmap == null) {
            return null;
        }
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 将imagesaved到disk中
     *
     * @param bitmap
     * @param file   imagesaved目录——不包含image名
     * @param path   imagesavedfilepath——包含image名
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, File file, File path) {
        boolean success = false;
        byte[] bytes = bitmapToBytes(bitmap, 100);
        OutputStream out = null;
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!file.exists() && file.isDirectory()) {
                file.mkdirs();
            }
            out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 高级image质量compression
     *
     * @param bitmap 位图
     * @param width  compression后的宽度，单位像素
     */
    public static Bitmap imageZoom(Bitmap bitmap, double width) {
        // 将bitmap放至array中，意在获得bitmap的大小（与实际读取的原file要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Format、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        // Get/Retrievebitmap大小 是允许最大大小的多少倍
        return scaleWithWH(newBitmap, width,
                width * newBitmap.getHeight() / newBitmap.getWidth());
    }

    /***
     * imageScale
     *@param bitmap 位图
     * @param w 新的宽度
     * @param h 新的高度
     * @return Bitmap
     */
    public static Bitmap scaleWithWH(Bitmap bitmap, double w, double h) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (w == 0 || h == 0 || bitmap == null) {
            return bitmap;
        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Matrix matrix = new Matrix();
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);

            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        }
    }

    /**
     * bitmapsaved到指定path
     *
     * @param file image的绝对path
     * @param file 位图
     * @return bitmap
     */
    public static boolean saveFile(String file, Bitmap bmp) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (TextUtils.isEmpty(file) || bmp == null) return false;

        File f = new File(file);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (f.exists()) {
            f.delete();
        } else {
            File p = f.getParentFile();
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!p.exists()) {
                p.mkdirs();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 把两个位图覆盖合成为a位图，以底层位图的长宽为基准
     *
     * @param backBitmap  在底部的位图
     * @param frontBitmap 盖在上area的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap, int leftFront, int topFront) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        canvas.drawBitmap(frontBitmap, leftFront, topFront, null);
// If (!frontBitmap.isRecycled()){
// FrontBitmap.recycle();
//        }
        return bitmap;
    }
    public static Bitmap mergeBitmapAlpha(Bitmap backBitmap, Bitmap frontBitmap,Paint paint, int leftFront, int topFront) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        canvas.drawBitmap(frontBitmap, leftFront, topFront, paint);
// If (!frontBitmap.isRecycled()){
// FrontBitmap.recycle();
//        }
        return bitmap;
    }

    public static Bitmap mergeBitmapByView(Bitmap backBitmap, Bitmap frontBitmap, BitmapViewListener view) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Paint paint = new Paint();
        paint.setAlpha((int) (view.getViewAlpha() * 255));
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (view.getViewScale() != 1){
            frontBitmap = scaleWithWH(frontBitmap,view.getViewWidth(),view.getViewHeight());
        }
        canvas.drawBitmap(frontBitmap, view.getViewX(),view.getViewY(), paint);
        frontBitmap.recycle();
        return bitmap;
    }

    @NonNull
    public static Bitmap mergeBitmapByViewNonNull(@NonNull Bitmap backBitmap, @Nullable Bitmap frontBitmap, BitmapViewListener view) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (frontBitmap == null || frontBitmap.isRecycled()) {
            return backBitmap;
        }

        Bitmap bitmap;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (backBitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), backBitmap.getConfig());
        } else {
            bitmap = backBitmap;
        }
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAlpha((int) (view.getViewAlpha() * 255));

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (view.getViewScale() != 1){
            frontBitmap = scaleWithWH(frontBitmap,view.getViewWidth(),view.getViewHeight());
        }
        canvas.drawBitmap(frontBitmap, view.getViewX(),view.getViewY(), paint);
        frontBitmap.recycle();
        return bitmap;
    }

    public static void mergeBitmapByView(Bitmap frontBitmap, BitmapViewListener view,Canvas canvas) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (frontBitmap == null || frontBitmap.isRecycled()) {
            return;
        }
        Paint paint = new Paint();
        paint.setAlpha((int) (view.getViewAlpha() * 255));
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (view.getViewScale() != 1){
            frontBitmap = scaleWithWH(frontBitmap,view.getViewWidth(),view.getViewHeight());
        }
        canvas.drawBitmap(frontBitmap, view.getViewX(),view.getViewY(), paint);
    }

    /**
     * 把两个位图覆盖合成为a位图，以底层位图的长宽为基准
     * @param bytes  在底部的位图
     * @param bytes2 盖在上area的位图
     */
    public static void savaRawFile(byte[] bytes, byte[] bytes2) {
        try {
            File path = new File("/sdcard");
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (!path.exists() && path.isDirectory()) {
                path.mkdirs();
            }
            File file = new File("/sdcard/", "xxx.raw");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.write(bytes2);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * addwatermark
     * @param bmp
     * @param title
     * @param address
     * @param time
     * @param seekBarWidth : 右边pseudo color控件的宽度，防止内容和控件重叠
     * @return
     */
    public static Bitmap drawCenterLable(Bitmap bmp, String title,String address,String time,int seekBarWidth) {
        // Create一样大小的image
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        // Create画布
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(bmp, 0, 0, null);  // 绘制原始image
        canvas.save();
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE); // 白色半透明
        paint.setTextSize(SizeUtils.sp2px(12));
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect rectText = new Rect();  // 得到text占用宽高， 单位：像素
        paint.getTextBounds("占位高度文本", 0,"占位高度文本".length(), rectText);
        double beginX = SizeUtils.dp2px(10);  // 45度angle值是1.414
        double beginY = bmp.getHeight() - SizeUtils.dp2px(10);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(time)){
            beginY = beginY - (rectText.bottom - rectText.top);
            canvas.drawText(time, (int)beginX, (int)beginY, paint);
            beginY -= SizeUtils.dp2px(6);
        }
        int lineWidth = bmp.getWidth() - SizeUtils.dp2px(20) - seekBarWidth;// 一行的可Show/Display内容宽度
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(address)){
            int textHeight = (rectText.bottom - rectText.top);
            paint.getTextBounds(address, 0,address.length(), rectText);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rectText.width() > lineWidth){
                // 字符太长，进行换行processing
                StaticLayout staticLayout = new StaticLayout(address,
                        paint, lineWidth,
                        Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                beginY = beginY - (textHeight+SizeUtils.dp2px(1f))*staticLayout.getLineCount();
                canvas.save();
                canvas.translate( (int)beginX, (int)beginY - textHeight);
                staticLayout.draw(canvas);
                canvas.restore();
            }else {
                beginY = beginY - textHeight;
                canvas.drawText(address, (int)beginX, (int)beginY, paint);
            }
            beginY -= SizeUtils.dp2px(6);
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!TextUtils.isEmpty(title)){
            int textHeight = (rectText.bottom - rectText.top);
            paint.getTextBounds(title, 0,title.length(), rectText);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rectText.width() > lineWidth){
                // 字符太长，进行换行processing
                StaticLayout staticLayout = new StaticLayout(title,
                        paint, lineWidth,
                        Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                beginY = beginY - textHeight*staticLayout.getLineCount();
                canvas.save();
                canvas.translate( (int)beginX, (int)beginY - textHeight);
                staticLayout.draw(canvas);
                canvas.restore();
            }else {
                beginY = beginY - textHeight;
                canvas.drawText(title, (int)beginX, (int)beginY, paint);
            }
            beginY -= SizeUtils.dp2px(6);
        }
        canvas.restore();
        return newBmp;
    }
}
