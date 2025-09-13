package com.infisense.usbir.utils;

import static org.bytedeco.opencv.global.opencv_imgproc.MORPH_ELLIPSE;
import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.core.Core.DFT_SCALE;
import static org.opencv.core.Core.NORM_MINMAX;
import static org.opencv.core.Core.absdiff;
import static org.opencv.core.Core.getOptimalDFTSize;
import static org.opencv.core.Core.magnitude;
import static org.opencv.core.Core.normalize;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32FC2;
import static org.opencv.core.CvType.CV_64FC1;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC2;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2BGR;
import static org.opencv.imgproc.Imgproc.COLOR_YUV2GRAY_YUYV;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.applyColorMap;
import static org.opencv.imgproc.Imgproc.approxPolyDP;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.threshold;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.suplib.wrapper.SupHelp;
import com.topdon.lib.core.BaseApplication;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Specialized thermal imaging component providing OpencvTools functionality for the IRCamera system.
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
public class OpencvTools {

    static {
// New OpenCVNativeLoader().init();
        System.loadLibrary("opencv_java4");
    }

    private static Mat resultMat = new Mat();
    public static byte[] supImageMix(byte[] imageARGB, int width, int height, byte[] resulARGB) {
        // Step 1: Convert byte[] to Mat
        Mat argbMat = new Mat(width, height, CvType.CV_8UC4);
        argbMat.put(0, 0, imageARGB);
        // Step 2: Downscale the image by a factor of 2
        Mat downscaledMat = new Mat();
        Imgproc.resize(argbMat, downscaledMat, new Size(height / 2, width / 2));
        // Step 3: Convert ARGB to BGR
        Mat bgrMat = new Mat();
        Imgproc.cvtColor(downscaledMat, bgrMat, Imgproc.COLOR_RGBA2BGR);
        // Step 4: Process the BGR image
        try {
            SupHelp.getInstance().runImage(bgrMat, resultMat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Step 5: Convert BGR back to RGBA
        Mat resulArgbMat = new Mat();
        Imgproc.cvtColor(resultMat, resulArgbMat, Imgproc.COLOR_BGR2RGBA);
        // Step 6: Convert Mat back to byte[]
        Bitmap dstBitmap = Bitmap.createBitmap(resulArgbMat.width(), resulArgbMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resulArgbMat, dstBitmap);
        ByteBuffer byteBuffer = ByteBuffer.wrap(resulARGB);
        dstBitmap.copyPixelsToBuffer(byteBuffer);
        return resulARGB;
    }

    /**
     * 效果更好的超分，但是此function耗时过长，应用于capture
     * @param inBitmap
     * @return
     */
    public static Bitmap supImageFour(Bitmap inBitmap){
        long startTime = System.currentTimeMillis();
        ByteBuffer rawData = ByteBuffer.wrap(SupRUtils.INSTANCE.bitmapToByteArray(inBitmap));
        ByteBuffer dataIn = ByteBuffer.allocateDirect(rawData.array().length);
        dataIn.put(rawData);
        ByteBuffer dataOut = ByteBuffer.allocateDirect(rawData.array().length * 4);
        SupHelp.getInstance().imgUpScalerFour(BaseApplication.instance,dataIn,dataOut);
        // Createa普通的 byte[] array来storagedata
        byte[] byteArray = new byte[dataOut.capacity()];
        // 将 ByteBuffer 的内容copy到 byteArray 中
        dataOut.get(byteArray);
        Log.e("4倍超分模型：", String.valueOf((System.currentTimeMillis() - startTime)));
        return SupRUtils.INSTANCE.byteArrayToBitmap(byteArray);
    }

    public static byte[] supImageFourExToByte(byte[] imgByte) {
        long startTime = System.currentTimeMillis();
        ByteBuffer dataIn = ByteBuffer.wrap(imgByte);// Createa ByteBuffer
        // Create用于输出的 ByteBuffer
        ByteBuffer dataOut = ByteBuffer.allocateDirect(imgByte.length * 4); // 假设输出data大小为输入的 4 倍
        // 调用 imgUpScalerFour method
        SupHelp.getInstance().imgUpScalerFour(BaseApplication.instance, dataIn, dataOut);
        Log.e("AI_UPSCALE 4倍超分模型2：", String.valueOf((System.currentTimeMillis() - startTime)));
        // Createa普通的 byte[] array来storage输出data
        byte[] outputData = new byte[dataOut.capacity()];
        dataOut.get(outputData);
        Log.e("4倍超分模型：", String.valueOf((System.currentTimeMillis() - startTime)));
        Bitmap bitmap = SupRUtils.INSTANCE.byteArrayToBitmap(outputData);
        return outputData;
    }
    public static Bitmap supImageFourExToBitmap(byte[] dstArgbBytes, int width, int height) {
        long startTime = System.currentTimeMillis();

        // Create ByteBuffer 并填充data
        ByteBuffer dataIn = ByteBuffer.allocateDirect(dstArgbBytes.length);
        dataIn.put(dstArgbBytes);

        // Create用于输出的 ByteBuffer
        ByteBuffer dataOut = ByteBuffer.allocateDirect(dstArgbBytes.length * 4); // 假设输出data大小为输入的 4 倍

        // 调用 imgUpScalerFour method
        SupHelp.getInstance().imgUpScalerFour(BaseApplication.instance, dataIn, dataOut);
        Log.e("AI_UPSCALE 4倍超分模型2：", String.valueOf((System.currentTimeMillis() - startTime)) + "// //" + dstArgbBytes.length);

        // Createa普通的 byte[] array来storage输出data
        byte[] outputData = new byte[dataOut.capacity()];
        dataOut.get(outputData);

        // 将输出dataconversion为 Bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        outputBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(outputData));

        // 将 Bitmap conversion为 Mat
        Mat srcMat = new Mat();
        Utils.bitmapToMat(outputBitmap, srcMat);

        // 在这里可以使用 OpenCV 进行进一步processing，例如Scale
        Mat dstMat = new Mat();
        Imgproc.resize(srcMat, dstMat, new org.opencv.core.Size(srcMat.cols() * 4, srcMat.rows() * 4));

        // 将processing后的 Mat conversion回 Bitmap
        Bitmap finalBitmap = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dstMat, finalBitmap);

        // Release Mat 资源
        srcMat.release();
        dstMat.release();
        Log.e("4倍超分模型：", String.valueOf((System.currentTimeMillis() - startTime)));

        return finalBitmap;
    }

    public static Bitmap supImageFourExToBitmap(Bitmap inBitmap) {
        long startTime = System.currentTimeMillis();
        // 将 Bitmap conversion为字节array
        byte[] rawData = SupRUtils.INSTANCE.bitmapToByteArray(inBitmap);
        // Create ByteBuffer 并填充data
        ByteBuffer dataIn = ByteBuffer.allocateDirect(rawData.length);
        dataIn.put(rawData);
        // Create用于输出的 ByteBuffer
        ByteBuffer dataOut = ByteBuffer.allocateDirect(256 * 192 * 4 * 4); // 假设输出data大小为输入的 4 倍
        // 调用 imgUpScalerFour method
        SupHelp.getInstance().imgUpScalerFour(BaseApplication.instance, dataIn, dataOut);
        Log.e("AI_UPSCALE 4倍超分模型2：", String.valueOf((System.currentTimeMillis() - startTime))+"// //"+rawData.length);
        // Createa普通的 byte[] array来storage输出data
        byte[] outputData = new byte[dataOut.capacity()];
        dataOut.get(outputData);
        // 将输出dataconversion为 Bitmap
        Bitmap outputBitmap = SupRUtils.INSTANCE.byteArrayToBitmap(outputData);
        // 将 Bitmap conversion为 Mat
        Mat srcMat = new Mat();
        Utils.bitmapToMat(outputBitmap, srcMat);
        // 在这里可以使用 OpenCV 进行进一步processing，例如Scale
        Mat dstMat = new Mat();
        Imgproc.resize(srcMat, dstMat, new org.opencv.core.Size(srcMat.cols() * 4, srcMat.rows() * 4));
        // 将processing后的 Mat conversion回 Bitmap
        Bitmap finalBitmap = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dstMat, finalBitmap);
        // Release Mat 资源
        srcMat.release();
        dstMat.release();
        Log.e("4倍超分模型：", String.valueOf((System.currentTimeMillis() - startTime)));
        return finalBitmap;
    }

