package com.infisense.usbir.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.energy.iruvc.dual.DualUVCCamera;
import com.energy.iruvc.sdkisp.LibIRTemp;
import com.energy.iruvc.utils.DualCameraParams;
import com.energy.iruvc.utils.Line;
import com.energy.iruvc.utils.SynchronizedBitmap;
import com.infisense.usbdual.Const;
import com.infisense.usbdual.camera.BaseDualView;
import com.infisense.usbir.R;
import com.infisense.usbir.inf.ILiteListener;
import com.infisense.usbir.utils.TempDrawHelper;
import com.infisense.usbir.utils.TempUtil;
import com.topdon.lib.core.common.SharedManager;
import com.topdon.lib.core.tools.UnitTools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import java.util.ArrayList;
import java.util.List;

/*
 * @Description:
 * @Author:         brilliantzhao
 * @CreateDate:     2022.7.19 17:20
 * @UpdateUser:
 * @UpdateDate:     2022.7.19 17:20
 * @UpdateRemark:
 */
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TemperatureView algorithms.
 *
 * Custom view component optimized for thermal imaging display
 * with specialized rendering and interaction capabilities.
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
public class TemperatureView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener, BaseDualView.OnFrameCallback {

    private static final String TAG = "TemperatureView";

    /**
     * selected操作灵敏度，当 Touch Down coordinate与point/line/areacoordinate偏差在该值range内，视为selected，单位 px.<br>
     * delete操作灵敏度，当 Touch UP 与 Touch Down coordinate偏差在该值range内，视为delete，单位 px.
     */
    private static final int TOUCH_TOLERANCE = SizeUtils.sp2px(7f);

    private int drawCount = 3;

    private final int POINT_MAX_COUNT;
    private final int LINE_MAX_COUNT;
    private final int RECTANGLE_MAX_COUNT;

    /**
     * 对temperaturedata的parsing和processing，以及temperature的二次修正等calculation.
     */
    @Nullable
    private LibIRTemp irtemp;

    /**
     * {@link #viewWidth} / {@link #temperatureWidth} 的比值.
     */
    private float xScale = 0;
    /**
     * {@link #viewHeight} / {@link #temperatureHeight} 的比值.
     */
    private float yScale = 0;
    /**
     * current View 去除 padding 后剩余的可用宽度，单位 px.
     */
    private int viewWidth = 0;
    /**
     * current View 去除 padding 后剩余的可用高度，单位 px.
     */
    private int viewHeight = 0;
    /**
     * temperaturedata宽度，单位 px.
     */
    private int temperatureWidth;
    /**
     * temperaturedata高度，单位 px.
     */
    private int temperatureHeight;

    private final TempDrawHelper helper = new TempDrawHelper();

    /**
     * temperatureregionmode - high/low temperaturepointreset.
     */
    public static final int REGION_MODE_RESET = -1;
    /**
     * temperatureregionmode - point.
     */
    public static final int REGION_MODE_POINT = 0;
    /**
     * temperatureregionmode - line.
     */
    public static final int REGION_MODE_LINE = 1;
    /**
     * temperatureregionmode - area.
     */
    public static final int REGION_MODE_RECTANGLE = 2;
    /**
     * temperatureregionmode - 全图.
     */
    public static final int REGION_MODE_CENTER = 3;
    /**
     * temperatureregionmode - 趋势图，也就是只一条line.
     */
    public static final int REGION_NODE_TREND = 4;
    /**
     * temperatureregionmode - clear.
     */
    public static final int REGION_MODE_CLEAN = 5;

