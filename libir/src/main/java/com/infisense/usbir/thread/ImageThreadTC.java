package com.infisense.usbir.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.energy.iruvc.sdkisp.LibIRProcess;
import com.energy.iruvc.utils.CommonParams;
import com.energy.iruvc.utils.SynchronizedBitmap;
import com.example.open3d.JNITool;
import com.example.suplib.wrapper.SupHelp;
import com.infisense.usbir.bean.ColorRGB;
import com.infisense.usbir.utils.IRImageHelp;
import com.infisense.usbir.utils.OpencvTools;
import com.infisense.usbir.utils.PseudocodeUtils;
import com.topdon.lib.core.bean.AlarmBean;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/*
 * @Description:
 * @Author:         brilliantzhao
 * @CreateDate:     2022.2.24 11:06
 * @UpdateUser:
 * @UpdateDate:     2022.2.24 11:06
 * @UpdateRemark:
 */
/**
 * Specialized thermal imaging component providing ImageThreadTC functionality for the IRCamera system.
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
public class ImageThreadTC extends Thread {

    public static final int TYPE_AI_C = -1;// 不开启
    public static final int TYPE_AI_D = 0;// 动态检测
    public static final int TYPE_AI_H = 1;// High temperature source检测
    public static final int TYPE_AI_L = 2;// Low temperature source检测

    private byte[] imgTmp;
    private String TAG = "ImageThread";
    private Context mContext;
    private Bitmap bitmap;
    private SynchronizedBitmap syncimage;
    private int imageWidth;
    private int imageHeight;
    private byte[] imageSrc;
    private byte[] temperatureSrc;
    private boolean rotate; // 屏幕rotation
    //
    private CommonParams.DataFlowMode dataFlowMode = CommonParams.DataFlowMode.IMAGE_AND_TEMP_OUTPUT;
    private byte[] imageYUV422;
    private byte[] imageARGB;
    private byte[] imageDst;
    public byte[] imageTemp;// 艾睿需要的Testdata，processing完可以delete

    private byte[] imageY8;
    private float max = Float.MAX_VALUE;
    private float min = Float.MIN_VALUE;
    private int maxColor;
    private int minColor;
    private int rotateInt;

    private int pseudocolorMode = 3;
    private AlarmBean alarmBean;

    private byte[] firstFrame = null;
    private byte[] firstTemp = null;
    private int typeAi = TYPE_AI_C;
    private IRImageHelp irImageHelp;

    private volatile boolean isOpenAmplify = false;
    private final byte[] amplifyRotateArray;
    public static final int MULTIPLE = 2;

    public void setOpenAmplify(boolean openAmplify) {
        isOpenAmplify = openAmplify;
    }

    public int getTypeAi() {
        return typeAi;
    }

    public void setTypeAi(int typeAi) {
        this.typeAi = typeAi;
    }

    public AlarmBean getAlarmBean() {
        return alarmBean;
    }

    public void setAlarmBean(AlarmBean alarmBean) {
        this.alarmBean = alarmBean;
    }

    public void setSyncImage(SynchronizedBitmap syncimage) {
        this.syncimage = syncimage;
    }

    public void setImageSrc(byte[] imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getPseudocolorMode() {
        return pseudocolorMode;
    }

    public void setPseudocolorMode(int pseudocolorMode) {
        this.pseudocolorMode = pseudocolorMode;
    }

    public void setTemperatureSrc(byte[] temperatureSrc) {
        this.temperatureSrc = temperatureSrc;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public void setRotate(int rotateInt) {
        this.rotateInt = rotateInt;
    }

    /**
     * Executes imagethreadtc operation with thermal imaging domain optimization.
     *
     */
    public ImageThreadTC(Context context, int imageWidth, int imageHeight) {
        Log.i(TAG, "ImageThread create->imageWidth = " + imageWidth + " imageHeight = " + imageHeight);
        this.mContext = context;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        imageYUV422 = new byte[imageWidth * imageHeight * 2];
        imageARGB = new byte[imageWidth * imageHeight * 4];
        imageDst = new byte[imageWidth * imageHeight * 4];
        imgTmp = new byte[imageWidth * imageHeight * 4];
        imageTemp = new byte[imageDst.length];
        imageY8 = new byte[imageWidth * imageHeight];
        irImageHelp = new IRImageHelp();
        amplifyRotateArray = new byte[imageWidth * MULTIPLE * imageHeight * MULTIPLE * 4];
    }

    public void setLimit(float max, float min) {
        this.max = max;
        this.min = min;
    }

    public void setLimit(float max, float min, int maxColor, int minColor) {
        this.max = max;
        this.min = min;
        this.maxColor = maxColor;
        this.minColor = minColor;
    }

    public void setDataFlowMode(CommonParams.DataFlowMode dataFlowMode) {
        this.dataFlowMode = dataFlowMode;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void run() {
        /**
         * Executes while operation with thermal imaging domain optimization.
         *
         */
        while (!isInterrupted()) {
            /**
             * Executes synchronized operation with thermal imaging domain optimization.
             *
             */
            synchronized (syncimage.dataLock) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (syncimage.start) {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (irImageHelp.getColorList() != null) {
                        LibIRProcess.convertYuyvMapToARGBPseudocolor(imageSrc, imageHeight * imageWidth, CommonParams.PseudoColorType.PSEUDO_1, imageARGB);
                    } else {
                        LibIRProcess.convertYuyvMapToARGBPseudocolor(imageSrc, imageHeight * imageWidth, PseudocodeUtils.INSTANCE.changePseudocodeModeByOld(pseudocolorMode), imageARGB);
                    }
                    /*
                     * 经过conversion之后的infrareddata
                     * 其中的data是rotation90度的，需要rotation回来,infraredrotation的逻辑放在这里processing。
                     */
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (rotateInt == 270) {
                        LibIRProcess.ImageRes_t imageRes = new LibIRProcess.ImageRes_t();
                        imageRes.height = (char) imageWidth;
                        imageRes.width = (char) imageHeight;
                        LibIRProcess.rotateRight90(imageARGB, imageRes,
                                CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888, imageDst);
                    } else if (rotateInt == 90) {
                        LibIRProcess.ImageRes_t imageRes = new LibIRProcess.ImageRes_t();
                        imageRes.height = (char) imageWidth;
                        imageRes.width = (char) imageHeight;
                        LibIRProcess.rotateLeft90(imageARGB, imageRes,
                                CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888, imageDst);
                    } else if (rotateInt == 180) {
                        LibIRProcess.ImageRes_t imageRes = new LibIRProcess.ImageRes_t();
                        imageRes.width = (char) imageHeight;
                        imageRes.height = (char) imageWidth;
                        LibIRProcess.rotate180(imageARGB, imageRes,
                                CommonParams.IRPROCSRCFMTType.IRPROC_SRC_FMT_ARGB8888, imageDst);
                    } else {
                        imageDst = imageARGB;
                    }
                    irImageHelp.customPseudoColor(imageDst,temperatureSrc,imageWidth,imageHeight);
                    /*
                     * 等温尺processing,展示pseudo color的temperaturerange内info
                     */
                    irImageHelp.setPseudoColorMaxMin(imageDst,temperatureSrc,max,min,imageWidth,imageHeight);
                }
                imageDst = irImageHelp.contourDetection(alarmBean,
                        imageDst, temperatureSrc,
                        (rotateInt == 270 || rotateInt == 90) ? imageWidth : imageHeight,
                        (rotateInt == 270 || rotateInt == 90) ? imageHeight : imageWidth);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (typeAi == TYPE_AI_H){
                    byte[] dataArray = JNITool.INSTANCE.maxTempL(imageDst,temperatureSrc,
                            (rotateInt == 270 || rotateInt == 90) ? imageWidth : imageHeight,
                            (rotateInt == 270 || rotateInt == 90) ? imageHeight : imageWidth,-1);
                    Mat diffMat = new Mat(192, 256, CvType.CV_8UC3);
                    diffMat.put(0, 0, dataArray);
                    Imgproc.cvtColor(diffMat, diffMat, Imgproc.COLOR_BGR2RGBA);
                    byte [] grayData = new byte[diffMat.cols()*diffMat.rows()*4];
                    diffMat.get(0,0,grayData);
                    imageDst = grayData;
                }else if (typeAi == TYPE_AI_L){
                    byte[] dataArray = JNITool.INSTANCE.lowTemTrack(imageDst,temperatureSrc,
                            (rotateInt == 270 || rotateInt == 90) ? imageWidth : imageHeight,
                            (rotateInt == 270 || rotateInt == 90) ? imageHeight : imageWidth,-1);
                    Mat diffMat = new Mat(192, 256, CvType.CV_8UC3);
                    diffMat.put(0, 0, dataArray);
                    Imgproc.cvtColor(diffMat, diffMat, Imgproc.COLOR_BGR2RGBA);
                    byte [] grayData = new byte[diffMat.cols()*diffMat.rows()*4];
                    diffMat.get(0,0,grayData);
                    imageDst = grayData;
                }else if (typeAi == TYPE_AI_D) {
                    int firstTime = 0;
                    // 静态闯入algorithm
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (firstFrame == null || firstTemp == null) {
                        firstFrame = new byte[imageDst.length];
                        firstTemp = new byte[temperatureSrc.length];
                        System.arraycopy(imageDst, 0, firstFrame, 0, imageDst.length);
                        System.arraycopy(temperatureSrc, 0, firstTemp, 0, temperatureSrc.length);
                    } else {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (OpencvTools.getStatus(firstFrame, imageDst)) {
                            try {
                                byte[] dataArray = JNITool.INSTANCE.diff2firstFrameByTempWH(
                                        (rotateInt == 270 || rotateInt == 90) ? imageWidth : imageHeight,
                                        (rotateInt == 270 || rotateInt == 90) ? imageHeight : imageWidth,
                                        firstTemp, temperatureSrc, imageDst);
                                Mat diffMat = new Mat(192, 256, CvType.CV_8UC4);
                                diffMat.put(0, 0, dataArray);
                                Imgproc.cvtColor(diffMat, diffMat, Imgproc.COLOR_RGB2RGBA);
                                byte[] grayData = new byte[diffMat.cols() * diffMat.rows() * 4];
                                diffMat.get(0, 0, grayData);
                                imageDst = grayData;
                                firstTime++;
                            } catch (Throwable e) {
                                Log.e("静态闯入exception：", e.getMessage());
                            }
                        } else {
                            // 相似度不同，则代表手机抖动
                            System.arraycopy(imageDst, 0, firstFrame, 0, imageDst.length);
                            System.arraycopy(temperatureSrc, 0, firstTemp, 0, temperatureSrc.length);
                        }
                    }
                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isOpenAmplify && SupHelp.getInstance().an4K != null){
                    OpencvTools.supImage(imageDst,
                            (rotateInt == 270 || rotateInt == 90) ? imageHeight : imageWidth ,
                            (rotateInt == 270 || rotateInt == 90) ? imageWidth  : imageHeight ,
                            amplifyRotateArray);
                }
//                    Log.e("image总processing耗时：", String.valueOf(System.currentTimeMillis() - startImageTime));
            }

            /**
             * Executes synchronized operation with thermal imaging domain optimization.
             *
             */
            synchronized (syncimage.viewLock) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (!syncimage.valid) {
                    try {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isOpenAmplify) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (amplifyRotateArray!=null){
                                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(amplifyRotateArray));
                            }
                        } else {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (imageDst != null) {
                                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(imageDst));
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    syncimage.valid = true;
                    syncimage.viewLock.notify();
                }
            }
            try {
                SystemClock.sleep(20);
            } catch (Exception e) {
                XLog.e("Image Threadrefreshexception: " + e.getMessage());
            }
        }
        Log.i(TAG, "ImageThread exit");
    }

    public Bitmap getBaseBitmap(int rotateInt){
        Bitmap baseBitmap = null;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rotateInt == 0 || rotateInt == 180) {
            baseBitmap = Bitmap.createBitmap(256, 192, Bitmap.Config.ARGB_8888);
        }else {
            baseBitmap = Bitmap.createBitmap(192, 256, Bitmap.Config.ARGB_8888);
        }
        baseBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(imageDst));
        return baseBitmap;
    }

    private ColorRGB getColorRGBByMap(LinkedHashMap<Integer, ColorRGB> map, Integer key) {
        return map.get(key);
    }

    public void setColorList(@Nullable int[] colorList, @Nullable float[] places, boolean isUseGray,
                             float customMaxTemp, float customMinTemp) {
        irImageHelp.setColorList(colorList, places, isUseGray,customMaxTemp,customMinTemp);
    }
}