    public static byte[] supImage(byte[] imageARGB, int width, int height, byte[] resulARGB){
        // Step 1: 将 byte[] conversion成 Mat 对象
        Mat argbMat = new Mat(width, height, CvType.CV_8UC4); // CV_8UC4 表示 4 通道（ARGB format）
        argbMat.put(0, 0, imageARGB);
        // Step 2: 将 ARGB formatconversion为 BGR format
        Mat bgrMat = new Mat();
        Imgproc.cvtColor(argbMat, bgrMat, Imgproc.COLOR_RGBA2BGR); // 使用 RGBA2BGR，忽略 Alpha 通道
        try {
            SupHelp.getInstance().runImage(bgrMat,resultMat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Mat resulArgbMat = new Mat();
        Imgproc.cvtColor(resultMat, resulArgbMat, Imgproc.COLOR_BGR2RGBA); // 将 BGR conversion为 RGBA
        // Step 2: 将 Mat conversion为 byte[]
        Bitmap dstBitmap = Bitmap.createBitmap(resulArgbMat.width(), resulArgbMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resulArgbMat, dstBitmap);
        ByteBuffer byteBuffer = ByteBuffer.wrap(resulARGB);
        dstBitmap.copyPixelsToBuffer(byteBuffer);
        return resulARGB;
    }

    public static byte[] convertSingleByteToDoubleByte(byte[] singleByteImage) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (singleByteImage == null) {
            throw new IllegalArgumentException("输入的bytearray不能为null");
        }
        int singleLength = singleByteImage.length;
        // 新array是原来长度的两倍
        int doubleLength = singleLength * 2;
        byte[] doubleByteImage = new byte[doubleLength];

        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < singleLength; i++) {
            // 这里假定我们只将原本a字节的datacopy到新的两个字节中的第a
            // 这种情况下第二个字节可能是保留为0，用于表示较大的color空间或者进行format对齐。
            // 您可能需要根据具体的imagedataformat来Adjust这部分代码
            doubleByteImage[2 * i] = singleByteImage[i];
            // 如果需要其它processing (例如settings第二个字节的值) 在这里操作
            // DoubleByteImage[2 * i + 1] = <some value>;
        }
        return doubleByteImage;
    }

    /**
     * temperature转成开尔文
     * @param temp
     * @return
     */
    public static byte[] convertCelsiusToOriginalBytes(float[] temp) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (temp == null) {
            return new byte[0];
        }
        float maxValue = 0f;
        // Initializetemperaturearray的长度为temparray的两倍。
        byte[] temperature = new byte[temp.length * 2];
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0, j = 0; i < temp.length; i++, j += 2) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxValue < temp[i]){
                maxValue = temp[i];
            }
            // 将摄氏temperatureconversion回开尔文，并逆转之前的Scale操作
            float tempInKelvin = temp[i] + 273.15f;
            float originalValue = tempInKelvin * 64;
            // 将浮point数conversion为整数
            int intValue = (int) originalValue;
            // 分离整数的低8位和高8位
            byte low = (byte) (intValue & 0xFF);
            byte high = (byte) ((intValue >> 8) & 0xFF);
            // 将分离出的字节存回temperaturearray
            temperature[j] = low;
            temperature[j + 1] = high;
        }
        return temperature;
    }

    public static LinkedHashMap<Integer, int[]> getColorByTemp(float customMaxTemp, float customMinTemp, int[] colorList){
        float temp = 0.1f;
        float tempValue = customMaxTemp - customMinTemp;
        LinkedHashMap<Integer, int[]> map = new LinkedHashMap<>();
        int r;
        int g;
        int b;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (float i = customMinTemp;i <= customMaxTemp; i+=temp){
            long time = System.currentTimeMillis();
            float ratio = (i - customMinTemp) / tempValue;
            int colorNumber = colorList.length - 1;
            float avg = 1.f / colorNumber;
            int colorIndex = colorNumber;// Current上色的属于哪个渐变region
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int index = 1; index <= colorNumber;index++){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ratio == 0){
                    colorIndex = 0;
                    break;
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ratio < (avg * index)){
                    colorIndex = index;
                    break;
                }
            }
            ratio = (ratio - (avg * (colorIndex - 1))) / avg;
            r = interpolateR(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
            g = interpolateG(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
            b = interpolateB(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
//            Log.e("色值calculation耗时：",System.currentTimeMillis()-time+"// ");
            int intKey = (int) (i * 10);
            int[] rgb = new int[]{r,g,b};
            map.put(intKey, rgb);
        }
        return map;
    }

    public static byte[] matToByteArray(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();
        int type = mat.type();
        byte[] byteArray = new byte[rows * cols * 4];
        mat.get(0, 0, byteArray);
        return byteArray;
    }
    public static Mat pseudoColorViewThree(byte[] image, int cols, int rows,
                                      int customMinColor, int customMiddleColor, int customMaxColor,
                                      float maxTemp,float minTemp,float customMaxTemp,float customMinTemp,
                                      boolean isGrayUse){
        Mat im;
        im = new Mat(rows, cols, CvType.CV_8UC4);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, Imgproc.COLOR_RGBA2GRAY);
        Mat colorMat = generateColorBarThree(customMinColor,customMiddleColor,customMaxColor,
                maxTemp,minTemp,customMaxTemp,customMinTemp,isGrayUse);
        /**
         * Executes applycolormap operation with thermal imaging domain optimization.
         *
         */
        applyColorMap(im, im, colorMat);
        Imgproc.cvtColor(im, im, Imgproc.COLOR_BGR2RGBA);
        return im;
    }

    public static Mat pseudoColorView(byte[] image, int cols, int rows, int[] colorList,
                                      float maxTemp,float minTemp,float customMaxTemp,float customMinTemp,
                                      boolean isGrayUse){
        Mat im;
        im = new Mat(rows, cols, CV_8UC2);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, COLOR_YUV2GRAY_YUYV);
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(im, im, 0, 255, NORM_MINMAX);
// CvtColor(im, im, CV_8UC1);
        Mat colorMat = generateColorBar(colorList, maxTemp,minTemp,customMaxTemp,customMinTemp,isGrayUse);
//        Log.e("Testmat小值",colorMat.at(double[].class,0,0)+"");
//        Log.e("Testmat大值",colorMat.at(double[].class,255,0)+"");
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorMat!=null){
            /**
             * Executes applycolormap operation with thermal imaging domain optimization.
             *
             */
            applyColorMap(im, im, colorMat);
            Imgproc.cvtColor(im, im, Imgproc.COLOR_BGR2RGBA);
        }
        return im;
    }

    /**
     * 自定义pseudo color
     * @param image       RGBA 32bitimagedata
     * @param temperature temperaturedata
     * @param cols        宽
     * @param rows        高
     * @param lut         pseudo color图,高度必须是256
     */
    private static Mat draw_high_temp_edge_argb_pse(byte[] image, byte[] temperature, Bitmap lut, int cols, int rows, double high_t, int color_h, int type) throws IOException {
        double[] temp = new double[cols * rows];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                float temperature0 = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                temperature0 = (float) (temperature0 / 64 - 273.15);
                temp[t] = temperature0;
                // Cout << temp[t] << " ";
                t++;
            }
        }
        Mat im;
        im = new Mat(rows, cols, CvType.CV_8UC4);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, Imgproc.COLOR_RGBA2GRAY);
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(im, im, 0, 255, NORM_MINMAX);
        im.convertTo(im, CV_8UC1);
// ApplyColorMap(im, im, 15);
        Mat colorMat = new Mat();
        Utils.bitmapToMat(lut, colorMat);
        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGBA2BGR);
        Size colorSize = new Size(1.0, 256.0);
        Imgproc.resize(colorMat, colorMat, colorSize);