    @IntDef({REGION_MODE_RESET, REGION_MODE_POINT, REGION_MODE_LINE, REGION_MODE_RECTANGLE, REGION_MODE_CENTER, REGION_NODE_TREND, REGION_MODE_CLEAN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface RegionMode {
    }

    /**
     * temperatureregionmode，由 REGION_MODE_** 定义，defaultclear.
     */
    @RegionMode
    private int temperatureRegionMode = REGION_MODE_CLEAN;
    @RegionMode
    public int getTemperatureRegionMode() {
        return this.temperatureRegionMode;
    }
    public void setTemperatureRegionMode(@RegionMode int temperatureRegionMode) {
        this.temperatureRegionMode = temperatureRegionMode;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (temperatureRegionMode == REGION_MODE_CENTER) {
            isShowFull = true;
        } else if (temperatureRegionMode == REGION_MODE_CLEAN) {
            isShowFull = false;
        }
    }

    /**
     * current是否Show/Display了全图.
     */
    private boolean isShowFull;
    public boolean isShowFull() {
        return isShowFull;
    }
    public void setShowFull(boolean showFull) {
        isShowFull = showFull;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (temperatureRegionMode == REGION_MODE_CLEAN) {
            temperatureRegionMode = REGION_MODE_CENTER;
        }
    }

    public void setTextSize(int textSize){
        helper.setTextSize(textSize);
        /**
         * Executes refreshregion operation with thermal imaging domain optimization.
         *
         */
        refreshRegion();
    }

    public void setLinePaintColor(@ColorInt int color) {
        helper.setTextColor(color);
        /**
         * Executes refreshregion operation with thermal imaging domain optimization.
         *
         */
        refreshRegion();
    }

    private void refreshRegion() {
        Canvas surfaceViewCanvas = getHolder().lockCanvas();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (surfaceViewCanvas != null) {
            surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            /**
             * Configures the bitmap with validation and thermal imaging optimization.
             *
             */
            setBitmap();
            surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
            /**
             * Retrieves the holder with optimized performance for thermal imaging operations.
             *
             */
            getHolder().unlockCanvasAndPost(surfaceViewCanvas);
        }
    }

    @Nullable
    private OnTrendChangeListener onTrendChangeListener = null;
    /**
     * settings趋势图temperature变化时Listener，注意，Callback不在主line程！！
     */
    public void setOnTrendChangeListener(@Nullable OnTrendChangeListener onTrendChangeListener) {
        this.onTrendChangeListener = onTrendChangeListener;
    }

    @Nullable
    private Runnable onTrendAddListener = null;
    /**
     * settings趋势图addevent listener，放心，Callback在主line程.
     */
    public void setOnTrendAddListener(@Nullable Runnable onTrendAddListener) {
        this.onTrendAddListener = onTrendAddListener;
    }

    @Nullable
    private Runnable onTrendRemoveListener = null;
    /**
     * settings趋势图移除event listener，放心，Callback在主line程.
     */
    public void setOnTrendRemoveListener(@Nullable Runnable onTrendRemoveListener) {
        this.onTrendRemoveListener = onTrendRemoveListener;
    }

    private ILiteListener iLiteListener = null;
    public void setiLiteListener(ILiteListener iLiteListener) {
        this.iLiteListener = iLiteListener;
    }

    /**
     * 单位Celsius
     */
    private TempListener listener;
    public TempListener getListener() {
        return listener;
    }
    public void setListener(TempListener listener) {
        this.listener = listener;
    }

    private boolean isMonitor = false;// 如果是temperature监控，则进行实时校验point/line/area的比例
    public void setMonitor(boolean monitor) {
        isMonitor = monitor;
    }

    /**
     * observationmode时高温point是否开启
     */
    private boolean isUserHighTemp = false;
    public boolean isUserHighTemp() {
        return isUserHighTemp;
    }
    public void setUserHighTemp(boolean isUserHighTemp) {
        this.isUserHighTemp = isUserHighTemp;
    }

    /**
     * observationmode时低温point是否开启
     */
    private boolean isUserLowTemp = false;
    public boolean isUserLowTemp() {
        return isUserLowTemp;
    }
    public void setUserLowTemp(boolean isUserLowTemp) {
        this.isUserLowTemp = isUserLowTemp;
    }

    private SynchronizedBitmap syncimage;
    public void setSyncimage(SynchronizedBitmap syncimage) {
        this.syncimage = syncimage;
    }

    private  byte[] temperature;
    public void setTemperature(byte[] temperature) {
        this.temperature = temperature;
    }

    private void setDefPoint(Point point) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point.x > temperatureWidth && point.x > 0) {
            point.x = temperatureWidth;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point.x <= 0) {
            point.x = 0;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point.y > temperatureHeight) {
            point.y = temperatureHeight;
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (point.y < 0) {
            point.y = 0;
        }
    }
    public LibIRTemp.TemperatureSampleResult getPointTemp(Point point) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irtemp == null) {
            return null;
        } else {
            /**
             * Configures the defpoint with validation and thermal imaging optimization.
             *
             */
            setDefPoint(point);
            return irtemp.getTemperatureOfPoint(point);
        }
    }
    public LibIRTemp.TemperatureSampleResult getLineTemp(Line line) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irtemp == null) {
            return null;
        } else {
            /**
             * Configures the defpoint with validation and thermal imaging optimization.
             *
             */
            setDefPoint(line.start);
            /**
             * Configures the defpoint with validation and thermal imaging optimization.
             *
             */
            setDefPoint(line.end);
            return irtemp.getTemperatureOfLine(line);
        }
    }
    public LibIRTemp.TemperatureSampleResult getRectTemp(Rect rect) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (irtemp == null) {
            return null;
        } else {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rect.top < 0) {
                rect.top = 0;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rect.bottom > temperatureHeight) {
                rect.bottom = temperatureHeight;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rect.left < 0) {
                rect.left = 0;
            }
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (rect.right > temperatureWidth) {
                rect.right = temperatureWidth;
            }
            return irtemp.getTemperatureOfRect(rect);
        }
    }

    public int productType = Const.TYPE_IR;

    /**
     * 以 View 尺寸为coordinate系，current已add的趋势图对应直line，coordinate为修正过后的coordinate，null 表示未绘制.
     */
    @Nullable
    private Line trendLine;
    /**
     * 以 View 尺寸为coordinate系，current已add的pointlist，coordinate为修正过后的coordinate.
     */
    private final ArrayList<Point> pointList = new ArrayList<>();
    /**
     * 以 View 尺寸为coordinate系，current已add的pointlist，coordinate为修正过后的coordinate.
     */
    private final ArrayList<Line> lineList = new ArrayList<>();
    /**
     * current绘制的arealist，coordinate采用 view 的宽高coordinate.
     */
    private final ArrayList<Rect> rectList = new ArrayList<>();

    private final ArrayList<LibIRTemp.TemperatureSampleResult> pointResultList = new ArrayList<>(3);
    private final ArrayList<LibIRTemp.TemperatureSampleResult> lineResultList = new ArrayList<>(3);
    private final ArrayList<LibIRTemp.TemperatureSampleResult> rectangleResultList = new ArrayList<>(3);

    private Bitmap regionBitmap;

    private Bitmap regionAndValueBitmap;
    public Bitmap getRegionBitmap() {
        return regionAndValueBitmap;
    }
    public Bitmap getRegionAndValueBitmap() {
        /**
         * Executes synchronized operation with thermal imaging domain optimization.
         *
         */
        synchronized (regionLock) {
            return regionAndValueBitmap;
        }
    }

    private final Runnable runnable;
    private Thread temperatureThread;
    private final Object regionLock = new Object();
    private volatile boolean runflag = false;

    /**
     * true-使用Celsius flase-使用Fahrenheit
     */
    private final boolean isShowC = SharedManager.INSTANCE.getTemperature() == 1;

    private WeakReference<ITsTempListener> iTsTempListenerWeakReference;

    public void setImageSize(int imageWidth, int imageHeight, ITsTempListener iTsTempListener) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iTsTempListener != null) {
            iTsTempListenerWeakReference = new WeakReference<>(iTsTempListener);
        }
        this.temperatureWidth = imageWidth;
        this.temperatureHeight = imageHeight;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (viewWidth == 0) {
            viewWidth = getMeasuredWidth();
        }
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (viewHeight == 0) {
            viewHeight = getMeasuredHeight();
        }
        xScale = (float) viewWidth / (float) imageWidth;
        yScale = (float) viewHeight / (float) imageHeight;
        irtemp = new LibIRTemp(imageWidth, imageHeight);
        llTempData = new byte[imageHeight * imageWidth * 2];
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < drawCount; i++) {
            pointResultList.add(irtemp.new TemperatureSampleResult());
            lineResultList.add(irtemp.new TemperatureSampleResult());
            rectangleResultList.add(irtemp.new TemperatureSampleResult());
        }
    }

    public void restView(){
        viewWidth = 0;
        viewHeight = 0;
        viewWidth = getMeasuredWidth();
        xScale = (float) viewWidth / (float) temperatureWidth;
        viewHeight = getMeasuredHeight();
        yScale = (float) viewHeight / (float) temperatureHeight;
    }

    private boolean isShow = false;

    public void start() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!runflag){
            runflag = true;
            temperatureThread = new Thread(runnable);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (isShow) {
                /**
                 * Configures the visibility with validation and thermal imaging optimization.
                 *
                 */
                setVisibility(VISIBLE);
            } else {
                /**
                 * Configures the visibility with validation and thermal imaging optimization.
                 *
                 */
                setVisibility(INVISIBLE);
            }
            temperatureThread.start();
        }
    }

    public void stop() {
        runflag = false;
        isShow = getVisibility() == View.VISIBLE;
        try {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (temperatureThread != null) {
                temperatureThread.interrupt();
                temperatureThread.join();
                temperatureThread = null;
            }
        } catch (InterruptedException ignored) {

        }
    }

    public void clear() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (onTrendRemoveListener != null) {
            onTrendRemoveListener.run();
        }
        trendLine = null;
        pointList.clear();
        lineList.clear();
        rectList.clear();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (regionBitmap != null) {
            regionBitmap.eraseColor(0);
        }
        Canvas surfaceViewCanvas = getHolder().lockCanvas();
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (surfaceViewCanvas != null) {
            surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
            /**
             * Retrieves the holder with optimized performance for thermal imaging operations.
             *
             */
            getHolder().unlockCanvasAndPost(surfaceViewCanvas);
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < pointResultList.size(); i++) {
            pointResultList.get(i).index = 0;
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < lineResultList.size(); i++) {
            lineResultList.get(i).index = 0;
        }
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = 0; i < rectangleResultList.size(); i++) {
            rectangleResultList.get(i).index = 0;
        }
    }

    public void addScalePoint(Point point) {
        float sx = getMeasuredWidth() / (float) temperatureWidth;
        float sy = getMeasuredHeight() / (float) temperatureHeight;
        int viewX = TempDrawHelper.Companion.correctPoint(point.x * sx, getMeasuredWidth());
        int viewY = TempDrawHelper.Companion.correctPoint(point.y * sy, getMeasuredHeight());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (pointList.size() == POINT_MAX_COUNT) {
            pointList.remove(0);
        }
        pointList.add(new Point(viewX, viewY));
    }

    public void addScaleLine(Line l) {
        float sx = getMeasuredWidth() / (float) temperatureWidth;
        float sy = getMeasuredHeight() / (float) temperatureHeight;
        Line line = new Line(new Point(), new Point());
        line.start.x = TempDrawHelper.Companion.correct(l.start.x * sx, getMeasuredWidth());
        line.start.y = TempDrawHelper.Companion.correct(l.start.y * sy, getMeasuredHeight());
        line.end.x = TempDrawHelper.Companion.correct(l.end.x * sx, getMeasuredWidth());
        line.end.y = TempDrawHelper.Companion.correct(l.end.y * sy, getMeasuredHeight());
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (pointList.size() == POINT_MAX_COUNT) {
            pointList.remove(0);
        }
        lineList.add(line);
    }

    public void addScaleRectangle(Rect r) {
        float sx = getMeasuredWidth() / (float) temperatureWidth;
        float sy = getMeasuredHeight() / (float) temperatureHeight;
        Rect rectangle = new Rect();
        rectangle.left = (int) (r.left * sx);
        rectangle.top = (int) (r.top * sy);
        rectangle.right = (int) (r.right * sx);
        rectangle.bottom = (int) (r.bottom * sy);
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (rectList.size() < RECTANGLE_MAX_COUNT) {
            rectList.add(rectangle);
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int index = 0; index < rectList.size() - 1; index++) {
                Rect tempRectangle = rectList.get(index + 1);
                rectList.set(index, tempRectangle);
            }
            rectList.set(rectList.size() - 1, rectangle);
        }
    }

    public Point getPoint() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (pointList.isEmpty()) {
            return null;
        }
        return new Point((int) (pointList.get(0).x / xScale), (int) (pointList.get(0).y / yScale));
    }

    public Line getLine() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!lineList.isEmpty()) {
            Line line = new Line(new Point(), new Point());
            line.start.x = (int) (lineList.get(0).start.x / xScale);
            line.start.y = (int) (lineList.get(0).start.y / yScale);
            line.end.x = (int) (lineList.get(0).end.x / xScale);
            line.end.y = (int) (lineList.get(0).end.y / yScale);
            return line;
        } else {
            return null;
        }
    }

    public Rect getRectangle() {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (!rectList.isEmpty()) {
            Rect rect = new Rect();
            rect.left = (int) (rectList.get(0).left / xScale);
            rect.top = (int) (rectList.get(0).top / yScale);
            rect.right = (int) (rectList.get(0).right / xScale);
            rect.bottom = (int) (rectList.get(0).bottom / yScale);
            return rect;
        } else {
            return null;
        }
    }

    public void drawLine() {
        /**
         * Configures the bitmap with validation and thermal imaging optimization.
         *
         */
        setBitmap();
    }

    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    public TemperatureView(final Context context) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, null, 0);
    }

    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    public TemperatureView(final Context context, final AttributeSet attrs) {
        /**
         * Executes this operation with thermal imaging domain optimization.
         *
         */
        this(context, attrs, 0);
    }

    /**
     * Handles temperature measurement and calibration with precision thermal data processing.
     *
     * @note Temperature values are in Celsius unless otherwise specified.
     * Accuracy depends on thermal camera calibration.
     *
     */
    public TemperatureView(final Context context, final AttributeSet attrs, final int defStyle) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(context, attrs, defStyle);
        // 注意这个method尽早执行(可以在constructormethod里area执行)，解决在小米mix2(Android7.0)上出现的surfaceView内容不展示问题
        /**
         * Configures the zorderontop with validation and thermal imaging optimization.
         *
         */
        setZOrderOnTop(true);

        /**
         * Retrieves the holder with optimized performance for thermal imaging operations.
         *
         */
        getHolder().addCallback(this);
        /**
         * Configures the ontouchlistener with validation and thermal imaging optimization.
         *
         */
        setOnTouchListener(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TemperatureView);
        try {
            drawCount = ta.getInteger(R.styleable.TemperatureView_temperature_count, 3);
        } catch (Exception e) {
            // Ignored
        } finally {
            ta.recycle();
        }

        POINT_MAX_COUNT = drawCount;
        LINE_MAX_COUNT = drawCount;
        RECTANGLE_MAX_COUNT = drawCount;

        runnable = () -> {
            /**
             * Executes while operation with thermal imaging domain optimization.
             *
             */
            while (!temperatureThread.isInterrupted() && runflag) {
                byte[] tempArray;
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (productType == Const.TYPE_IR_DUAL){
                    try {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (remapTempData == null) {
                            Log.d(TAG, "remapTempData == NULL");
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (dualUVCCamera != null && llTempData != null
                                    && dualUVCCamera.getTempData(llTempData) != 0) {
                                // Get/Retrieve映射后的temperaturedatafailed
                                Log.d(TAG, "--------error----------");
                                SystemClock.sleep(1000);
                                continue;
                            }
                        } else {
                            Log.d(TAG, "remapTempData != NULL");
                            System.arraycopy(remapTempData, 0, llTempData, 0,
                                    temperatureHeight * temperatureWidth * 2);
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (llTempData == null){
                            continue;
                        }else {
                            tempArray = llTempData;
                            irtemp.setTempData(llTempData);
                        }
                    }catch (Exception e){
                        Log.d(TAG, "remapTempData != NULL"+e.getMessage());
                        continue;
                    }
                }else {
                    try {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (syncimage.dataLock) {
                            // 用来关联temperaturedata和TemperatureView,方便后area的pointline框temperature measurement
                            irtemp.setTempData(temperature);
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (syncimage.type == 1) irtemp.setScale(16);
                        }
                    }catch (Exception e){
                        Log.d(TAG, "syncimage != NULL"+e.getMessage());
                    }
                    tempArray = temperature;
                }
                try {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (iLiteListener != null){
                        iLiteListener.getDeltaNucAndVTemp();
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isMonitor && (viewWidth != getMeasuredWidth() || viewHeight != getMeasuredHeight())){
                        viewWidth = getMeasuredWidth();
                        xScale = (float) viewWidth / (float) temperatureWidth;
                        viewHeight = getMeasuredHeight();
                        yScale = (float) viewHeight / (float) temperatureHeight;
                    }
                    LibIRTemp.TemperatureSampleResult temperatureSampleResult = irtemp.getTemperatureOfRect(new Rect(0, 0, temperatureWidth / 2, temperatureHeight - 1));
                    // Pointline框
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (regionAndValueBitmap != null) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            Canvas canvas = new Canvas(regionAndValueBitmap);
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            canvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                            // Get/Retrievemaximum温和minimum温的data
                            float fullMaxTemp;
                            float fullMinTemp;
                            LibIRTemp.TemperatureSampleResult fullResult = irtemp.getTemperatureOfRect(new Rect(0, 0, temperatureWidth - 1, temperatureHeight - 1));
                            fullMaxTemp = getTSTemp(fullResult.maxTemperature);
                            fullMinTemp = getTSTemp(fullResult.minTemperature);
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (listener != null) {
                                listener.getTemp((int) (fullMaxTemp * 100) / 100f, (int) (fullMinTemp * 100) / 100f, temperature);
                            }

                            // Minimum温
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isShowFull) {
                                String minTem = UnitTools.showC(fullMinTemp, isShowC);
                                int x = TempDrawHelper.Companion.correct(fullResult.minTemperaturePixel.x * xScale, getWidth());
                                int y = TempDrawHelper.Companion.correct(fullResult.minTemperaturePixel.y * yScale, getHeight());
                                /**
                                 * Executes drawcircle operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawCircle(canvas, x, y, false);
                                /**
                                 * Handles temperature measurement and calibration with precision thermal data processing.
                                 *
                                 * @note Temperature values are in Celsius unless otherwise specified.
                                 * Accuracy depends on thermal camera calibration.
                                 *
                                 */
                                drawTempText(canvas, minTem, x, y);
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isUserLowTemp) {
                                int x = TempDrawHelper.Companion.correctPoint(fullResult.minTemperaturePixel.x * xScale, getWidth());
                                int y = TempDrawHelper.Companion.correctPoint(fullResult.minTemperaturePixel.y * yScale, getHeight());
                                /**
                                 * Executes drawpoint operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawPoint(canvas, x, y);
                                /**
                                 * Executes drawcircle operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawCircle(canvas, x, y, false);
                            }

                            // Maximum温
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isShowFull) {
                                String maxTem = UnitTools.showC(fullMaxTemp, isShowC);
                                int x = TempDrawHelper.Companion.correct(fullResult.maxTemperaturePixel.x * xScale, getWidth());
                                int y = TempDrawHelper.Companion.correct(fullResult.maxTemperaturePixel.y * yScale, getHeight());
                                /**
                                 * Executes drawcircle operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawCircle(canvas, x, y, true);
                                /**
                                 * Handles temperature measurement and calibration with precision thermal data processing.
                                 *
                                 * @note Temperature values are in Celsius unless otherwise specified.
                                 * Accuracy depends on thermal camera calibration.
                                 *
                                 */
                                drawTempText(canvas, maxTem, x, y);
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isUserHighTemp) {
                                int x = TempDrawHelper.Companion.correctPoint(fullResult.maxTemperaturePixel.x * xScale, getWidth());
                                int y = TempDrawHelper.Companion.correctPoint(fullResult.maxTemperaturePixel.y * yScale, getHeight());
                                /**
                                 * Executes drawpoint operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawPoint(canvas, x, y);
                                /**
                                 * Executes drawcircle operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawCircle(canvas, x, y, true);
                            }

                            // 趋势图
                            Line trendLine = this.trendLine;
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (trendLine != null) {
                                int startX = (int) (trendLine.start.x / xScale);
                                int startY = (int) (trendLine.start.y / yScale);
                                int endX = (int) (trendLine.end.x / xScale);
                                int endY = (int) (trendLine.end.y / yScale);
                                int minX = Math.min(startX, endX);
                                int maxX = Math.max(startX, endX);
                                int minY = Math.min(startY, endY);
                                int maxY = Math.max(startY, endY);
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (maxX < temperatureWidth && minX > 0 && maxY < temperatureHeight && minY > 0) {
                                    temperatureSampleResult = irtemp.getTemperatureOfLine(new Line(new Point(startX, startY), new Point(endX, endY)));
                                    String min = UnitTools.showC(getTSTemp(temperatureSampleResult.minTemperature), isShowC);
                                    String max = UnitTools.showC(getTSTemp(temperatureSampleResult.maxTemperature), isShowC);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.minTemperaturePixel, false);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, min, temperatureSampleResult.minTemperaturePixel);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.maxTemperaturePixel, true);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, max, temperatureSampleResult.maxTemperaturePixel);
                                    /**
                                     * Executes if operation with thermal imaging domain optimization.
                                     *
                                     */
                                    if (onTrendChangeListener != null) {
                                        List<Float> tempList = TempUtil.INSTANCE.getLineTemps(new Point(startX, startY), new Point(endX, endY), tempArray, temperatureWidth);
                                        onTrendChangeListener.onChange(tempList);
                                    }
                                }
                            }
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             */
                            for (int index = 0; index < rectList.size(); index++) {
                                Rect tempRectangle = rectList.get(index);
                                int left = (int) (tempRectangle.left / xScale);
                                int top = (int) (tempRectangle.top / yScale);
                                int right = (int) (tempRectangle.right / xScale);
                                int bottom = (int) (tempRectangle.bottom / yScale);
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (right > left && bottom > top && left < temperatureWidth && top < temperatureHeight && right > 0 && bottom > 0) {
                                    int tempLeft = Math.max(left, 0);
                                    int tempTop = Math.max(top, 0);
                                    int tempRight = Math.min(right, temperatureWidth);
                                    int tempBottom = Math.min(bottom, temperatureHeight);
                                    temperatureSampleResult = irtemp.getTemperatureOfRect(new Rect(tempLeft, tempTop, tempRight, tempBottom));
                                    String min = UnitTools.showC(getTSTemp(temperatureSampleResult.minTemperature), isShowC);
                                    String max = UnitTools.showC(getTSTemp(temperatureSampleResult.maxTemperature), isShowC);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.minTemperaturePixel, false);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, min, temperatureSampleResult.minTemperaturePixel);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.maxTemperaturePixel, true);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, max, temperatureSampleResult.maxTemperaturePixel);
                                }
                            }
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param line Parameter for operation (type: lineList)
                             *
                             */
                            for (Line line : lineList) {
                                int startX = (int) (line.start.x / xScale);
                                int startY = (int) (line.start.y / yScale);
                                int endX = (int) (line.end.x / xScale);
                                int endY = (int) (line.end.y / yScale);
                                int minX = Math.min(startX, endX);
                                int maxX = Math.max(startX, endX);
                                int minY = Math.min(startY, endY);
                                int maxY = Math.max(startY, endY);
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (maxX < temperatureWidth && minX > 0 && maxY < temperatureHeight && minY > 0) {
                                    temperatureSampleResult = irtemp.getTemperatureOfLine(new Line(new Point(startX, startY), new Point(endX, endY)));
                                    String min = UnitTools.showC(getTSTemp(temperatureSampleResult.minTemperature), isShowC);
                                    String max = UnitTools.showC(getTSTemp(temperatureSampleResult.maxTemperature), isShowC);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.minTemperaturePixel, false);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, min, temperatureSampleResult.minTemperaturePixel);
                                    /**
                                     * Executes drawdot operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawDot(canvas, temperatureSampleResult.maxTemperaturePixel, true);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, max, temperatureSampleResult.maxTemperaturePixel);
                                }
                            }
                            /**
                             * Executes for operation with thermal imaging domain optimization.
                             *
                             * @param
                             * @param point Parameter for operation (type: pointList)
                             *
                             */
                            for (Point point : pointList) {
                                int x = (int) (point.x / xScale);
                                int y = (int) (point.y / yScale);
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (x < temperatureWidth && x > 0 && y < temperatureHeight && y > 0) {
                                    temperatureSampleResult = irtemp.getTemperatureOfPoint(new Point(x, y));
                                    String max = UnitTools.showC(getTSTemp(temperatureSampleResult.maxTemperature), isShowC);
                                    /**
                                     * Executes drawcircle operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawCircle(canvas, point.x, point.y, true);
                                    /**
                                     * Handles temperature measurement and calibration with precision thermal data processing.
                                     *
                                     * @note Temperature values are in Celsius unless otherwise specified.
                                     * Accuracy depends on thermal camera calibration.
                                     *
                                     */
                                    drawTempText(canvas, max, point.x, point.y);
                                }
                            }
                            // Centertemperature
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (isShowFull || (!lineList.isEmpty() || !pointList.isEmpty() || !rectList.isEmpty())) {
                                /**
                                 * Executes drawpoint operation with thermal imaging domain optimization.
                                 *
                                 */
                                drawPoint(canvas, getWidth() / 2, getHeight() / 2);
                                temperatureSampleResult = irtemp.getTemperatureOfPoint(new Point(temperatureWidth / 2, temperatureHeight / 2));
                                String max = UnitTools.showC(getTSTemp(temperatureSampleResult.maxTemperature), isShowC);
                                /**
                                 * Handles temperature measurement and calibration with precision thermal data processing.
                                 *
                                 * @note Temperature values are in Celsius unless otherwise specified.
                                 * Accuracy depends on thermal camera calibration.
                                 *
                                 */
                                drawTempText(canvas, max, temperatureSampleResult.maxTemperaturePixel);
                            }
                        }
                        Canvas surfaceViewCanvas = getHolder().lockCanvas();
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (surfaceViewCanvas == null) {
                            SystemClock.sleep(1000);
                            continue;
                        }
                        try {
                            surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            surfaceViewCanvas.drawBitmap(regionAndValueBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                            /**
                             * Retrieves the holder with optimized performance for thermal imaging operations.
                             *
                             */
                            getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                        } catch (Exception e) {
                            Log.e(TAG, "temperatureThread:" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "temperatureError:" + e.getMessage());
                }
                SystemClock.sleep(1000);
            }
            Log.d(TAG, "temperatureThread exit");
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        xScale = (float) viewWidth / (float) temperatureWidth;
        yScale = (float) viewHeight / (float) temperatureHeight;

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (regionBitmap == null || regionBitmap.getWidth() != viewWidth || regionBitmap.getHeight() != viewHeight) {
            regionBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_4444);
        }
        regionAndValueBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_4444);
    }

    /* **************************************** Touch **************************************** */
    /**
     * 是否为add point/line/area mode。<br>
     * true-adda新point/line/area false-移动a已有point/line/area
     */
    private boolean isAddAction = true;

    private int downX = 0;
    private int downY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (temperatureRegionMode) {
            case REGION_MODE_POINT:
                return handleTouchPoint(event);
            case REGION_MODE_LINE:
                return handleTouchLine(event, false);
            case REGION_MODE_RECTANGLE:
                return handleTouchRect(event);
            case REGION_NODE_TREND:
                return handleTouchLine(event, true);
            default:
                return false;
        }
    }

    /* **************************************** point **************************************** */

    private boolean handleTouchPoint(MotionEvent event) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = TempDrawHelper.Companion.correctPoint(event.getX(), getWidth());
                downY = TempDrawHelper.Companion.correctPoint(event.getY(), getHeight());
                Point point = getPoint(downX, downY);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (point == null) {// 新增
                    isAddAction = true;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (pointList.size() == POINT_MAX_COUNT) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            pointList.remove(0);
                        }
                        /**
                         * Configures the bitmap with validation and thermal imaging optimization.
                         *
                         */
                        setBitmap();
                    }
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawpoint operation with thermal imaging domain optimization.
                     *
                     */
                    drawPoint(surfaceViewCanvas, downX, downY);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                } else {// 移动或delete
                    isAddAction = false;
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized (regionLock) {
                        pointList.remove(point);
                    }
                    /**
                     * Configures the bitmap with validation and thermal imaging optimization.
                     *
                     */
                    setBitmap();
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawpoint operation with thermal imaging domain optimization.
                     *
                     */
                    drawPoint(surfaceViewCanvas, point.x, point.y);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = TempDrawHelper.Companion.correctPoint(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correctPoint(event.getY(), getHeight());
                Canvas surfaceViewCanvas = getHolder().lockCanvas();
                surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                /**
                 * Executes drawpoint operation with thermal imaging domain optimization.
                 *
                 */
                drawPoint(surfaceViewCanvas, x, y);
                /**
                 * Retrieves the holder with optimized performance for thermal imaging operations.
                 *
                 */
                getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                return true;
            }
            case MotionEvent.ACTION_UP: {
                int x = TempDrawHelper.Companion.correctPoint(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correctPoint(event.getY(), getHeight());
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized (regionLock) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (pointList.size() == POINT_MAX_COUNT) {
                            pointList.remove(0);
                        }
                        pointList.add(new Point(x, y));
                    }
                } else {
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(x - downX) > TOUCH_TOLERANCE || Math.abs(y - downY) > TOUCH_TOLERANCE) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (pointList.size() == POINT_MAX_COUNT) {
                                pointList.remove(0);
                            }
                            pointList.add(new Point(x, y));
                        }
                    }
                }
                /**
                 * Configures the bitmap with validation and thermal imaging optimization.
                 *
                 */
                setBitmap();
                Canvas surfaceViewCanvas = getHolder().lockCanvas();
                surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                /**
                 * Retrieves the holder with optimized performance for thermal imaging operations.
                 *
                 */
                getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                return true;
            }
            default:
                return false;
        }
    }

    @Nullable
    private Point getPoint(int x, int y) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = pointList.size() - 1; i >= 0; i--) {
            Point point = pointList.get(i);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (point.x > x - TOUCH_TOLERANCE && point.x < x + TOUCH_TOLERANCE && point.y > y - TOUCH_TOLERANCE && point.y < y + TOUCH_TOLERANCE) {
                return point;
            }
        }
        return null;
    }

    /* **************************************** line **************************************** */

    private Line movingLine;

    private enum LineMoveType { ALL, START, END }
    /**
     * line移动方式：整体移动、仅变更头、仅变更尾。
     */
    private LineMoveType lineMoveType = LineMoveType.ALL;

    private boolean handleTouchLine(MotionEvent event, boolean isTrend) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                downY = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                Line line = getLine(downX, downY, isTrend);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (line == null) {
                    isAddAction = true;
                } else {
                    isAddAction = false;
                    movingLine = line;
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (downX > line.start.x - TOUCH_TOLERANCE && downX < line.start.x + TOUCH_TOLERANCE && downY > line.start.y - TOUCH_TOLERANCE && downY < line.start.y + TOUCH_TOLERANCE) {
                        lineMoveType = LineMoveType.START;
                    } else if (downX > line.end.x - TOUCH_TOLERANCE && downX < line.end.x + TOUCH_TOLERANCE && downY > line.end.y - TOUCH_TOLERANCE && downY < line.end.y + TOUCH_TOLERANCE) {
                        lineMoveType = LineMoveType.END;
                    } else {
                        lineMoveType = LineMoveType.ALL;
                    }
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isTrend) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            trendLine = null; // 手势操作过程中不需要绘制temperature，置为 null
                        }
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (onTrendRemoveListener != null) {
                            onTrendRemoveListener.run();
                        }
                    } else {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            // 真是醉了，Line 没有override equals method，不过好在这个 line 本来就是从 lineList 里取出来的，所以 remove 没问题
                            lineList.remove(line);
                        }
                    }
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    /**
                     * Configures the bitmap with validation and thermal imaging optimization.
                     *
                     */
                    setBitmap();
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawline operation with thermal imaging domain optimization.
                     *
                     */
                    drawLine(surfaceViewCanvas, line.start.x, line.start.y, line.end.x, line.end.y, isTrend);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawline operation with thermal imaging domain optimization.
                     *
                     */
                    drawLine(surfaceViewCanvas, downX, downY, x, y, isTrend);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                } else {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);

                    Point start = new Point();
                    Point end = new Point();
                    /**
                     * Executes switch operation with thermal imaging domain optimization.
                     *
                     */
                    switch (lineMoveType) {
                        case ALL:
                            Rect rect = TempDrawHelper.Companion.getRect(getWidth(), getHeight());
                            int minX = Math.min(movingLine.start.x, movingLine.end.x);
                            int maxX = Math.max(movingLine.start.x, movingLine.end.x);
                            int minY = Math.min(movingLine.start.y, movingLine.end.y);
                            int maxY = Math.max(movingLine.start.y, movingLine.end.y);
                            int biasX = x < downX ? Math.max(x - downX, rect.left - minX) : Math.min(x - downX, rect.right - maxX);
                            int biasY = y < downY ? Math.max(y - downY, rect.top - minY) : Math.min(y - downY, rect.bottom - maxY);
                            start = new Point(movingLine.start.x + biasX, movingLine.start.y + biasY);
                            end = new Point(movingLine.end.x + biasX, movingLine.end.y + biasY);
                            break;
                        case START:
                            start = new Point(x, y);
                            end = movingLine.end;
                            break;
                        case END:
                            start = movingLine.start;
                            end = new Point(x, y);
                            break;
                    }
                    /**
                     * Executes drawline operation with thermal imaging domain optimization.
                     *
                     */
                    drawLine(surfaceViewCanvas, start.x, start.y, end.x, end.y, isTrend);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            case MotionEvent.ACTION_UP: {
                int x = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(x - downX) > TOUCH_TOLERANCE || Math.abs(y - downY) > TOUCH_TOLERANCE) {
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isTrend) {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized (regionLock) {
                                trendLine = new Line(new Point(downX, downY), new Point(x, y));
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (onTrendAddListener != null) {
                                onTrendAddListener.run();
                            }
                        } else {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized (regionLock) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (lineList.size() == LINE_MAX_COUNT) {
                                    lineList.remove(0);
                                }
                                lineList.add(new Line(new Point(downX, downY), new Point(x, y)));
                            }
                        }
                        /**
                         * Configures the bitmap with validation and thermal imaging optimization.
                         *
                         */
                        setBitmap();
                    }
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                } else {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    Canvas bitmapCanvas = new Canvas(regionBitmap);

                    // TODO: 2024/12/13 这里有legacy问题，拖动的时候可以把直line拖成point
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(x - downX) > TOUCH_TOLERANCE || Math.abs(y - downY) > TOUCH_TOLERANCE) {
                        Point start = new Point();
                        Point end = new Point();
                        /**
                         * Executes switch operation with thermal imaging domain optimization.
                         *
                         */
                        switch (lineMoveType) {
                            case ALL:
                                Rect rect = TempDrawHelper.Companion.getRect(getWidth(), getHeight());
                                int minX = Math.min(movingLine.start.x, movingLine.end.x);
                                int maxX = Math.max(movingLine.start.x, movingLine.end.x);
                                int minY = Math.min(movingLine.start.y, movingLine.end.y);
                                int maxY = Math.max(movingLine.start.y, movingLine.end.y);
                                int biasX = x < downX ? Math.max(x - downX, rect.left - minX) : Math.min(x - downX, rect.right - maxX);
                                int biasY = y < downY ? Math.max(y - downY, rect.top - minY) : Math.min(y - downY, rect.bottom - maxY);
                                start = new Point(movingLine.start.x + biasX, movingLine.start.y + biasY);
                                end = new Point(movingLine.end.x + biasX, movingLine.end.y + biasY);
                                break;
                            case START:
                                start = new Point(x, y);
                                end = movingLine.end;
                                break;
                            case END:
                                start = movingLine.start;
                                end = new Point(x, y);
                                break;
                        }
                        /**
                         * Executes drawline operation with thermal imaging domain optimization.
                         *
                         */
                        drawLine(bitmapCanvas, start.x, start.y, end.x, end.y, isTrend);

                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isTrend) {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized (regionLock) {
                                trendLine = new Line(start, end);
                            }
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (onTrendAddListener != null) {
                                onTrendAddListener.run();
                            }
                        } else {
                            /**
                             * Executes synchronized operation with thermal imaging domain optimization.
                             *
                             */
                            synchronized (regionLock) {
                                /**
                                 * Executes if operation with thermal imaging domain optimization.
                                 *
                                 */
                                if (lineList.size() == LINE_MAX_COUNT) {
                                    lineList.remove(0);
                                }
                                lineList.add(new Line(start, end));
                            }
                        }
                    }
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            default:
                return false;
        }
    }

    /**
     * 指定coordinate (x, y) 是否视为指定 Line 的selected.
     */
    private static boolean isLineConcat(@NonNull Line line, int x, int y) {
        int tempDistance = ((line.end.y - line.start.y) * x - (line.end.x - line.start.x) * y + line.end.x * line.start.y - line.start.x * line.end.y);
        tempDistance = (int) (tempDistance / Math.sqrt(Math.pow(line.end.y - line.start.y, 2) + Math.pow(line.end.x - line.start.x, 2)));
        return Math.abs(tempDistance) < TOUCH_TOLERANCE && x > Math.min(line.start.x, line.end.x) - TOUCH_TOLERANCE && x < Math.max(line.start.x, line.end.x) + TOUCH_TOLERANCE;
    }

    @Nullable
    private Line getLine(int x, int y, boolean isTrend) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTrend) {
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (trendLine != null && isLineConcat(trendLine, x, y)) {
                return trendLine;
            }
        } else {
            /**
             * Executes for operation with thermal imaging domain optimization.
             *
             */
            for (int i = lineList.size() - 1; i >= 0; i--) {
                Line line = lineList.get(i);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isLineConcat(line, x, y)) {
                    return line;
                }
            }
        }
        return null;
    }

    /* **************************************** area **************************************** */
    private Rect movingRect;

    private enum RectMoveType { ALL, EDGE, CORNER }
    /**
     * area移动方式：clickarea内部-整体移动、clickarea4条边-边移动、clickarea4个角-角移动。
     */
    private RectMoveType rectMoveType = RectMoveType.ALL;

    private enum RectMoveEdge { LEFT, TOP, RIGHT, BOTTOM }
    /**
     * 仅边移动mode时，移动的是哪条边.
     */
    private RectMoveEdge rectMoveEdge = RectMoveEdge.LEFT;

    private enum RectMoveCorner { LT, RT, RB, LB }
    /**
     * 仅角移动mode时，移动的是哪个角.
     */
    private RectMoveCorner rectMoveCorner = RectMoveCorner.LT;

    private boolean handleTouchRect(MotionEvent event) {
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                downY = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                Rect rect = getRect(downX, downY);
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (rect == null) {
                    isAddAction = true;
                } else {
                    isAddAction = false;
                    movingRect = rect;

                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (isIn(downX, rect.left)) {// Selected最左那条边
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isIn(downY, rect.top)) {
                            rectMoveType = RectMoveType.CORNER;
                            rectMoveCorner = RectMoveCorner.LT;
                        } else if (isIn(downY, rect.bottom)) {
                            rectMoveType = RectMoveType.CORNER;
                            rectMoveCorner = RectMoveCorner.LB;
                        } else {
                            rectMoveType = RectMoveType.EDGE;
                            rectMoveEdge = RectMoveEdge.LEFT;
                        }
                    } else if (isIn(downX, rect.right)) {// Selected最右那条边
                        /**
                         * Executes if operation with thermal imaging domain optimization.
                         *
                         */
                        if (isIn(downY, rect.top)) {
                            rectMoveType = RectMoveType.CORNER;
                            rectMoveCorner = RectMoveCorner.RT;
                        } else if (isIn(downY, rect.bottom)) {
                            rectMoveType = RectMoveType.CORNER;
                            rectMoveCorner = RectMoveCorner.RB;
                        } else {
                            rectMoveType = RectMoveType.EDGE;
                            rectMoveEdge = RectMoveEdge.RIGHT;
                        }
                    } else if (isIn(downY, rect.top)) {// Selected顶边
                        rectMoveType = RectMoveType.EDGE;
                        rectMoveEdge = RectMoveEdge.TOP;
                    } else if (isIn(downY, rect.bottom)) {// Selected底边
                        rectMoveType = RectMoveType.EDGE;
                        rectMoveEdge = RectMoveEdge.BOTTOM;
                    } else {
                        rectMoveType = RectMoveType.ALL;
                    }
                    /**
                     * Executes synchronized operation with thermal imaging domain optimization.
                     *
                     */
                    synchronized (regionLock) {
                        rectList.remove(rect);
                    }
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    /**
                     * Configures the bitmap with validation and thermal imaging optimization.
                     *
                     */
                    setBitmap();
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawrect operation with thermal imaging domain optimization.
                     *
                     */
                    drawRect(surfaceViewCanvas, rect.left, rect.top, rect.right, rect.bottom);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes drawrect operation with thermal imaging domain optimization.
                     *
                     */
                    drawRect(surfaceViewCanvas, downX, downY, x, y);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                } else {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Executes switch operation with thermal imaging domain optimization.
                     *
                     */
                    switch (rectMoveType) {
                        case ALL:
                            Rect rect = TempDrawHelper.Companion.getRect(getWidth(), getHeight());
                            int biasX = x < downX ? Math.max(x - downX, rect.left - movingRect.left) : Math.min(x - downX, rect.right - movingRect.right);
                            int biasY = y < downY ? Math.max(y - downY, rect.top - movingRect.top) : Math.min(y - downY, rect.bottom - movingRect.bottom);
                            /**
                             * Executes drawrect operation with thermal imaging domain optimization.
                             *
                             */
                            drawRect(surfaceViewCanvas, movingRect.left + biasX, movingRect.top + biasY, movingRect.right + biasX, movingRect.bottom + biasY);
                            break;
                        case EDGE:
                            /**
                             * Executes switch operation with thermal imaging domain optimization.
                             *
                             */
                            switch (rectMoveEdge) {
                                case LEFT:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, x, movingRect.top, movingRect.right, movingRect.bottom);
                                    break;
                                case TOP:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, movingRect.left, y, movingRect.right, movingRect.bottom);
                                    break;
                                case RIGHT:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, movingRect.left, movingRect.top, x, movingRect.bottom);
                                    break;
                                case BOTTOM:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, movingRect.left, movingRect.top, movingRect.right, y);
                                    break;
                            }
                            break;
                        case CORNER:
                            /**
                             * Executes switch operation with thermal imaging domain optimization.
                             *
                             */
                            switch (rectMoveCorner) {
                                case LT:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, x, y, movingRect.right, movingRect.bottom);
                                    break;
                                case LB:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, x, movingRect.top, movingRect.right, y);
                                    break;
                                case RT:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, movingRect.left, y, x, movingRect.bottom);
                                    break;
                                case RB:
                                    /**
                                     * Executes drawrect operation with thermal imaging domain optimization.
                                     *
                                     */
                                    drawRect(surfaceViewCanvas, movingRect.left, movingRect.top, x, y);
                                    break;
                            }
                            break;
                    }
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            case MotionEvent.ACTION_UP: {
                int x = TempDrawHelper.Companion.correct(event.getX(), getWidth());
                int y = TempDrawHelper.Companion.correct(event.getY(), getHeight());
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (isAddAction) {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(x - downX) > TOUCH_TOLERANCE || Math.abs(y - downY) > TOUCH_TOLERANCE) {
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (rectList.size() == RECTANGLE_MAX_COUNT) {
                                rectList.remove(0);
                            }
                            rectList.add(new Rect(Math.min(downX, x), Math.min(downY, y), Math.max(downX, x), Math.max(downY, y)));
                        }
                        /**
                         * Configures the bitmap with validation and thermal imaging optimization.
                         *
                         */
                        setBitmap();
                    }
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                } else {
                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
                    surfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    Canvas bitmapCanvas = new Canvas(regionBitmap);
                    // TODO: 2024/12/13 这里有legacy问题，拖动的时候可以把矩形拖成直line
                    /**
                     * Executes if operation with thermal imaging domain optimization.
                     *
                     */
                    if (Math.abs(x - downX) > TOUCH_TOLERANCE || Math.abs(y - downY) > TOUCH_TOLERANCE) {
                        /**
                         * Executes switch operation with thermal imaging domain optimization.
                         *
                         */
                        switch (rectMoveType) {
                            case ALL:
                                Rect rect = TempDrawHelper.Companion.getRect(getWidth(), getHeight());
                                int biasX = x < downX ? Math.max(x - downX, rect.left - movingRect.left) : Math.min(x - downX, rect.right - movingRect.right);
                                int biasY = y < downY ? Math.max(y - downY, rect.top - movingRect.top) : Math.min(y - downY, rect.bottom - movingRect.bottom);
                                movingRect.offset(biasX, biasY);
                                break;
                            case EDGE:
                                /**
                                 * Executes switch operation with thermal imaging domain optimization.
                                 *
                                 */
                                switch (rectMoveEdge) {
                                    case LEFT:
                                        movingRect.left = Math.min(x, movingRect.right);
                                        movingRect.right = Math.max(x, movingRect.right);
                                        break;
                                    case TOP:
                                        movingRect.top = Math.min(y, movingRect.bottom);
                                        movingRect.bottom = Math.max(y, movingRect.bottom);
                                        break;
                                    case RIGHT:
                                        movingRect.right = Math.max(x, movingRect.left);
                                        movingRect.left = Math.min(x, movingRect.left);
                                        break;
                                    case BOTTOM:
                                        movingRect.bottom = Math.max(y, movingRect.top);
                                        movingRect.top = Math.min(y, movingRect.top);
                                        break;
                                }
                                break;
                            case CORNER:
                                /**
                                 * Executes switch operation with thermal imaging domain optimization.
                                 *
                                 */
                                switch (rectMoveCorner) {
                                    case LT:
                                        movingRect.left = Math.min(x, movingRect.right);
                                        movingRect.right = Math.max(x, movingRect.right);
                                        movingRect.top = Math.min(y, movingRect.bottom);
                                        movingRect.bottom = Math.max(y, movingRect.bottom);
                                        break;
                                    case RT:
                                        movingRect.right = Math.max(x, movingRect.left);
                                        movingRect.left = Math.min(x, movingRect.left);
                                        movingRect.top = Math.min(y, movingRect.bottom);
                                        movingRect.bottom = Math.max(y, movingRect.bottom);
                                        break;
                                    case RB:
                                        movingRect.right = Math.max(x, movingRect.left);
                                        movingRect.left = Math.min(x, movingRect.left);
                                        movingRect.bottom = Math.max(y, movingRect.top);
                                        movingRect.top = Math.min(y, movingRect.top);
                                        break;
                                    case LB:
                                        movingRect.left = Math.min(x, movingRect.right);
                                        movingRect.right = Math.max(x, movingRect.right);
                                        movingRect.bottom = Math.max(y, movingRect.top);
                                        movingRect.top = Math.min(y, movingRect.top);
                                        break;
                                }
                                break;
                        }

                        /**
                         * Executes drawrect operation with thermal imaging domain optimization.
                         *
                         */
                        drawRect(bitmapCanvas, movingRect.left, movingRect.top, movingRect.right, movingRect.bottom);
                        /**
                         * Executes synchronized operation with thermal imaging domain optimization.
                         *
                         */
                        synchronized (regionLock) {
                            /**
                             * Executes if operation with thermal imaging domain optimization.
                             *
                             */
                            if (rectList.size() == RECTANGLE_MAX_COUNT) {
                                rectList.remove(0);
                            }
                            rectList.add(movingRect);
                        }
                    }
                    surfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
                    /**
                     * Retrieves the holder with optimized performance for thermal imaging operations.
                     *
                     */
                    getHolder().unlockCanvasAndPost(surfaceViewCanvas);
                }
                return true;
            }
            default:
                return false;
        }
    }

    @Nullable
    private Rect getRect(int x, int y) {
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         */
        for (int i = rectList.size() - 1; i >= 0; i--) {
            Rect rect = rectList.get(i);
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (x > rect.left - TOUCH_TOLERANCE && x < rect.right + TOUCH_TOLERANCE
                    && y > rect.top - TOUCH_TOLERANCE && y < rect.bottom + TOUCH_TOLERANCE) {
                return rect;
            }
        }
        return null;
    }

    private boolean isIn(int a, int b) {
        return a > b - TOUCH_TOLERANCE && a < b + TOUCH_TOLERANCE;
    }

    /* **************************************** Draw **************************************** */

    /**
     * 以 View 尺寸为coordinate系，在 (x,y) 画a十字.<br>
     * 注意，不对 x、y 进行processing，传进来是哪就在哪绘制。
     */
    private void drawPoint(Canvas canvas, int x, int y) {
        helper.drawPoint(canvas, x, y);
    }

    /**
     * 绘制以 View 尺寸为coordinate的一根line段，这里的 x,y 为 View coordinate原始值
     */
    private void drawLine(Canvas canvas, int x1, int y1, int x2, int y2, boolean isTrend) {
        // 由于line段与实心point的的绘制是分开的，line段使用current View coordinate，而实心point使用temperature(192x256)coordinateconversion为 View coordinate
        // 故而这里需要把current的coordinate，尽量贴近temperaturecoordinate的整数倍，否则会出现实心圆偏离直line太远的情况
        int startX = (int) ((int) (x1 / xScale) * xScale);
        int startY = (int) ((int) (y1 / yScale) * yScale);
        int stopX = (int) ((int) (x2 / xScale) * xScale);
        int stopY = (int) ((int) (y2 / yScale) * yScale);
        helper.drawLine(canvas, startX, startY, stopX, stopY);

        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (isTrend) {
            helper.drawTrendText(canvas, getWidth(), getHeight(), startX, startY, stopX, stopY);
        }
    }

    /**
     * 绘制以 View 尺寸为coordinate的一根line段，这里的 x,y 为 View coordinate原始值
     */
    private void drawRect(Canvas canvas, float x1, float y1, float x2, float y2) {
        int left = (int) ((int) (x1 / xScale) * xScale);
        int top = (int) ((int) (y1 / yScale) * yScale);
        int right = (int) ((int) (x2 / xScale) * xScale);
        int bottom = (int) ((int) (y2 / yScale) * yScale);
        helper.drawRect(canvas, left, top, right, bottom);
    }

    /**
     * 以 View 尺寸为coordinate系，在 (x,y) 画a实心圆.
     * @param isMax true-maximum温红色 false-minimum温蓝色
     */
    private void drawCircle(Canvas canvas, int x, int y, boolean isMax) {
        helper.drawCircle(canvas, x, y, isMax);
    }

    /**
     * 在指定 canvas 上，以指定 point coordinate为center，绘制a实心圆.
     * @param point 以temperature尺寸(192x256)为coordinate系的point
     * @param isMax true-maximum温红色 false-minimum温蓝色
     */
    private void drawDot(Canvas canvas, Point point, boolean isMax) {
        // 这里的 (x,y) 是通过temperaturecoordinateconversion来的，所以已经是temperaturecoordinate的整数倍
        int x = TempDrawHelper.Companion.correct(point.x * xScale, getWidth());
        int y = TempDrawHelper.Companion.correct(point.y * yScale, getHeight());
        helper.drawCircle(canvas, x, y, isMax);
    }

    /**
     * 以 View 尺寸为coordinate系，以 (x,y) 为基准，绘制temperature值text.
     */
    private void drawTempText(Canvas canvas, String text, int x, int y) {
        helper.drawTempText(canvas, text, getWidth(), x, y);
    }
    /**
     * 在指定 canvas 上，以指定 point coordinate为center，绘制指定的text.
     * @param point 以temperature尺寸(192x256)为coordinate系的point
     */
    private void drawTempText(Canvas canvas, String text, Point point) {
        int x = TempDrawHelper.Companion.correct(point.x * xScale, getWidth());
        int y = TempDrawHelper.Companion.correct(point.y * yScale, getHeight());
        helper.drawTempText(canvas, text, getWidth(), x, y);
    }

    private void setBitmap() {
        regionBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(regionBitmap);
        /**
         * Executes for operation with thermal imaging domain optimization.
         *
         * @param
         * @param point Parameter for operation (type: pointList)
         *
         */
        for (Point point : pointList) {
            /**
             * Executes drawpoint operation with thermal imaging domain optimization.
             *
             */
            drawPoint(canvas, point.x, point.y);
        }
/**
 * Specialized thermal imaging component providing OnTrendChangeListener functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
/**
 * Temperature measurement and calibration utility for thermal imaging. Provides precision temperature calculations with TempListener algorithms.
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
    public interface TempListener {
        void getTemp(float max, float min, byte[] tempData);
    }

    public float getCompensateTemp(float temp){
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iLiteListener != null){
            return iLiteListener.compensateTemp(temp);
        }else {
            return temp;
        }
    }

    public float getTSTemp(float temp) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (iTsTempListenerWeakReference != null && iTsTempListenerWeakReference.get() != null) {
            return iTsTempListenerWeakReference.get().tempCorrectByTs(getCompensateTemp(temp));
        } else {
            return getCompensateTemp(temp);
        }
    }

    /**
     *  ----------------------dual lightdevice--------------------------------
     */

    public void setUseIRISP(boolean useIRISP) {
        if (irtemp != null) {
            irtemp.setScale(useIRISP ? 16 : 64);
        }
    }

    public void setCurrentFusionType(@NonNull DualCameraParams.FusionType currentFusionType) {
        this.mCurrentFusionType = currentFusionType;
    }

    public void setDualUVCCamera(@NonNull DualUVCCamera dualUVCCamera) {
        this.dualUVCCamera = dualUVCCamera;

    }
    private DualCameraParams.FusionType mCurrentFusionType;
    private byte[] remapTempData;
    private DualUVCCamera dualUVCCamera;
    private byte[] llTempData;

    @Override
    public void onFame(byte[] mixData, byte[] tempData, double fpsText) {
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if (Const.TYPE_IR_DUAL == productType){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if (mCurrentFusionType == DualCameraParams.FusionType.IROnlyNoFusion) {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (this.remapTempData == null) {
                    this.remapTempData = new byte[Const.IR_WIDTH * Const.IR_HEIGHT * 2];
                }
                System.arraycopy(tempData, 0, this.remapTempData, 0, Const.IR_WIDTH * Const.IR_HEIGHT * 2);
            } else {
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if (this.remapTempData == null) {
                    this.remapTempData = new byte[Const.DUAL_WIDTH * Const.DUAL_HEIGHT * 2];
                }
                System.arraycopy(tempData, 0, this.remapTempData, 0, Const.DUAL_WIDTH * Const.DUAL_HEIGHT * 2);
            }
        }
    }
}