// If (colorMat.size() != colorSize) {
//            Log.w("123", "lut映射表尺寸不符合Size(1, 256), colorMat: " + colorMat);
// Return null;
//        }
        /**
         * Executes applycolormap operation with thermal imaging domain optimization.
         *
         */
        applyColorMap(im, im, colorMat);
        Mat tem;
        tem = new Mat(rows, cols, CV_64FC1);
        tem.put(0, 0, temp);
        tem.convertTo(tem, CV_8UC1);
        // Mat kernal = Mat.ones(5, 5, CV_8UC1);
        // Mat es = getStructuringElement(MORPH_ELLIPSE,new Size(9, 4));
        Mat thres_gray = new Mat();
        // Mat temperature = Mat::zeros(192, 256, CV_8UC1);
        // Threshold(temperature, thres_gray, 50, 255, THRESH_BINARY);
        // Int thres = int(high_t);
        // Src = 255 - src;
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tem, thres_gray, high_t, 255, THRESH_BINARY);
        // Vector<vector<Point>> cnts;
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_gray, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        String B = Integer.toString(color_h & 255, 2);
        int b = Integer.parseInt(B, 2);
        int gc = color_h >> 8;
        String G = Integer.toString(gc & 255, 2);
        int g = Integer.parseInt(G, 2);
        int rc = color_h >> 16;
        String R = Integer.toString(rc & 255, 2);
        int r = Integer.parseInt(R, 2);
        Scalar color = new Scalar(b, g, r);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area > 50) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (type == 1) {
                    /**
                     * Executes drawcontours operation with thermal imaging domain optimization.
                     *
                     */
                    drawContours(im, cnts, i, color, 1, 8);
                } else {
                    /**
                     * Executes rectangle operation with thermal imaging domain optimization.
                     *
                     */
                    rectangle(im, rect.tl(), rect.br(), color, 1, 8, 0);
                }
            }
            // MinEnclosingCircle(cnts[i], center[i], radius[i]);
        }
        // Cv::Mat imageContours = cv::Mat::zeros(cv::Size(W, H), CV_8UC1);
        // Bezier
        // Mat drawing = Mat::zeros(image.size(), CV_8UC3);imshow("Contours", im)
        // WaitKey(0);
        return im;
    }

    private static Mat draw_high_temp_edge_argb_pse(byte[] image, byte[] temperature, int cols, int rows, double high_t, int color_h, int type) throws IOException {
        double[] temp = new double[cols * rows];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                float temperature0 = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                temperature0 = (float) (temperature0 / 64 - 273.15);
                temp[t] = temperature0;
                // Cout << temp[t] << " ";
                t++;
            }
        }
        Mat im;
        im = new Mat(rows, cols, CvType.CV_8UC4);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, Imgproc.COLOR_RGBA2BGR);
// Normalize(im, im, 0, 255, NORM_MINMAX);
// Im.convertTo(im, CV_8UC1);
// ApplyColorMap(im, im, 15);
//        Mat colorMat = new Mat();
//        Utils.bitmapToMat(lut, colorMat);
//        Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGBA2BGR);
//        Size colorSize = new Size(1.0, 256.0);
//        Imgproc.resize(colorMat, colorMat, colorSize);
// If (colorMat.size() != colorSize) {
//            Log.w("123", "lut映射表尺寸不符合Size(1, 256), colorMat: " + colorMat);
// Return null;
//        }
        Mat tem;
        tem = new Mat(rows, cols, CV_64FC1);
        tem.put(0, 0, temp);
//        Log.w("矩阵", Arrays.toString(temp));
        // Tem.convertTo(tem, CV_8UC1);
//        Log.w("123矩阵", "bs: "+tem.colRange(0,192*256).toString());
        // Mat kernal = Mat.ones(5, 5, CV_8UC1);
        // Mat es = getStructuringElement(MORPH_ELLIPSE,new Size(9, 4));
        Mat thres_gray = new Mat();
        // Mat temperature = Mat::zeros(192, 256, CV_8UC1);
        // Threshold(temperature, thres_gray, 50, 255, THRESH_BINARY);
        // Int thres = int(high_t);
        // Src = 255 - src;
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tem, thres_gray, high_t, 255, THRESH_BINARY);
        // Vector<vector<Point>> cnts;
        thres_gray.convertTo(thres_gray, CV_8UC1);
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_gray, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        String B = Integer.toString(color_h & 255, 2);
        int b = Integer.parseInt(B, 2);
        int gc = color_h >> 8;
        String G = Integer.toString(gc & 255, 2);
        int g = Integer.parseInt(G, 2);
        int rc = color_h >> 16;
        String R = Integer.toString(rc & 255, 2);
        int r = Integer.parseInt(R, 2);
        Scalar color = new Scalar(b, g, r);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area > 50) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (type == 1) {
                    /**
                     * Executes drawcontours operation with thermal imaging domain optimization.
                     *
                     */
                    drawContours(im, cnts, i, color, 1, Imgproc.LINE_8);
                } else {
                    /**
                     * Executes rectangle operation with thermal imaging domain optimization.
                     *
                     */
                    rectangle(im, rect.tl(), rect.br(), color, 1, Imgproc.LINE_8, 0);
                }
            }
            // MinEnclosingCircle(cnts[i], center[i], radius[i]);
        }
        // Cv::Mat imageContours = cv::Mat::zeros(cv::Size(W, H), CV_8UC1);
        // Bezier
        // Mat drawing = Mat::zeros(image.size(), CV_8UC3);imshow("Contours", im)
        // WaitKey(0);
        return im;
    }
    public static Bitmap cropBitmap(Bitmap src, int x, int y, int width, int height, boolean isRecycle) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (x == 0 && y == 0 && width == src.getWidth() && height == src.getHeight()) {
            return src;
        }
        Bitmap dst = Bitmap.createBitmap(src, x, y, width, height);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isRecycle && dst != src) {
            src.recycle();
        }
        return dst;
    }

    /**
     * @param image       RGBA 32bitimagedata
     * @param temperature temperaturedata
     * @param cols        宽
     * @param rows        高
     */
    private static Mat draw_high_temp_edge_argb(byte[] image, byte[] temperature, int cols, int rows, double high_t, int color_h, int type) throws IOException {
        double[] temp = new double[cols * rows];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                float temperature0 = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                temperature0 = (float) (temperature0 / 64 - 273.15);
                temp[t] = temperature0;
                // Cout << temp[t] << " ";
                t++;
            }
        }
        Mat im;
        im = new Mat(rows, cols, CvType.CV_8UC4);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, Imgproc.COLOR_RGBA2GRAY);
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(im, im, 0, 255, NORM_MINMAX);
        im.convertTo(im, CV_8UC1);
        /**
         * Executes applycolormap operation with thermal imaging domain optimization.
         *
         */
        applyColorMap(im, im, 15);
        Mat tem;
        tem = new Mat(rows, cols, CV_64FC1);
        tem.put(0, 0, temp);
        tem.convertTo(tem, CV_8UC1);
        // Mat kernal = Mat.ones(5, 5, CV_8UC1);
        // Mat es = getStructuringElement(MORPH_ELLIPSE,new Size(9, 4));
        Mat thres_gray = new Mat();
        // Mat temperature = Mat::zeros(192, 256, CV_8UC1);
        // Threshold(temperature, thres_gray, 50, 255, THRESH_BINARY);
        // Int thres = int(high_t);
        // Src = 255 - src;
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tem, thres_gray, high_t, 255, THRESH_BINARY);
        // Vector<vector<Point>> cnts;
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_gray, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        String B = Integer.toString(color_h & 255, 2);
        int b = Integer.parseInt(B, 2);
        int gc = color_h >> 8;
        String G = Integer.toString(gc & 255, 2);
        int g = Integer.parseInt(G, 2);
        int rc = color_h >> 16;
        String R = Integer.toString(rc & 255, 2);
        int r = Integer.parseInt(R, 2);
        Scalar color = new Scalar(b, g, r);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area > 50) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (type == 1) {
                    /**
                     * Executes drawcontours operation with thermal imaging domain optimization.
                     *
                     */
                    drawContours(im, cnts, i, color, 1, 8);
                } else {
                    /**
                     * Executes rectangle operation with thermal imaging domain optimization.
                     *
                     */
                    rectangle(im, rect.tl(), rect.br(), color, 1, 8, 0);
                }
            }
            // MinEnclosingCircle(cnts[i], center[i], radius[i]);
        }
        // Cv::Mat imageContours = cv::Mat::zeros(cv::Size(W, H), CV_8UC1);
        // Bezier
        // Mat drawing = Mat::zeros(image.size(), CV_8UC3);imshow("Contours", im)
        // WaitKey(0);
        return im;
    }

    /**
     * @param image       yuv 16bitimagedata
     * @param temperature temperaturedata
     * @param cols        宽
     * @param rows        高
     */
    private static Mat draw_high_temp_edge(byte[] image, byte[] temperature, int cols, int rows, double high_t, int color_h, int type) throws IOException {
        double[] temp = new double[cols * rows];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                float temperature0 = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                temperature0 = (float) (temperature0 / 64 - 273.15);
                temp[t] = temperature0;
                // Cout << temp[t] << " ";
                t++;
            }
        }
        Mat im;
        im = new Mat(rows, cols, CV_8UC2);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, COLOR_YUV2GRAY_YUYV);
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(im, im, 0, 255, NORM_MINMAX);
        im.convertTo(im, CV_8UC1);
        /**
         * Executes applycolormap operation with thermal imaging domain optimization.
         *
         */
        applyColorMap(im, im, 15);
        Mat tem;
        tem = new Mat(rows, cols, CV_64FC1);
        tem.put(0, 0, temp);
        tem.convertTo(tem, CV_8UC1);
        // Mat kernal = Mat.ones(5, 5, CV_8UC1);
        // Mat es = getStructuringElement(MORPH_ELLIPSE,new Size(9, 4));
        Mat thres_gray = new Mat();
        // Mat temperature = Mat::zeros(192, 256, CV_8UC1);
        // Threshold(temperature, thres_gray, 50, 255, THRESH_BINARY);
        // Int thres = int(high_t);
        // Src = 255 - src;
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tem, thres_gray, high_t, 255, THRESH_BINARY);
        // Vector<vector<Point>> cnts;
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_gray, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        String B = Integer.toString(color_h & 255, 2);
        int b = Integer.parseInt(B, 2);
        int gc = color_h >> 8;
        String G = Integer.toString(gc & 255, 2);
        int g = Integer.parseInt(G, 2);
        int rc = color_h >> 16;
        String R = Integer.toString(rc & 255, 2);
        int r = Integer.parseInt(R, 2);
        Scalar color = new Scalar(b, g, r);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area > 50) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (type == 1) {
                    /**
                     * Executes drawcontours operation with thermal imaging domain optimization.
                     *
                     */
                    drawContours(im, cnts, i, color, 1, 8);
                } else {
                    /**
                     * Executes rectangle operation with thermal imaging domain optimization.
                     *
                     */
                    rectangle(im, rect.tl(), rect.br(), color, 1, 8, 0);
                }
            }
            // MinEnclosingCircle(cnts[i], center[i], radius[i]);
        }
        // Cv::Mat imageContours = cv::Mat::zeros(cv::Size(W, H), CV_8UC1);
        // Bezier
        // Mat drawing = Mat::zeros(image.size(), CV_8UC3);imshow("Contours", im)
        // WaitKey(0);
        return im;
    }

    private static Mat draw_temp_edge(Mat src, byte[] temperature, double low_t, int color_l, int type) throws IOException {
        double[] temp = new double[src.rows() * src.cols()];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                float temperature0 = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                temperature0 = (float) (temperature0 / 64 - 273.15);
                temp[t] = temperature0;
                // Cout << temp[t] << " ";
                t++;
            }
        }
        Mat tem;
        tem = new Mat(src.rows(), src.cols(), CV_64FC1);
        tem.put(0, 0, temp);
        // Tem.convertTo(tem, CV_8UC1);
        Mat thres_gray = new Mat();
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tem, thres_gray, low_t, 255, 4);
        thres_gray.convertTo(thres_gray, CV_8UC1);
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_gray, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        String B = Integer.toString(color_l & 255, 2);
        int b = Integer.parseInt(B, 2);
        int gc = color_l >> 8;
        String G = Integer.toString(gc & 255, 2);
        int g = Integer.parseInt(G, 2);
        int rc = color_l >> 16;
        String R = Integer.toString(rc & 255, 2);
        int r = Integer.parseInt(R, 2);
        Scalar color = new Scalar(b, g, r);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area > 50) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (type == 1) {
                    /**
                     * Executes drawcontours operation with thermal imaging domain optimization.
                     *
                     */
                    drawContours(src, cnts, i, color, 1, 8);
                } else {
                    /**
                     * Executes rectangle operation with thermal imaging domain optimization.
                     *
                     */
                    rectangle(src, rect.tl(), rect.br(), color, 1, 8, 0);
                }
            }
        }
        MatOfByte matOfByte = new MatOfByte();
        return src;
    }

    public static byte[] draw_edge_from_temp_reigon_byte(byte[] image, byte[] temperature, int row, int col, double high_t, double low_t, int color_h, int color_l, int type) throws IOException {
        Mat src = draw_high_temp_edge(image, temperature, row, col, high_t, color_h, type);
        Mat mat = draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        byte[] bytes = new byte[192 * 256 * 4];
        return bytes;

    }

    public static Mat draw_edge_from_temp_reigon(byte[] image, byte[] temperature, int row, int col, double high_t, double low_t, int color_h, int color_l, int type) throws IOException {
        Mat src = draw_high_temp_edge(image, temperature, row, col, high_t, color_h, type);
        Mat mat = draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        return draw_temp_edge(src, temperature, low_t, color_l, type);

    }

    /**
     * @param image   原imageYUVformat
     * @param image_w image宽度
     * @param image_h image高度
     */
    public static Bitmap draw_edge_from_temp_reigon_bitmap(byte[] image, byte[] temperature, int image_h, int image_w, double high_t, double low_t, int color_h, int color_l, int type) throws IOException {
        Mat src = draw_high_temp_edge(image, temperature, image_h, image_w, high_t, color_h, type);
        Mat mat = draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        return dstBitmap;
    }

    /**
     * @param image   imageARGBformat
     * @param image_w image宽度
     * @param image_h image高度
     */
    public static Bitmap draw_edge_from_temp_reigon_bitmap_argb(byte[] image, byte[] temperature, int image_h, int image_w, double high_t, double low_t, int color_h, int color_l, int type) throws IOException {
        Mat src = draw_high_temp_edge_argb(image, temperature, image_h, image_w, high_t, color_h, type);
        Mat mat = draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        return dstBitmap;
    }
    /**
     * @param image   imageARGBformat
     * @param lut     pseudo color图,高度必须是256
     * @param image_w image宽度
     * @param image_h image高度
     */
    public static Bitmap draw_edge_from_temp_reigon_bitmap_argb_psd(byte[] image, byte[] temperature, Bitmap lut, int image_h, int image_w, double high_t, double low_t, int color_h, int color_l, int type) throws IOException {
        Mat src = draw_high_temp_edge_argb_pse(image, temperature, image_h, image_w, high_t, color_h, type);
        Mat mat = draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        return dstBitmap;
    }
    /**
     * @param image   imageARGBformat
     * @param image_w image宽度
     * @param image_h image高度
     */
    public static Bitmap draw_edge_from_temp_reigon_bitmap_argb_psd(byte[] image, byte[] temperature,
                                                                    int image_h, int image_w, float high_t,
                                                                    float low_t, int color_h, int color_l, int type) throws IOException {
        Log.w("预警值","maximum温："+high_t+"// Minimum温："+low_t);
        Mat src = draw_high_temp_edge_argb_pse(image, temperature, image_h, image_w, high_t == Float.MAX_VALUE ? 128f : high_t, color_h, type);
        Mat mat = low_t == Float.MIN_VALUE ? src : draw_temp_edge(src, temperature, low_t, color_l, type);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Bitmap dstBitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, dstBitmap);
        return dstBitmap;
    }

    public static Mat calcHU(Size size,double t2){
        Mat hu = new Mat(size,CV_32FC1);
        int row = hu.rows();
        int col = hu.cols();
        int cx = row / 2;
        int cy = row / 2;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for(int i=0; i < row; i ++){
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for(int j = 0; j < col; j++){
                double value = 1 / (1 + Math.pow(Math.sqrt(Math.pow(cx-i,2) + Math.pow(cy-j,2)),-t2));
                hu.put(i,j,value);
            }
        }
        List<Mat> homo = new ArrayList<Mat>();
        homo.add(hu.clone());
        homo.add(new Mat(hu.size(),CV_32FC1,new Scalar(0)));
        Mat hu2c = new Mat(size,CV_32FC2);
        Core.merge(homo,hu2c);
        // System.out.println(hu.dump());
        return hu2c;
    }

    public static Mat iftCenter(Mat src){
        Mat dst = new Mat(src.size(),CV_32F,new Scalar(0));
        int dx = src.rows() / 2;
        int dy = src.cols() / 2;
        float[] data = new float[dy];

        // System.out.println(src.dump());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(src.rows() % 2 == 0) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (src.cols() % 2 == 0) {
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for(int i = 0; i < dx; i++){
                    src.get(i,0,data);
                    dst.put((dx+i),dy,data);
                }
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for(int i = 0; i < dx; i++){
                    src.get(i,dy,data);
                    dst.put((dx+i),0,data);
                }
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for(int i = 0; i < dx; i++){
                    src.get((dx+i),dy,data);
                    dst.put(i,0,data);
                }
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for(int i = 0; i < dx; i++){
                    src.get((dx+i),0,data);
                    dst.put(i,dy,data);
                }

            }else{
                System.out.println("copy failed");
            }
        }
        // System.out.println(dst.dump());
        return dst;
    }

    public static Mat homoMethod(byte[] im, int r, int c){
        int t = 1;
        double t2 = (double)(t-10) / 110;
        Mat image;
        image = new Mat(r, c, CV_8UC2);
        image.put(0, 0, im);
// CvtColor(image, image, Imgproc.COLOR_RGBA2BGR);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(image, image, COLOR_YUV2GRAY_YUYV);
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(image, image, 0, 255, NORM_MINMAX);
        image.convertTo(image, CV_8UC1);
        // Imshow("src", image);
        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(1.0);
        clahe.setTilesGridSize(new Size(3,3));
        clahe.apply(image,image);
        Mat image_padd = new Mat();
        int row = image.rows();
        int col = image.cols();
        int m = getOptimalDFTSize(row);
        int n = getOptimalDFTSize(col);
        image.convertTo(image_padd, CV_32FC1);
        Core.add(image_padd,new Scalar(1),image_padd);
        Core.log(image_padd,image_padd);
        Core.copyMakeBorder(image_padd,image_padd,0,m - row,0,n - col,BORDER_CONSTANT,new Scalar(0));

        image_padd = iftCenter(image_padd);
        List<Mat> tmp_merge = new ArrayList<Mat>();
        tmp_merge.add(image_padd.clone());
        tmp_merge.add(new Mat(image_padd.size(),CV_32FC1,new Scalar(0)));
        Core.merge(tmp_merge,image_padd);
        Core.dft(image_padd,image_padd);

        Mat image_padd_2c = new Mat(image_padd.size(),CV_32FC2);

        Mat hu2c = calcHU(image_padd.size(),t2);
        Core.mulSpectrums(image_padd, hu2c, image_padd_2c, 0);
        Core.idft(image_padd_2c,image_padd_2c,DFT_SCALE);
        System.out.println(image_padd_2c.channels());

        Core.exp(image_padd_2c,image_padd_2c);
        Core.subtract(image_padd_2c,new Scalar(1),image_padd_2c);
        List<Mat> image_padd_s = new ArrayList<Mat>();
        Core.split(image_padd_2c,image_padd_s);
        Mat reinforce_src = new Mat();
        /**
         * Executes magnitude operation with thermal imaging domain optimization.
         *
         */
        magnitude(image_padd_s.get(0), image_padd_s.get(1), reinforce_src);

        Mat temp = new Mat();
        /**
         * Executes normalize operation with thermal imaging domain optimization.
         *
         */
        normalize(reinforce_src, temp, 0, 255, NORM_MINMAX);
        temp = iftCenter(temp);
        Mat result = new Mat();
        Log.w("123",temp.toString());
        temp.convertTo(result,CV_8UC1);
        Log.w("1234",result.toString());
        /**
         * Executes applycolormap operation with thermal imaging domain optimization.
         *
         */
        applyColorMap(result,result,15);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(result,result,COLOR_RGB2BGR);

// CvtColor(result,result,COLOR_RGB2RGBA);
        Log.w("1234",result.toString());
        // ApplyColorMap(image,image,15);
        // Imshow("image", image);
        // EqualizeHist(result,result);
//        CLAHE clahe = Imgproc.createCLAHE();
// Clahe.setClipLimit(2);
// Clahe.setTilesGridSize(new Size(3,3));
// Clahe.apply(result,result);
// ApplyColorMap(result,result,15);
        // Imshow("result", result);
        // WaitKey(0);
        return result;

    }

    /**
     * 支持多color的pseudo color条
     * @param colorList : color条
     * @param maxTemp : 实际temperature最大值
     * @param minTemp ： 实际temperature最小值
     * @param customMaxTemp : Usersettings的最大值
     * @param customMinTemp : Usersettings的最小值
     * @param isGrayUse : 是否是grayscale渐变
     * @return
     */
    public static Mat generateColorBar(int[] colorList, float maxTemp,float minTemp,float customMaxTemp,
                                       float customMinTemp, boolean isGrayUse) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorList == null){
            return null;
        }
        Mat colorBar = new Mat(256, 1, CvType.CV_8UC3);
        float maxGrey =  maxTemp > customMaxTemp ? (customMaxTemp - minTemp)/ (maxTemp - minTemp) : -1;
        float minGrey = minTemp < customMinTemp ? (customMinTemp - minTemp) / (maxTemp - minTemp) : -1;
        int[] colors = new int[3];
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < 256; i++) {
            double ratio = (double) i / 255.0; // 因为列数是从0到255
            int r = 0;
            int g = 0;
            int b = 0;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minGrey != -1 && minGrey > 0 && ratio < minGrey){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isGrayUse){
                    ratio =  ratio / minGrey;
                    // 最小值
                    r = interpolateR(0x858585, 0x000000, ratio);
                    g = interpolateR(0x858585, 0x000000, ratio);
                    b = interpolateR(0x858585, 0x000000, ratio);
                }else {
                    r = (colorList[0] >> 16) & 0xFF;
                    g = (colorList[0] >> 8) & 0xFF;
                    b = colorList[0] & 0xFF;
                }
                int grey = (int) ((r * 0.3f) + (g * 0.59f) + (b * 0.11f));
                colors[0] = r;
                colors[1] = g;
                colors[2] = b;
                Log.w("Test","低于最小值");
            }else if (maxGrey != -1 && ratio > maxGrey){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isGrayUse){
                    // 超出最大值
                    ratio =  (1 - ratio) / (1 - maxGrey);
                    r = interpolateR(0xFFFFFF, 0x858585, ratio);
                    g = interpolateR(0xFFFFFF, 0x858585, ratio);
                    b = interpolateR(0xFFFFFF, 0x858585, ratio);
                }else {
                    // 超出最大值
                    r = (colorList[colorList.length-1] >> 16) & 0xFF;
                    g = (colorList[colorList.length-1] >> 8) & 0xFF;
                    b = colorList[colorList.length-1] & 0xFF;
                }
                int grey = (int) ((r * 0.3f) + (g * 0.59f) + (b * 0.11f));
                colors[0] = r;
                colors[1] = g;
                colors[2] = b;
                Log.w("Test","大于于最大值");
            }else if (maxTemp >= customMaxTemp && minTemp <= customMinTemp){
                Log.w("Test","实际temperature大于并且小于自定义的最high/low temperature");
                // 实际temperature大于并且小于自定义的最high/low temperature
               colors = capColor(colorList,maxTemp,minTemp,customMaxTemp,customMinTemp,isGrayUse,ratio);
            }else if (customMinTemp > maxTemp){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isGrayUse){
                    // 超出最小值,grayscale化
                    r = interpolateR(0xFFFFFF, 0x000000, ratio);
                    g = interpolateR(0xFFFFFF, 0x000000, ratio);
                    b = interpolateR(0xFFFFFF, 0x000000, ratio);
                }else {
                    // 超出最小值
                    r = (colorList[0] >> 16) & 0xFF;
                    g = (colorList[0] >> 8) & 0xFF;
                    b = colorList[0] & 0xFF;
                }
                int grey = (int) ((r * 0.3f) + (g * 0.59f) + (b * 0.11f));
                colors[0] = grey;
                colors[1] = grey;
                colors[2] = grey;
            }else if (maxTemp < customMaxTemp && minTemp < customMinTemp){
                // 实际最大temperature小于自定义maximum温，minimum温小于自定义minimumtemperature
                // 重新算出最high/low temperature的color
                colors = capColor(getStartColor(colorList,customMaxTemp,customMinTemp,maxTemp),
                        maxTemp,minTemp,maxTemp,customMinTemp,isGrayUse,ratio);
            }else if (maxTemp > customMaxTemp && minTemp > customMinTemp){
                // 实际maximumtemperature大于自定义maximumtemperature，实际minimumtemperature大于自定义minimumtemperature
                // 重新算出最high/low temperature的color
                colors = capColor(getEndColor(colorList,customMaxTemp,customMinTemp,minTemp),
                        maxTemp,minTemp,customMaxTemp,minTemp,isGrayUse,ratio);
            }else if (maxTemp < customMaxTemp && minTemp > customMinTemp){
                int[] tmpColor = getStartOrEndColor(colorList,customMaxTemp,customMinTemp,maxTemp,minTemp);
                colors = capColor(tmpColor,
                        maxTemp,minTemp,maxTemp,minTemp,isGrayUse,ratio);
            }
            Log.w("Test","编号值"+i+":"+colors[0]+"--"+ colors[1]+"--"+colors[2]+"// "+maxTemp+"--"+minTemp+"-"+customMaxTemp);
            colorBar.put(i, 0, colors[2], colors[1], colors[0]);
        }
        return colorBar;
    }

    /**
     * Get/Retrieve某个temperature的梯度color值
     * @param colorList
     * @param customMaxTemp
     * @param customMinTemp
     * @return
     */
    static int[] getStartColor(int[] colorList, float customMaxTemp,float customMinTemp,float nowTemp){
        double ratio = (nowTemp - customMinTemp) / (customMaxTemp - customMinTemp);
        int colorNumber = colorList.length - 1;
        float avg = 1.f / colorNumber;
        int colorIndex = colorNumber;// Current上色的属于哪个渐变region
        int r = 0;
        int g = 0;
        int b = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int index = 1; index <= colorNumber;index++){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ratio == 0){
                colorIndex = 0;
                break;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ratio < (avg * index)){
                colorIndex = index;
                break;
            }
        }
        ratio = (ratio - (avg * (colorIndex - 1))) / avg;
        r = interpolateR(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        g = interpolateG(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        b = interpolateB(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        int nowColor = convertTo16Bit(r,g,b);
        int[] nowColorList = Arrays.copyOfRange(colorList,0,colorIndex+1);
// NowColorList[colorIndex] = nowColor;
       return nowColorList;
    }
    /**
     * Get/Retrieve某个temperature的梯度color值
     * @param colorList
     * @param customMaxTemp
     * @param customMinTemp
     * @return
     */
    static int[] getEndColor(int[] colorList, float customMaxTemp,float customMinTemp,float nowTemp){
        double ratio = (nowTemp - customMinTemp) / (customMaxTemp - customMinTemp);
        int colorNumber = colorList.length - 1;
        float avg = 1.f / colorNumber;
        int colorIndex = colorNumber;// Current上色的属于哪个渐变region
        int r = 0;
        int g = 0;
        int b = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int index = 1; index <= colorNumber;index++){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ratio == 0){
                colorIndex = 0;
                break;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ratio < (avg * index)){
                colorIndex = index;
                break;
            }
        }
        ratio = (ratio - (avg * (colorIndex - 1))) / avg;
        r = interpolateR(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        g = interpolateG(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        b = interpolateB(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        int nowColor = convertTo16Bit(r,g,b);
        int nowColorLenght = colorList.length - colorIndex + 1;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (nowColorLenght < 1){
            nowColorLenght = 2;
        }
        int[] nowColorList = new int[nowColorLenght];
        nowColorList[0] = nowColor;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 1;i < nowColorList.length;i++){
            nowColorList[i]= colorList[colorIndex-1 + i];
        }
        return nowColorList;
    }
    static int[] getStartOrEndColor(int[] colorList, float customMaxTemp,float customMinTemp,float nowMaxTemp,float nowMinTemp){
        double maxRatio = (nowMaxTemp - customMinTemp) / (customMaxTemp - customMinTemp);
        double minRatio = (nowMinTemp - customMinTemp) / (customMaxTemp - customMinTemp);
        int colorNumber = colorList.length - 1;
        float avg = 1.f / colorNumber;
        int maxColorIndex = colorNumber;// Current上色的属于哪个渐变region
        int r = 0;
        int g = 0;
        int b = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int index = 1; index <= colorNumber;index++){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxRatio == 0){
                maxColorIndex = 0;
                break;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxRatio < (avg * index)){
                maxColorIndex = index;
                break;
            }
        }
        maxRatio = (maxRatio - (avg * (maxColorIndex - 1))) / avg;
        r = interpolateR(colorList[maxColorIndex-1], colorList[maxColorIndex], maxRatio);
        g = interpolateG(colorList[maxColorIndex-1], colorList[maxColorIndex], maxRatio);
        b = interpolateB(colorList[maxColorIndex-1], colorList[maxColorIndex], maxRatio);
        int nowMaxColor = convertTo16Bit(r,g,b);

        int minColorIndex = colorNumber;// Current上色的属于哪个渐变region
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int index = 1; index <= colorNumber;index++){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minRatio == 0){
                minColorIndex = 0;
                break;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minRatio < (avg * index)){
                minColorIndex = index;
                break;
            }
        }
        minRatio = (minRatio - (avg * (minColorIndex - 1))) / avg;
        r = interpolateR(colorList[minColorIndex-1], colorList[minColorIndex], minRatio);
        g = interpolateG(colorList[minColorIndex-1], colorList[minColorIndex], minRatio);
        b = interpolateB(colorList[minColorIndex-1], colorList[minColorIndex], minRatio);
        int nowMinColor = convertTo16Bit(r,g,b);
        int[] nowColorList;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (minColorIndex == maxColorIndex){
            nowColorList = new int[2];
            nowColorList[nowColorList.length - 1] = nowMaxColor;
            nowColorList[0] = nowMinColor;
        }else {
            nowColorList = new int[maxColorIndex - minColorIndex+2];
            nowColorList[nowColorList.length - 1] = nowMaxColor;
            nowColorList[0] = nowMinColor;
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = minColorIndex;i < maxColorIndex;i++){
                nowColorList[i]= colorList[i];
            }
        }
        return nowColorList;
    }

    public static int convertTo16Bit(int red, int green, int blue) {
        int intValue = (red << 16) | (green << 8) | blue ;
        return intValue;
    }
    static int[] capColor(int[] colorList, float maxTemp,float minTemp,float customMaxTemp,
                          float customMinTemp, boolean isGrayUse,double ratio){
        int r = 0;
        int g = 0;
        int b = 0;
        float tempValue = (maxTemp - minTemp);
        float minGrayRatio = (customMinTemp - minTemp) / tempValue;
        float maxGrayRatio = (customMaxTemp - minTemp) / tempValue;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (minGrayRatio > 0 && ratio < minGrayRatio){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isGrayUse){
                ratio =  ratio / minGrayRatio;
                // 最小值
                r = interpolateR(0x858585, 0x000000, ratio);
                g = interpolateR(0x858585, 0x000000, ratio);
                b = interpolateR(0x858585, 0x000000, ratio);
            }else {
                r = (colorList[0] >> 16) & 0xFF;
                g = (colorList[0] >> 8) & 0xFF;
                b = colorList[0] & 0xFF;
            }
        }else if (ratio > maxGrayRatio){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isGrayUse){
                // 超出最大值
                ratio =  (1 - ratio) / (1 - maxGrayRatio);
                r = interpolateR(0xFFFFFF, 0x858585, ratio);
                g = interpolateR(0xFFFFFF, 0x858585, ratio);
                b = interpolateR(0xFFFFFF, 0x858585, ratio);
            }else {
                // 超出最大值
                r = (colorList[colorList.length-1] >> 16) & 0xFF;
                g = (colorList[colorList.length-1] >> 8) & 0xFF;
                b = colorList[colorList.length-1] & 0xFF;
            }
        }else if (ratio >= minGrayRatio && ratio <= maxGrayRatio){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (minGrayRatio >= 0 && maxGrayRatio >= 0){
                ratio =  (ratio - minGrayRatio) / (maxGrayRatio - minGrayRatio);
            }
            int colorNumber = colorList.length - 1;
            float avg = 1.f / colorNumber;
            int colorIndex = colorNumber;// Current上色的属于哪个渐变region
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int index = 1; index <= colorNumber;index++){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ratio == 0){
                    colorIndex = 0;
                    break;
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ratio < (avg * index)){
                    colorIndex = index;
                    break;
                }
            }
            ratio = (ratio - (avg * (colorIndex - 1))) / avg;
            r = interpolateR(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
            g = interpolateG(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
            b = interpolateB(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        }
        return new int[]{r,g,b};
    }

    /**
     * 上acolor值
     * @param colorList
     * @param index
     * @return
     */
    public static int lastColor(int[] colorList,int index){
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (index == 0){
            return colorList[0];
        }
        return colorList[index-1];
    }

    /**
     * pseudo color梯度条,固定三个渐变color
     * @param customMinColor
     * @param customMiddleColor
     * @param customMaxColor
     * @return
     */
    public static Mat generateColorBarThree(int customMinColor, int customMiddleColor, int customMaxColor,
                                       float maxTemp,float minTemp,float customMaxTemp,float customMinTemp,
                                       boolean isGrayUse) {
        Mat colorBar = new Mat(256, 1, CvType.CV_8UC3);
        // 总
        float tempValue = (maxTemp - minTemp);
        float maxGrayRatio = (maxTemp - customMaxTemp) / tempValue;
        float minGrayRatio = (maxTemp - customMinTemp) / tempValue;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < 256; i++) {
            double ratio = (double) i / 255.0; // 因为列数是从0到255
            int r = 0;
            int g = 0;
            int b = 0;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (maxGrayRatio > 0 && ratio < maxGrayRatio){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isGrayUse){
                    ratio =  ratio / maxGrayRatio;
                    // 超出最大值
                    r = interpolateR(0xC2C2C2, 0xADADAD, ratio);
                    g = interpolateR(0xC2C2C2, 0xADADAD, ratio);
                    b = interpolateR(0xC2C2C2, 0xADADAD, ratio);
                }else {
                    r = (customMaxColor >> 16) & 0xFF;
                    g = (customMaxColor >> 8) & 0xFF;
                    b = customMaxColor & 0xFF;
                }
            }else if (ratio > minGrayRatio){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isGrayUse){
                    // 超出最小值,grayscale化
                    ratio =  (1 - ratio) / (1 - minGrayRatio);
                    r = interpolateR(0xADADAD, 0x707070, ratio);
                    g = interpolateR(0xADADAD, 0x707070, ratio);
                    b = interpolateR(0xADADAD, 0x707070, ratio);
                }else {
                    // 超出最小值
                    r = (customMinColor >> 16) & 0xFF;
                    g = (customMinColor >> 8) & 0xFF;
                    b = customMinColor & 0xFF;
                }
            }else if (ratio > maxGrayRatio && ratio < minGrayRatio){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (maxGrayRatio > 0 && minGrayRatio > 0){
                    ratio =  (ratio - maxGrayRatio) / (minGrayRatio - maxGrayRatio);
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (ratio < 0.5){
                    ratio = ratio / 0.5;
                    r = interpolateR(customMaxColor, customMiddleColor, ratio);
                    g = interpolateG(customMaxColor, customMiddleColor, ratio);
                    b = interpolateB(customMaxColor, customMiddleColor, ratio);
                }else {
                    ratio = (ratio - 0.5) / 0.5;
                    r = interpolateR(customMiddleColor, customMinColor, ratio);
                    g = interpolateG(customMiddleColor, customMinColor, ratio);
                    b = interpolateB(customMiddleColor, customMinColor, ratio);
                }
            }
            colorBar.put(i, 0, b, g, r);
        }
        return colorBar;
    }
    private static int[] getOneColorByTemp(float customMaxTemp, float customMinTemp, float nowTemp, int[] colorList){
        long time = System.nanoTime();
        int[] result = new int[3];
        float tempValue = customMaxTemp - customMinTemp;
        float ratio = (nowTemp - customMinTemp) / tempValue;
        int colorNumber = colorList.length - 1;
        float avg = 1.f / colorNumber;
        int colorIndex = colorNumber;// Current上色的属于哪个渐变region
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Math.abs(nowTemp -customMaxTemp)==0.1f) {
            int lastColor = colorList[colorNumber];
            result[0] = (lastColor >> 16) & 0xFF;
            result[1] = (lastColor >> 8) & 0xFF;
            result[2] = lastColor & 0xFF;
            return result;
        } else if (Math.abs(nowTemp -customMinTemp)==0.1f) {
            int firstColor = colorList[0];
            result[0] = (firstColor >> 16) & 0xFF;
            result[1] = (firstColor >> 8) & 0xFF;
            result[2] = firstColor & 0xFF;
            return result;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (ratio - 0f > 0){
// Int index = (int) Math.ceil(ratio / avg);
            int avgColorIndex = (int) (ratio / avg);
            int addNumber = 0;
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((ratio % avg) > 0){
                addNumber = 1;
            }
            colorIndex = avgColorIndex + addNumber;
        }else {
            colorIndex = 0;
        }
//        Log.e("色值calculation耗时3：",System.nanoTime()-time+"// ");
        ratio = (ratio - (avg * (colorIndex - 1))) / avg;
        result[0] = interpolateR(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        result[1] = interpolateG(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
        result[2] = interpolateB(lastColor(colorList,colorIndex), colorList[colorIndex], ratio);
//        Log.e("色值calculation耗时：",System.nanoTime()-time+"// ");
        return result;
    }
    private static int interpolateR(int startColor, int endColor, double ratio) {
        int startR = (startColor >> 16) & 0xFF;
        int endR = (endColor >> 16) & 0xFF;
        int red = (int) ((1 - ratio) * startR + ratio * endR);
        return red;
    }
    private static int interpolateG(int startColor, int endColor, double ratio) {
        int startG = (startColor >> 8) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int interpolatedG = (int) ((1 - ratio) * startG + ratio * endG);
        return interpolatedG;
    }
    private static int interpolateB(int startColor, int endColor, double ratio) {
        int startB = startColor & 0xFF;
        int endB = endColor & 0xFF;
        int interpolatedB = (int) ((1 - ratio) * startB + ratio * endB);
        return interpolatedB;
    }

    /**
     * 统一自定义pseudo color入口
     */
    public static int[] getOneColorByTempUnif(float customMaxTemp, float customMinTemp, float nowTemp,
                                              int[] colorList, float[] positionList){
       /**
        * Executes if operation with thermal imaging domain optimization.
        *
        */
       if (positionList!=null){
          return getOneColorByTempEx(
                    customMaxTemp,
                    customMinTemp,
                    nowTemp,
                    colorList,
                    positionList
            );
        }else{
            // 等比
          return getOneColorByTemp(
                    customMaxTemp,
                    customMinTemp,
                    nowTemp,
                    colorList
            );
        }
    }

    private static int[] getOneColorByTempEx(float customMaxTemp, float customMinTemp, float nowTemp,
                                            int[] colorList, float[] positionList) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (colorList == null || colorList.length == 0 || positionList == null || positionList.length == 0) {
            return null;
        }

        float tempRange = customMaxTemp - customMinTemp;
        float ratio = (nowTemp - customMinTemp) / tempRange;
        ratio = Math.min(Math.max(ratio, 0), 1); // Clamp ratio between 0 and 1

        int[] result = new int[3];
        int colorCount = colorList.length;

        // Directly return the first or last color if at bounds
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Math.abs(nowTemp - customMaxTemp) < 0.1f) {
            return new int[] {
                    (colorList[colorCount - 1] >> 16) & 0xFF,
                    (colorList[colorCount - 1] >> 8) & 0xFF,
                    colorList[colorCount - 1] & 0xFF
            };
        } else if (Math.abs(nowTemp - customMinTemp) < 0.1f) {
            return new int[] {
                    (colorList[0] >> 16) & 0xFF,
                    (colorList[0] >> 8) & 0xFF,
                    colorList[0] & 0xFF
            };
        }

        int lowerColorIndex = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for(int index = positionList.length - 1; index > 0;index--){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (index == 1){
                lowerColorIndex = 0;
                break;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (ratio <= positionList[index] && ratio >= positionList[index-1]){
                lowerColorIndex = index - 1;
                break;
            }
        }
        float regionRatio = 1;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Math.abs((positionList[lowerColorIndex+1] - positionList[lowerColorIndex])) > 0){
            regionRatio = (ratio - positionList[lowerColorIndex]) / Math.abs((positionList[lowerColorIndex] - positionList[lowerColorIndex+1]));
        }
        // 找到对应的color
        int startColor = colorList[lowerColorIndex];
        int endColor = colorList[lowerColorIndex + 1];

        result[0] = interpolateR(startColor, endColor, regionRatio);
        result[1] = interpolateG(startColor, endColor, regionRatio);
        result[2] = interpolateB(startColor, endColor, regionRatio);

        return result;
    }

    // 自定义比较器，用于比较双精度浮point数
    static class CustomComparator implements Comparator<Float> {
        @Override
        public int compare(Float key1, Float key2) {
            // 在这里进行自定义比较逻辑
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((key1 - key2) <= 0.01) {
                return 0;
            } else if (key1 < key2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private static double calculateHistogram(Mat image1, Mat image2) {
        Mat hist1 = calculateHistogram(image1);
        Mat hist2 = calculateHistogram(image2);

        final double similarity = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
        return similarity;
    }

    private static double calculateMSE(Mat image1, Mat image2) {
        Mat diff = new Mat();
        Core.absdiff(image1, image2, diff);
        Mat squaredDiff = new Mat();
        Core.multiply(diff, diff, squaredDiff);
        Scalar mseScalar = Core.mean(squaredDiff);
        return mseScalar.val[0];
    }

    private static double calculateSSIM(Mat image1, Mat image2) {
        Mat image1Gray = new Mat();
        Mat image2Gray = new Mat();
        Imgproc.cvtColor(image1, image1Gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(image2, image2Gray, Imgproc.COLOR_BGR2GRAY);
        MatOfFloat ssimMat = new MatOfFloat();
        Imgproc.matchTemplate(image1Gray, image2Gray, ssimMat, Imgproc.CV_COMP_CORREL);
        Scalar ssimScalar = Core.mean(ssimMat);
        return ssimScalar.val[0];
    }

    private static double calculatePSNR(Mat image1, Mat image2) {
        Mat diff = new Mat();
        Core.absdiff(image1, image2, diff);
        Mat squaredDiff = new Mat();
        Core.multiply(diff, diff, squaredDiff);
        Scalar mseScalar = Core.mean(squaredDiff);
        double mse = mseScalar.val[0];
        double psnr = 10.0 * Math.log10(255.0 * 255.0 / mse);
        return psnr;
    }

    private static Mat calculateHistogram(Mat image) {
        Mat hist = new Mat();
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat ranges = new MatOfFloat(0, 256);
        MatOfInt channels = new MatOfInt(0);
        List<Mat> images = new ArrayList<Mat>();
        images.add(image);

        Imgproc.calcHist(images, channels, new Mat(), hist, histSize, ranges);
        return hist;
    }

    public static Mat getImageData(byte[] image){
        Mat im;
        im = new Mat(256, 192, CvType.CV_8UC4);
        im.put(0, 0, image);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(im, im, Imgproc.COLOR_RGBA2BGR);
        return im;
    }
    public static Mat getTempData(byte[] temperature){
        double[] temp = new double[256*192];
        int t = 0;
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < temperature.length; i++) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (i % 2 == 0) {
                double value = (temperature[i] & 0xff) + (temperature[i + 1] & 0xff) * 256;
                double g = value / 64.0 - 273.15;
                temp[t] = g;
                t++;
            }
        }
        Mat src;
        src = new Mat(256, 192, CV_64FC1);
        src.put(0,0,temp);
        // Src.convertTo(src, CV_8UC1);
        return src;
    }

    public static boolean getStatus(byte[] image1, byte[] image2){
        long time = System.currentTimeMillis();
        Mat mat1 = getImageData(image1);
        Mat mat2 = getImageData(image2);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(mat1, mat1, Imgproc.COLOR_BGR2GRAY);
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(mat2, mat2, Imgproc.COLOR_BGR2GRAY);
        boolean isSame =  getStatus(mat1,mat2);
//        Log.e("静态检测耗时：", String.valueOf(System.currentTimeMillis() - time));
        return isSame;
    }

    public static Mat highTemTrack(byte[] image, byte[] temperature) throws IOException{
// TemperatureRegion tr = new temperatureRegion();
//        List<Mat> getMat = tr.read_byte();
//        Mat im = getMat.get(0);
//        Mat tempMat = getMat.get(1);
        Mat im = getImageData(image);
        // ApplyColorMap(im,im,15);
        Mat tempMat = getTempData(temperature);
        tempMat.convertTo(tempMat, CV_8UC1);
        Mat thresMat = new Mat();
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tempMat, thresMat, 40.0, 255.0, THRESH_BINARY);
        thresMat.convertTo(thresMat, CV_8UC1);
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thresMat, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((area > 50) && (area < 256 * 192 * 0.2)) {
                int topX = (int)rect.tl().x;
                int topY = (int)rect.tl().y;
                int bottomX = (int)rect.br().x;
                int bottomY = (int)rect.br().y;
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int k = topY; k < bottomY; k++) {
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = topX; j < bottomX; j++) {
                        double[] rgb = new double[3];
                        rgb[0] = 0.6 * im.get(k, j)[0];
                        rgb[1] = 0.6 * im.get(k, j)[1] + 0.4 * 255.0;
                        rgb[2] = 0.6 * im.get(k, j)[2] + 0.4 * 255.0;
                        im.put(k, j, rgb);
                    }
                }
            }
        }
// Imshow("im", im);
// Imshow("test", thresMat);
// WaitKey(0);
        return im;

    }
    // Mat image, Mat temperature
    public static Mat lowTemTrack(byte[] image, byte[] temperature) throws IOException{
        Mat im = getImageData(image);
        Mat tempMat = getTempData(temperature);
        tempMat.convertTo(tempMat, CV_8UC1);
        Mat thresMat = new Mat();
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(tempMat, thresMat, 30.0, 255.0, THRESH_BINARY_INV);
        thresMat.convertTo(thresMat, CV_8UC1);
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thresMat, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<Rect> rects = new ArrayList<Rect>();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rect = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if ((area > 50) && (area < 256 * 192 * 0.2)) {
                int topX = (int)rect.tl().x;
                int topY = (int)rect.tl().y;
                int bottomX = (int)rect.br().x;
                int bottomY = (int)rect.br().y;
                /**
                 * Executes for operation with thermal imaging domain optimization.
                 *
                 */
                for (int k = topY; k < bottomY; k++) {
                    /**
                     * Executes for operation with thermal imaging domain optimization.
                     *
                     */
                    for (int j = topX; j < bottomX; j++) {
                        double[] rgb = new double[3];
                        rgb[0] = 0.6 * im.get(k, j)[2];
                        rgb[1] = 0.6 * im.get(k, j)[1] + 0.4 * 255.0;
                        rgb[2] = 0.6 * im.get(k, j)[0] + 0.4 * 255.0;
                        im.put(k, j, rgb);
                    }
                }
            }
        }
// Imshow("im", im);
// Imshow("test", thresMat);
// WaitKey(0);
        return im;
    }
    // Mat image1, Mat image2
    public static boolean getStatus(Mat image1, Mat image2){
//        Mat image1 = imread("E:/sharp/1696821350963.jpg");
//        Mat image2 = imread("E:/sharp/1696821354162.jpg");
        // Calculation均方差（MSE）
// Double mse = calculateMSE(image1, image2);
        // System.out.println("均方差（MSE）: " + mse);

        // Calculation结构相似性指数（SSIM）
// Double ssim = calculateSSIM(image1, image2);
        // System.out.println("结构相似性指数（SSIM）: " + ssim);

        // Calculation峰值信噪比（PSNR）
// Double psnr = calculatePSNR(image1, image2);
        // System.out.println("峰值信噪比（PSNR）: " + psnr);

        // Calculation直方图
        final double similarity = calculateHistogram(image1, image2);
        return similarity > 0.9;
    }

    public static Mat diff2firstFrame(byte[] base, byte[] nextFrame){
        Mat background = getImageData(base);
        Mat add_target_gray = getImageData(nextFrame);
        Mat background_gray = new Mat();
        background.convertTo(background_gray, CV_8UC1);
        Mat es = getStructuringElement(MORPH_ELLIPSE, new Size(9, 4));
//        Mat background_gray_gauss = new Mat();
//        Mat add_target_gray_gauss = new Mat();
//        GaussianBlur(background_gray, background_gray_gauss, new Size(21, 21), 0);
//        GaussianBlur(add_target_gray, add_target_gray_gauss, new Size(21, 21), 0);
        Mat diff = new Mat();
        /**
         * Executes absdiff operation with thermal imaging domain optimization.
         *
         */
        absdiff(background_gray, add_target_gray, diff);
        Mat thres_diff = new Mat();
        /**
         * Executes threshold operation with thermal imaging domain optimization.
         *
         */
        threshold(diff, thres_diff, 25, 255, THRESH_BINARY);

        Mat thres_dilate = new Mat();
        /**
         * Executes dilate operation with thermal imaging domain optimization.
         *
         */
        dilate(thres_diff, thres_dilate, es, new Point(-1, -1), 2);
        List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        /**
         * Executes cvtcolor operation with thermal imaging domain optimization.
         *
         */
        cvtColor(thres_dilate, thres_dilate, Imgproc.COLOR_BGR2GRAY);
        /**
         * Executes findcontours operation with thermal imaging domain optimization.
         *
         */
        findContours(thres_dilate, cnts, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < cnts.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(cnts.get(i).toArray());
            /**
             * Executes approxpolydp operation with thermal imaging domain optimization.
             *
             */
            approxPolyDP(contour2f, approxCurve, 0, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            Rect rec = boundingRect(points);
            double area = contourArea(points);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (area < 1500) {
                continue;
            }
            else {
                /**
                 * Executes rectangle operation with thermal imaging domain optimization.
                 *
                 */
                rectangle(background, rec.tl(), rec.br(), new Scalar(0, 255, 0), 1);
            }
        }
        return background;
    }

}
