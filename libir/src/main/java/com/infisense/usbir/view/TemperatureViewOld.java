// Package com.infisense.usbir.view;
//
// Import android.content.Context;
// Import android.content.res.TypedArray;
// Import android.graphics.Bitmap;
// Import android.graphics.Canvas;
// Import android.graphics.Color;
// Import android.graphics.Paint;
// Import android.graphics.PixelFormat;
// Import android.graphics.Point;
// Import android.graphics.PorterDuff;
// Import android.graphics.Rect;
// Import android.os.SystemClock;
// Import android.util.AttributeSet;
// Import android.util.Log;
// Import android.view.MotionEvent;
// Import android.view.SurfaceHolder;
// Import android.view.SurfaceView;
// Import android.view.View;
//
// Import com.blankj.utilcode.util.SizeUtils;
// Import com.elvishew.xlog.XLog;
// Import com.infisense.iruvc.sdkisp.Libirtemp;
// Import com.infisense.iruvc.sdkisp.Libirtemp.TemperatureSampleResult;
// Import com.infisense.iruvc.utils.Line;
// Import com.infisense.iruvc.utils.SynchronizedBitmap;
// Import com.infisense.usbir.R;
// Import com.topdon.lib.core.common.SharedManager;
// Import com.topdon.lib.core.tools.UnitTools;
//
// Import java.util.ArrayList;
//
// /**
// *
// */
// Public class TemperatureViewOld extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
//
// Private final String TAG = "TemperatureView";
// Private final int LINE_STROKE_WIDTH = SizeUtils.dp2px(1f);// Point,line,area画笔大小
// Private final int DOT_STROKE_WIDTH = SizeUtils.dp2px(1f);// 圆pointline宽
// Private final int DOT_RADIUS = SizeUtils.dp2px(3f);// 圆point半径
// Private final int POINT_SIZE = SizeUtils.sp2px(8f);// 十字架
// Private final int TEXT_SIZE = SizeUtils.sp2px(14f);// 文本大小
//
//    // Private final int TOUCH_TOLERANCE = 48;
// Private final int TOUCH_TOLERANCE = SizeUtils.sp2px(7f);
// Private final boolean isShowC;
// Private float minTemperatureTem;
// Private float maxTemperatureTem;
// Private Canvas regionAndValueCanvas;
// Private Rect tempRect;
// Private int drawCount = 3;
//
// Private int POINT_MAX_COUNT = 3;
// Private int LINE_MAX_COUNT = 3;
// Private int RECTANGLE_MAX_COUNT = 3;
//
// Private Runnable runnable;
// Public Thread temperatureThread;
// Private Libirtemp irtemp;
// Private float minTemperature;
// Private float maxTemperature;
//    // 框里area的maximum温和minimum温
// Private String RectMinTemp, RectMaxTemp;
//
//    // Private float scale = 0;
// Private float xscale = 0;// ImageScale比例
// Private float yscale = 0;
// Private int viewWidth = 0;// 控件宽度
// Private int viewHeight = 0;// 控件高度
// Private Bitmap regionBitmap;
// Private Bitmap regionAndValueBitmap;
// Private Object regionLock = new Object();
// Private Paint linePaint;
// Private Paint bluePaint;
// Private Paint redPaint;
// Private Paint whitePaint;
// Private Paint maxPaint;
// Private Paint minPaint;
//
// Private int actionMode;
// Private static final int ACTION_MODE_INSERT = 0;
// Private static final int ACTION_MODE_MOVE = 1;
//
// Private float startX, startY, endX, endY;
// Public static int REGION_MODE_CLEAN = 0;
// Public static int REGION_MODE_POINT = 1;
// Public static int REGION_MODE_LINE = 2;
// Public static int REGION_MODE_RECTANGLE = 3;
// Public static int REGION_MODE_CENTER = 4;
//    /* point */
// Private ArrayList<Point> points = new ArrayList<Point>();
// Private Point movingPoint;
//    /* line */
// Private ArrayList<Line> lines = new ArrayList<Line>();
// Private Line movingLine;
// Private int lineMoveType;
// Private static final int LINE_MOVE_ENTIRE = 0;
// Private static final int LINE_MOVE_POINT = 1;
// Private int lineMovePoint;
// Private static final int LINE_START = 0;
// Private static final int LINE_END = 1;
//
//    /* rectangle */
// Private ArrayList<Rect> rectangles = new ArrayList<Rect>();
//
// Private Rect movingRectangle;
// Private int rectangleMoveType;
// Private static final int RECTANGLE_MOVE_ENTIRE = 0;
// Private static final int RECTANGLE_MOVE_EDGE = 1;
// Private static final int RECTANGLE_MOVE_CORNER = 2;
// Private int rectangleMoveEdge;
// Private static final int RECTANGLE_LEFT_EDGE = 0;
// Private static final int RECTANGLE_TOP_EDGE = 1;
// Private static final int RECTANGLE_RIGHT_EDGE = 2;
// Private static final int RECTANGLE_BOTTOM_EDGE = 3;
// Private int rectangleMoveCorner;
// Private static final int RECTANGLE_LEFT_TOP_CORNER = 0;
// Private static final int RECTANGLE_RIGHT_TOP_CORNER = 1;
// Private static final int RECTANGLE_RIGHT_BOTTOM_CORNER = 2;
// Private static final int RECTANGLE_LEFT_BOTTOM_CORNER = 3;
// Private int imageWidth;
// Private int imageHeight;
// Private SynchronizedBitmap syncimage;
// Private int temperatureRegionMode; // 0:point  1:line  2: area  3:全屏
// Private boolean runflag = true;
// Private boolean isShow = false;
//
// Private final static int PIXCOUNT = 5;
//
// Public TemperatureSampleResult centerResultList = null;
// Public ArrayList<TemperatureSampleResult> pointResultList = new ArrayList<>(3);
// Public ArrayList<TemperatureSampleResult> lineResultList = new ArrayList<>(3);
// Public ArrayList<TemperatureSampleResult> rectangleResultList = new ArrayList<>(3);
//
// Public TempListener listener;
//
// Public boolean canTouch = true;
//
// Public TemperatureSampleResult getV() {
//        TemperatureSampleResult result = irtemp.getTemperatureOfLine(new Line(new Point(50, 100), new Point(100, 50)));
//        Log.w("123", "data size:" + irtemp.data.length);
//        Log.w("123", "data scale:" + irtemp.scale);
//        Log.w("123", "data tempDataRes_t:" + (int) irtemp.tempDataRes_t.width + ", h:" + (int) irtemp.tempDataRes_t.height);
// Return result;
//    }
//
// Public boolean isCanTouch() {
// Return canTouch;
//    }
//
// Public void setCanTouch(boolean canTouch) {
// This.canTouch = canTouch;
//    }
//
// Public void setMinTemperature(float minTemperature) {
// This.minTemperature = minTemperature;
//    }
//
// Public void setMaxTemperature(float maxTemperature) {
// This.maxTemperature = maxTemperature;
//    }
//
// Public TemperatureSampleResult getPointTemp(Point point) {
// Return irtemp.getTemperatureOfPoint(point);
//    }
//
// Public TemperatureSampleResult getLineTemp(Line line) {
// Return irtemp.getTemperatureOfLine(line);
//    }
//
// Public TemperatureSampleResult getRectTemp(Rect rect) {
// Return irtemp.getTemperatureOfRect(rect);
//    }
//
//    // ImageWidth: 192, imageHeight: 256
// Public void setImageSize(int imageWidth, int imageHeight) {
//        Log.w("123", "imageWidth: " + imageWidth + ", imageHeight: " + imageHeight);
// This.imageWidth = imageWidth;
// This.imageHeight = imageHeight;
// If (viewWidth != 0)
// Xscale = (float) viewWidth / (float) imageWidth;
// If (viewHeight != 0)
// Yscale = (float) viewHeight / (float) imageHeight;
// Irtemp = new Libirtemp(imageWidth, imageHeight);
//
// CenterResultList = irtemp.new TemperatureSampleResult();
// PointResultList.clear();
// LineResultList.clear();
// RectangleResultList.clear();
// For (int i = 0; i < drawCount; i++) {
// PointResultList.add(irtemp.new TemperatureSampleResult());
// LineResultList.add(irtemp.new TemperatureSampleResult());
// RectangleResultList.add(irtemp.new TemperatureSampleResult());
//        }
//    }
//
// Public void setSyncimage(SynchronizedBitmap syncimage) {
// This.syncimage = syncimage;
//    }
//
// Public void setTemperatureRegionMode(int temperatureRegionMode) {
// This.temperatureRegionMode = temperatureRegionMode;
//    }
//
// Public int getTemperatureRegionMode() {
// Return this.temperatureRegionMode;
//    }
//
// Public void setTemperature(byte[] temperature) {
// This.temperature = temperature;
//    }
//
// Private byte[] temperature;
//
// Public TemperatureViewOld(final Context context) {
// This(context, null, 0);
//    }
//
// Public TemperatureViewOld(final Context context, final AttributeSet attrs) {
// This(context, attrs, 0);
//    }
//
// Public TemperatureViewOld(final Context context, final AttributeSet attrs, final int defStyle) {
// Super(context, attrs, defStyle);
// IsShowC =  SharedManager.INSTANCE.getTemperature() == 1;
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TemperatureView);
// Try {
// DrawCount = ta.getInteger(R.styleable.TemperatureView_temperature_count, 3);
//        } catch (Exception e) {
//            // Ignored
//        } finally {
// Ta.recycle();
//        }
//        POINT_MAX_COUNT = drawCount;
//        LINE_MAX_COUNT = drawCount;
//        RECTANGLE_MAX_COUNT = drawCount;
// GetHolder().addCallback(this);
// SetOnTouchListener(this);
// Runnable = () -> {
// Int length = imageWidth * imageHeight * 2;
// Byte[] sampledTemperature = new byte[length];
//
// LinePaint = new Paint();
// LinePaint.setStrokeWidth(LINE_STROKE_WIDTH);
// // GreenPaint.setColor(Color.GREEN);
// LinePaint.setColor(Color.WHITE);
//
// BluePaint = new Paint();
// BluePaint.setStrokeWidth(DOT_STROKE_WIDTH);
// BluePaint.setStyle(Paint.Style.STROKE);
// BluePaint.setTextSize(TEXT_SIZE);
// BluePaint.setColor(Color.BLUE);
// // BluePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//
// RedPaint = new Paint();
// RedPaint.setStrokeWidth(DOT_STROKE_WIDTH);
// RedPaint.setStyle(Paint.Style.STROKE);
// RedPaint.setTextSize(TEXT_SIZE);
// RedPaint.setColor(Color.RED);
// // RedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//
// WhitePaint = new Paint();
// WhitePaint.setStrokeWidth(DOT_STROKE_WIDTH);
// WhitePaint.setStyle(Paint.Style.STROKE);
// WhitePaint.setTextSize(TEXT_SIZE);
// WhitePaint.setColor(Color.WHITE);
//
// MaxPaint = new Paint();
// MaxPaint.setTextSize(TEXT_SIZE);
// MaxPaint.setColor(Color.WHITE);
//
// MinPaint = new Paint();
// MinPaint.setTextSize(TEXT_SIZE);
// MinPaint.setColor(Color.WHITE);
//
// While (!temperatureThread.isInterrupted() && runflag) {
//
// Synchronized (syncimage.dataLock) {
// Irtemp.settempdata(temperature);
// If (syncimage.type == 1) irtemp.setScale(16);
//                }
//
//                // Centerpointdata
//                TemperatureSampleResult temperatureSampleResult = irtemp.getTemperatureOfRect(new Rect(0, 0, imageWidth / 2, imageHeight - 1));
// MaxTemperature = temperatureSampleResult.maxTemperature;
// MinTemperature = temperatureSampleResult.minTemperature;
//
//                // Point,line,框
// If (rectangles.size() != 0 || lines.size() != 0 || points.size() != 0 || temperatureRegionMode == REGION_MODE_CENTER) {
// Synchronized (regionLock) {
// Int moveX = SizeUtils.dp2px(8);
// If(regionAndValueCanvas == null){
// RegionAndValueCanvas = new Canvas(regionAndValueBitmap);
//                        }else {
// RegionAndValueCanvas.setBitmap(regionAndValueBitmap);
//                        }
// RegionAndValueCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// RegionAndValueCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
//                        // Get/Retrieve全图maximum温和minimum温的data
// If(tempRect == null){
// TempRect = new Rect(0, 0, imageWidth - 1, imageHeight - 1);
//                        }else{
// TempRect.left = 0;
// TempRect.top = 0;
// TempRect.right = imageWidth - 1;
// TempRect.bottom = imageHeight - 1;
//                        }
//                        TemperatureSampleResult temperatureSampleEasyResult = irtemp.getTemperatureOfRect(tempRect);
//
// Float newMaxTemperatureTem = UnitTools.toFloatValue(temperatureSampleEasyResult.maxTemperature);
// Float newMinTemperatureTem = UnitTools.toFloatValue(temperatureSampleEasyResult.minTemperature);
// Boolean hasChange = false;
// If (newMaxTemperatureTem != maxTemperatureTem){
// MaxTemperatureTem = newMaxTemperatureTem;
// HasChange = true;
//                        }
// If (newMinTemperatureTem != minTemperatureTem){
// MinTemperatureTem = newMinTemperatureTem;
// HasChange = true;
//                        }
// If (listener != null && hasChange) {
// Listener.getTemp(newMaxTemperatureTem, newMinTemperatureTem);
//                        }
//                        // 全局minimum温
// Float minX0 = temperatureSampleEasyResult.minTemperaturePixel.x * xscale;
// Float minY0 = temperatureSampleEasyResult.minTemperaturePixel.y * yscale;
//                        String minTem = showCText(minTemperatureTem);
//                        // 越界修正coordinatepoint(minX0, minY0)
// If (minX0 <= 0 && minY0 <= 0) {
// MinX0 = PIXCOUNT;
// MinY0 = PIXCOUNT;
//                        } else if (minX0 <= 0 && (minY0 > 0 && minY0 <= viewHeight)) {
// MinX0 = PIXCOUNT;
//                        } else if (minX0 <= 0 && (minY0 >= viewHeight)) {
// MinX0 = PIXCOUNT;
// MinY0 = viewHeight - PIXCOUNT;
//                        } else if (minX0 >= viewWidth && minY0 <= 0) {
// MinY0 = PIXCOUNT;
//                        } else if (minX0 >= viewWidth && minY0 >= viewHeight) {
// MinX0 = viewWidth - PIXCOUNT;
// MinY0 = viewHeight - PIXCOUNT;
//                        } else if ((minX0 > 0 && minX0 <= viewWidth) && minY0 >= viewHeight) {
// MinY0 = viewHeight - PIXCOUNT;
//                        } else if ((minX0 > 0 && minX0 <= viewWidth) && minY0 <= 0) {
// MinY0 = PIXCOUNT;
//                        } else if (minX0 >= viewWidth && (minY0 > 0 && minY0 <= viewHeight)) {
// MinX0 = viewWidth - PIXCOUNT;
//                        }
// Float minTemTextX = minX0;
// Float minTemTextY = minY0;
// Float minTemTextTolerate = 30;
//                        // 越界修正填充coordinatepoint(minTemTextX, minTemTextY)
// If (minX0 <= minTemTextTolerate && minY0 <= minTemTextTolerate) {
// MinTemTextX = minTemTextTolerate;
// MinTemTextY = minTemTextTolerate;
//                        } else if (minX0 <= minTemTextTolerate && (minY0 > minTemTextTolerate && minY0 <= viewHeight - minTemTextTolerate)) {
// MinTemTextX = minTemTextTolerate;
//                        } else if (minX0 <= minTemTextTolerate && (minY0 >= viewHeight - minTemTextTolerate)) {
// MinTemTextX = minTemTextTolerate;
// MinTemTextY = viewHeight - minTemTextTolerate;
//                        } else if (minX0 >= viewWidth - minTemTextTolerate && minY0 <= minTemTextTolerate) {
// MinTemTextX = (float) (viewWidth - minTemTextTolerate * 1.5);
// MinTemTextY = minTemTextTolerate;
//                        } else if (minX0 >= viewWidth - minTemTextTolerate && minY0 >= viewHeight - minTemTextTolerate) {
// MinTemTextX = viewWidth - minTemTextTolerate;
// MinTemTextY = viewHeight - minTemTextTolerate;
//                        } else if ((minX0 > minTemTextTolerate && minX0 <= viewWidth - minTemTextTolerate) && minY0 >= viewHeight - minTemTextTolerate) {
// MinTemTextY = viewHeight - minTemTextTolerate;
//                        } else if ((minX0 > minTemTextTolerate && minX0 <= viewWidth - minTemTextTolerate) && minY0 <= minTemTextTolerate) {
// MinTemTextY = minTemTextTolerate;
//                        } else if (minX0 >= viewWidth - minTemTextTolerate && (minY0 > minTemTextTolerate && minY0 <= viewHeight - minTemTextTolerate)) {
// MinTemTextX = (float) (viewWidth - minTemTextTolerate * 1.5);
//                        } else {
// MinTemTextX = minX0;
// MinTemTextY = minY0;
//                        }
//                        // 绘制全局minimumtemperature
// If (temperatureRegionMode == REGION_MODE_CENTER) {
// RegionAndValueCanvas.drawText(minTem, 0, minTem.length(), minTemTextX + moveX, minTemTextY, maxPaint);
// DrawDot(regionAndValueCanvas, bluePaint, minX0, minY0);
//                        }
//                        // 全局maximum温
//                        String maxTem = showCText(maxTemperatureTem);
// Float maxTemX = temperatureSampleEasyResult.maxTemperaturePixel.x * xscale;
// Float maxTemY = temperatureSampleEasyResult.maxTemperaturePixel.y * yscale;
//                        // 越界修正coordinatepoint
// If (maxTemX <= 0 && maxTemY <= 0) {
// MaxTemX = PIXCOUNT;
// MaxTemY = PIXCOUNT;
//                        } else if (maxTemX <= 0 && (maxTemY > 0 && maxTemY <= viewHeight)) {
// MaxTemX = PIXCOUNT;
//                        } else if (maxTemX <= 0 && (maxTemY >= viewHeight)) {
// MaxTemX = PIXCOUNT;
// MaxTemY = viewHeight - PIXCOUNT;
//                        } else if (maxTemX >= viewWidth && maxTemY <= 0) {
// MaxTemY = PIXCOUNT;
//                        } else if (maxTemX >= viewWidth && maxTemY >= viewHeight) {
// MaxTemX = viewWidth - PIXCOUNT;
// MaxTemY = viewHeight - PIXCOUNT;
//                        } else if ((maxTemX > 0 && maxTemX <= viewWidth) && maxTemY >= viewHeight) {
// MaxTemY = viewHeight - PIXCOUNT;
//                        } else if ((maxTemX > 0 && maxTemX <= viewWidth) && maxTemY <= 0) {
// MaxTemY = PIXCOUNT;
//                        } else if (maxTemX >= viewWidth && (maxTemY > 0 && maxTemY < viewHeight)) {
// MaxTemX = viewWidth - PIXCOUNT;
//                        }
// Float maxTemTextX = maxTemX;
// Float maxTemTextY = maxTemY;
//                        // 越界修正填充coordinatepoint(maxTemTextX, maxTemTextY)
// If (maxTemX <= minTemTextTolerate && maxTemY <= minTemTextTolerate) {
// MaxTemTextX = minTemTextTolerate;
// MaxTemTextY = minTemTextTolerate;
//                        } else if (maxTemX <= minTemTextTolerate && (maxTemY > minTemTextTolerate && maxTemY <= viewHeight - minTemTextTolerate)) {
// MaxTemTextX = minTemTextTolerate;
//                        } else if (maxTemX <= minTemTextTolerate && (maxTemY >= viewHeight - minTemTextTolerate)) {
// MaxTemTextX = minTemTextTolerate;
// MaxTemTextY = viewHeight - minTemTextTolerate;
//                        } else if (maxTemX >= viewWidth - minTemTextTolerate && maxTemY <= minTemTextTolerate) {
// MaxTemTextX = (float) (viewWidth - minTemTextTolerate * 1.5);
// MaxTemTextY = minTemTextTolerate;
//                        } else if (maxTemX >= viewWidth - minTemTextTolerate && maxTemY >= viewHeight - minTemTextTolerate) {
// MaxTemTextX = viewWidth - minTemTextTolerate;
// MaxTemTextY = viewHeight - minTemTextTolerate;
//                        } else if ((maxTemX > minTemTextTolerate && maxTemX <= viewWidth - minTemTextTolerate) && maxTemY >= viewHeight - minTemTextTolerate) {
// MaxTemTextY = viewHeight - minTemTextTolerate;
//                        } else if ((maxTemX > minTemTextTolerate && maxTemX <= viewWidth - minTemTextTolerate) && maxTemY <= minTemTextTolerate) {
// MaxTemTextY = minTemTextTolerate;
//                        } else if (maxTemX >= viewWidth - minTemTextTolerate && (maxTemY > minTemTextTolerate && maxTemY <= viewHeight - minTemTextTolerate)) {
// MaxTemTextX = (float) (viewWidth - minTemTextTolerate * 1.5);
//                        } else {
// MaxTemTextX = maxTemX;
// MaxTemTextY = maxTemY;
//                        }
//
//                        // 绘制全局maximumtemperature
// If (temperatureRegionMode == REGION_MODE_CENTER) {
// RegionAndValueCanvas.rotate(0, maxTemTextX, maxTemTextY);
// RegionAndValueCanvas.drawText(maxTem, 0, maxTem.length(), maxTemTextX + moveX, maxTemTextY, maxPaint);
// DrawDot(regionAndValueCanvas, redPaint, maxTemTextX, maxTemTextY);
//                        }
//
//                        // Areatemperature
// For (int index = 0; index < rectangles.size(); index++) {
//                            Rect tempRectangle = rectangles.get(index);
// Int left = (int) (tempRectangle.left / xscale);
// Int top = (int) (tempRectangle.top / yscale);
// Int right = (int) (tempRectangle.right / xscale);
// Int bottom = (int) (tempRectangle.bottom / yscale);
//                            Log.d(TAG, "Rectangle right: " + right + ", bottom: " + bottom);
// If (right > left && bottom > top && left < imageWidth && top < imageHeight && right > 0 && bottom > 0) {
// TemperatureSampleResult = irtemp.getTemperatureOfRect(new Rect(left, top, right, bottom));
// RectangleResultList.set(index, temperatureSampleResult);
// RectangleResultList.get(index).index = index + 1;
//                                String min = showCText(temperatureSampleResult.minTemperature);
//                                String max = showCText(temperatureSampleResult.maxTemperature);
//
// SetRectMaxTemp(max);
// SetRectMinTemp(min);
//
// DrawDot(regionAndValueCanvas, bluePaint, temperatureSampleResult.minTemperaturePixel.x * xscale, temperatureSampleResult.minTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(min, 0, min.length(), temperatureSampleResult.minTemperaturePixel.x * xscale + moveX, temperatureSampleResult.minTemperaturePixel.y * yscale, minPaint);
// DrawDot(regionAndValueCanvas, redPaint, temperatureSampleResult.maxTemperaturePixel.x * xscale, temperatureSampleResult.maxTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(max, 0, max.length(), temperatureSampleResult.maxTemperaturePixel.x * xscale + moveX, temperatureSampleResult.maxTemperaturePixel.y * yscale, maxPaint);
//                            }
//                        }
// For (int i = rectangles.size(); i < drawCount; i++) {
// RectangleResultList.get(i).index = 0;
//                        }
//                        // Linetemperature
// For (int index = 0; index < lines.size(); index++) {
//                            Line tempLine = lines.get(index);
// Int startX = (int) (tempLine.start.x / xscale);
// Int startY = (int) (tempLine.start.y / yscale);
// Int endX = (int) (tempLine.end.x / xscale);
// Int endY = (int) (tempLine.end.y / yscale);
// Int minX = Math.min(startX, endX);
// Int maxX = Math.max(startX, endX);
// Int minY = Math.min(startY, endY);
// Int maxY = Math.max(startY, endY);
// If (maxX < imageWidth && minX > 0 && maxY < imageHeight && minY > 0) {
//                                Log.d(TAG, "start point: (" + startX + ", " + startY + "), endX: (" + endX + ", " + endY + ")");
// TemperatureSampleResult = irtemp.getTemperatureOfLine(new Line(new Point(startX, startY), new Point(endX, endY)));
// LineResultList.set(index, temperatureSampleResult);
// LineResultList.get(index).index = index + 1;
//                                // 读取到temperature
//                                Log.d(TAG, "minTemperaturePixel x: " + temperatureSampleResult.minTemperaturePixel.x);
//                                String min = showCText(temperatureSampleResult.minTemperature);
//                                String max = showCText(temperatureSampleResult.maxTemperature);
// DrawDot(regionAndValueCanvas, bluePaint, temperatureSampleResult.minTemperaturePixel.x * xscale, temperatureSampleResult.minTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(min, 0, min.length(), temperatureSampleResult.minTemperaturePixel.x * xscale + moveX, temperatureSampleResult.minTemperaturePixel.y * yscale, minPaint);
// DrawDot(regionAndValueCanvas, redPaint, temperatureSampleResult.maxTemperaturePixel.x * xscale, temperatureSampleResult.maxTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(max, 0, max.length(), temperatureSampleResult.maxTemperaturePixel.x * xscale + moveX, temperatureSampleResult.maxTemperaturePixel.y * yscale, maxPaint);
//                            }
//                        }
// For (int i = lines.size(); i < drawCount; i++) {
//                            // Settings不计数state
// LineResultList.get(i).index = 0;
//                        }
//                        // Pointtemperature
// For (int index = 0; index < points.size(); index++) {
//                            Point tempPoint = points.get(index);
// Int x = (int) (tempPoint.x / xscale);// 精度丢失,processing方式:在onTouch绘制的十字标做同样丢失,保证Show/Displaypoint校对
// Int y = (int) (tempPoint.y / yscale);
// If (x < imageWidth && x > 0 && y < imageHeight && y > 0) {
// TemperatureSampleResult = irtemp.getTemperatureOfPoint(new Point(x, y));
// PointResultList.set(index, temperatureSampleResult);
// PointResultList.get(index).index = index + 1;
//                                String max = showCText(temperatureSampleResult.maxTemperature);
// DrawDot(regionAndValueCanvas, whitePaint, temperatureSampleResult.maxTemperaturePixel.x * xscale, temperatureSampleResult.maxTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(max, 0, max.length(), temperatureSampleResult.maxTemperaturePixel.x * xscale + moveX, temperatureSampleResult.maxTemperaturePixel.y * yscale, maxPaint);
//                            }
//                        }
// For (int i = points.size(); i < drawCount; i++) {
// PointResultList.get(i).index = 0;
//                        }
//                        // Centertemperature
// If (temperatureRegionMode == REGION_MODE_CENTER ||
// TemperatureRegionMode == REGION_MODE_POINT ||
// TemperatureRegionMode == REGION_MODE_LINE ||
// TemperatureRegionMode == REGION_MODE_RECTANGLE) {
// TemperatureSampleResult = irtemp.getTemperatureOfPoint(new Point(imageWidth / 2, imageHeight / 2));
// CenterResultList = temperatureSampleResult;
//                            String max = showCText(temperatureSampleResult.maxTemperature);
// // DrawDot(canvas, redPaint, temperatureSampleResult.maxTemperaturePixel.x * xscale, temperatureSampleResult.maxTemperaturePixel.y * yscale);
// RegionAndValueCanvas.drawText(max, 0, max.length(), temperatureSampleResult.maxTemperaturePixel.x * xscale + moveX, temperatureSampleResult.maxTemperaturePixel.y * yscale - SizeUtils.dp2px(2.5f), maxPaint);
//                        }
//                    }
// Try {
//                        Canvas surfaceViewCanvas = getHolder().lockCanvas();
// If (surfaceViewCanvas == null) {
// Continue;
//                        }
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionAndValueBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                    }catch (Exception e){
//                        XLog.e("Temperature Viewrefreshexception: " + e.getMessage());
//                    }
//                }else {
//                    TemperatureSampleResult temperatureSampleEasyResult = null;
// TempRect = new Rect(0, 0, imageWidth - 1, imageHeight - 1);
// If (irtemp != null){
// TemperatureSampleEasyResult = irtemp.getTemperatureOfRect(tempRect);
//                    }
// If (temperatureSampleEasyResult!=null){
// Float newMaxTemperatureTem = UnitTools.toFloatValue(temperatureSampleEasyResult.maxTemperature);
// Float newMinTemperatureTem = UnitTools.toFloatValue(temperatureSampleEasyResult.minTemperature);
// Boolean hasChange = false;
// If (newMaxTemperatureTem != maxTemperatureTem){
// MaxTemperatureTem = newMaxTemperatureTem;
// HasChange = true;
//                        }
// If (newMinTemperatureTem != minTemperatureTem){
// MinTemperatureTem = newMinTemperatureTem;
// HasChange = true;
//                        }
// If (listener != null && hasChange) {
// Listener.getTemp(newMaxTemperatureTem, newMinTemperatureTem);
//                        }
//                    }
//                }
//
// //                SystemClock.sleep(333);
// Try {
//                    SystemClock.sleep(1000);// Settingsrefresh间隔
// // Int[] value = new int[1];
// //                    Libircmd.set_prop_tpd_params(Libircmd.TPD_PROP_GAIN_SEL, (char) 0, 1);
//                } catch (Exception e) {
//                    XLog.e("Temperature Viewrefreshexception: " + e.getMessage());
//                }
//            }
//            Log.d(TAG, "temperatureThread exit");
//        };
//
//    }
//
//
// Public String showCText(Float  temp){
// Return UnitTools.showC(temp,isShowC);
//    }
//
//
//
//    @Override
// Protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
// Int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
// Int initialHeight = MeasureSpec.getSize(heightMeasureSpec);
//
// Int paddingLeft = getPaddingLeft();
// Int paddingRight = getPaddingRight();
// Int paddingTop = getPaddingTop();
// Int paddingBottom = getPaddingBottom();
//
// InitialWidth -= paddingLeft + paddingRight;
// InitialHeight -= paddingTop + paddingBottom;
//
// Xscale = (float) initialWidth / (float) imageWidth;
// Yscale = (float) initialHeight / (float) imageHeight;
//
// ViewWidth = initialWidth;
// ViewHeight = initialHeight;
// If (regionBitmap == null) {
// RegionBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_4444);
//        }
// RegionAndValueBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_4444);
//
// Super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    }
//
//    @Override
// Public void surfaceCreated(SurfaceHolder holder) {
//        Log.w(TAG, "surfaceCreated");
// SetZOrderOnTop(true);
// Holder.setFormat(PixelFormat.TRANSLUCENT);
//    }
//
//    @Override
// Public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
// Public void surfaceDestroyed(SurfaceHolder holder) {
//        Log.w(TAG, "surfaceDestroyed");
//    }
//
//    /**
//     * temperaturemeasurement选区
//     */
//    @Override
// Public boolean onTouch(View v, MotionEvent event) {
// If (!canTouch){
// Return false;
//        }
// If (temperatureRegionMode == REGION_MODE_RECTANGLE) {
// If (event.getAction() == MotionEvent.ACTION_DOWN) {
// StartX = event.getX();
// StartY = event.getY();
//                Log.w(TAG, "ACTION_DOWN" + startX + "|" + startY);
//                Rect rectangle = getRectangle(new Point((int) startX, (int) startY));
// If (rectangle.equals(new Rect())) {
// ActionMode = ACTION_MODE_INSERT;
//                    Log.w(TAG, "ACTION_MODE_INSERT");
//                } else {
// ActionMode = ACTION_MODE_MOVE;
// MovingRectangle = rectangle;
//                    Log.w(TAG, "ACTION_MODE_MOVE");
// If (startX > rectangle.left - TOUCH_TOLERANCE && startX < rectangle.left + TOUCH_TOLERANCE && startY > rectangle.top - TOUCH_TOLERANCE && startY < rectangle.top + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move left top corner");
// RectangleMoveType = RECTANGLE_MOVE_CORNER;
// RectangleMoveCorner = RECTANGLE_LEFT_TOP_CORNER;
//                    } else if (startX > rectangle.right - TOUCH_TOLERANCE && startX < rectangle.right + TOUCH_TOLERANCE && startY > rectangle.top - TOUCH_TOLERANCE && startY < rectangle.top + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move right top corner");
// RectangleMoveType = RECTANGLE_MOVE_CORNER;
// RectangleMoveCorner = RECTANGLE_RIGHT_TOP_CORNER;
//                    } else if (startX > rectangle.right - TOUCH_TOLERANCE && startX < rectangle.right + TOUCH_TOLERANCE && startY > rectangle.bottom - TOUCH_TOLERANCE && startY < rectangle.bottom + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move right bottom corner");
// RectangleMoveType = RECTANGLE_MOVE_CORNER;
// RectangleMoveCorner = RECTANGLE_RIGHT_BOTTOM_CORNER;
//                    } else if (startX > rectangle.left - TOUCH_TOLERANCE && startX < rectangle.left + TOUCH_TOLERANCE && startY > rectangle.bottom - TOUCH_TOLERANCE && startY < rectangle.bottom + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move left bottom corner");
// RectangleMoveType = RECTANGLE_MOVE_CORNER;
// RectangleMoveCorner = RECTANGLE_LEFT_BOTTOM_CORNER;
//                    } else if (startX > rectangle.left - TOUCH_TOLERANCE && startX < rectangle.left + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move left edge");
// RectangleMoveType = RECTANGLE_MOVE_EDGE;
// RectangleMoveEdge = RECTANGLE_LEFT_EDGE;
//                    } else if (startY > rectangle.top - TOUCH_TOLERANCE && startY < rectangle.top + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move top edge");
// RectangleMoveType = RECTANGLE_MOVE_EDGE;
// RectangleMoveEdge = RECTANGLE_TOP_EDGE;
//                    } else if (startX > rectangle.right - TOUCH_TOLERANCE && startX < rectangle.right + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move right edge");
// RectangleMoveType = RECTANGLE_MOVE_EDGE;
// RectangleMoveEdge = RECTANGLE_RIGHT_EDGE;
//                    } else if (startY > rectangle.bottom - TOUCH_TOLERANCE && startY < rectangle.bottom + TOUCH_TOLERANCE) {
//                        Log.w(TAG, "move bottom edge");
// RectangleMoveType = RECTANGLE_MOVE_EDGE;
// RectangleMoveEdge = RECTANGLE_BOTTOM_EDGE;
//                    } else {
//                        Log.w(TAG, "move entire");
// RectangleMoveType = RECTANGLE_MOVE_ENTIRE;
//                    }
// Synchronized (regionLock) {
// DeleteRectangle(rectangle);
//                    }
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SetBitmap();
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawRectangle(surfaceViewCanvas, linePaint, rectangle.left, rectangle.top, rectangle.right, rectangle.bottom);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;            // Must
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
// EndX = event.getX();
// EndY = event.getY();
//                Log.w(TAG, "ACTION_DOWN " + endX + " | " + endY);
// If (actionMode == ACTION_MODE_INSERT) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawRectangle(surfaceViewCanvas, linePaint, startX, startY, endX, endY);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else if (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
// If (rectangleMoveType == RECTANGLE_MOVE_ENTIRE) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left + biasX, movingRectangle.top + biasY, movingRectangle.right + biasX, movingRectangle.bottom + biasY);
//                    }
// If (rectangleMoveType == RECTANGLE_MOVE_EDGE) {
// If (rectangleMoveEdge == RECTANGLE_LEFT_EDGE) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left + biasX, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
//                        }
// If (rectangleMoveEdge == RECTANGLE_TOP_EDGE) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left, movingRectangle.top + biasY, movingRectangle.right, movingRectangle.bottom);
//                        }
// If (rectangleMoveEdge == RECTANGLE_RIGHT_EDGE) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right + biasX, movingRectangle.bottom);
//                        }
// If (rectangleMoveEdge == RECTANGLE_BOTTOM_EDGE) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom + biasY);
//                        }
//                    }
// If (rectangleMoveType == RECTANGLE_MOVE_CORNER) {
// If (rectangleMoveCorner == RECTANGLE_LEFT_TOP_CORNER) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left + biasX, movingRectangle.top + biasY, movingRectangle.right, movingRectangle.bottom);
//                        }
// If (rectangleMoveCorner == RECTANGLE_RIGHT_TOP_CORNER) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left, movingRectangle.top + biasY, movingRectangle.right + biasX, movingRectangle.bottom);
//                        }
// If (rectangleMoveCorner == RECTANGLE_RIGHT_BOTTOM_CORNER) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right + biasX, movingRectangle.bottom + biasY);
//                        }
// If (rectangleMoveCorner == RECTANGLE_LEFT_BOTTOM_CORNER) {
// DrawRectangle(surfaceViewCanvas, linePaint, movingRectangle.left + biasX, movingRectangle.top, movingRectangle.right, movingRectangle.bottom + biasY);
//                        }
//                    }
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                Log.w(TAG, "ACTION_UP");
// EndX = event.getX();
// EndY = event.getY();
// If (actionMode == ACTION_MODE_INSERT) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// If (Math.abs(endX - startX) > TOUCH_TOLERANCE || Math.abs(endY - startY) > TOUCH_TOLERANCE) {
// Int left = (int) Math.min(startX, endX);
// Int right = (int) Math.max(startX, endX);
// Int top = (int) Math.min(startY, endY);
// Int bottom = (int) Math.max(startY, endY);
// If (rectangles.size() < RECTANGLE_MAX_COUNT) {
// Synchronized (regionLock) {
// AddRectangle(new Rect(left, top, right, bottom));
//                            }
//                            Canvas bitmapCanvas = new Canvas(regionBitmap);
// DrawRectangle(bitmapCanvas, linePaint, startX, startY, endX, endY);
//                        } else {
// Synchronized (regionLock) {
// AddRectangle(new Rect(left, top, right, bottom));
//                            }
// SetBitmap();
//                        }
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// If (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                    Canvas bitmapCanvas = new Canvas(regionBitmap);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
//                    Log.d(TAG, "ACTION_UP" + movingRectangle.left + " " + movingRectangle.top + "----" + movingRectangle.right + " " + movingRectangle.bottom + " ");
// Int tmp;
// If (Math.abs(biasX) > TOUCH_TOLERANCE || Math.abs(biasY) > TOUCH_TOLERANCE) {
// If (rectangleMoveType == RECTANGLE_MOVE_ENTIRE) {
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left + biasX, movingRectangle.top + biasY, movingRectangle.right + biasX, movingRectangle.bottom + biasY);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left + biasX), (int) (movingRectangle.top + biasY), (int) (movingRectangle.right + biasX), (int) (movingRectangle.bottom + biasY)));
//                            }
//                        }
// If (rectangleMoveType == RECTANGLE_MOVE_EDGE) {
// If (rectangleMoveEdge == RECTANGLE_LEFT_EDGE) {
// MovingRectangle.left += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveEdge == RECTANGLE_TOP_EDGE) {
// MovingRectangle.top += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveEdge == RECTANGLE_RIGHT_EDGE) {
// MovingRectangle.right += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveEdge == RECTANGLE_BOTTOM_EDGE) {
// MovingRectangle.bottom += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
//                        }
// If (rectangleMoveType == RECTANGLE_MOVE_CORNER) {
// If (rectangleMoveCorner == RECTANGLE_LEFT_TOP_CORNER) {
// MovingRectangle.left += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// MovingRectangle.top += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
//
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveCorner == RECTANGLE_RIGHT_TOP_CORNER) {
// MovingRectangle.right += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// MovingRectangle.top += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveCorner == RECTANGLE_RIGHT_BOTTOM_CORNER) {
// MovingRectangle.right += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// MovingRectangle.bottom += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
// If (rectangleMoveCorner == RECTANGLE_LEFT_BOTTOM_CORNER) {
// MovingRectangle.left += biasX;
// If (movingRectangle.right < movingRectangle.left) {
// Tmp = movingRectangle.left;
// MovingRectangle.left = movingRectangle.right;
// MovingRectangle.right = tmp;
//                                }
// MovingRectangle.bottom += biasY;
// If (movingRectangle.bottom < movingRectangle.top) {
// Tmp = movingRectangle.bottom;
// MovingRectangle.bottom = movingRectangle.top;
// MovingRectangle.top = tmp;
//                                }
// DrawRectangle(bitmapCanvas, linePaint, movingRectangle.left, movingRectangle.top, movingRectangle.right, movingRectangle.bottom);
// Synchronized (regionLock) {
// AddRectangle(new Rect((int) (movingRectangle.left), (int) (movingRectangle.top), (int) (movingRectangle.right), (int) (movingRectangle.bottom)));
//                                }
//                            }
//                        }
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return false;
//            } else {
// Return false;
//            }
//        } else if (temperatureRegionMode == REGION_MODE_LINE) {
// If (event.getAction() == MotionEvent.ACTION_DOWN) {
//                Log.w(TAG, "ACTION_DOWN");
// StartX = event.getX();
// StartY = event.getY();
//                Line line = getLine(new Point((int) startX, (int) startY));
// If (line.start == null || line.end == null) {
// ActionMode = ACTION_MODE_INSERT;
//                    Log.w(TAG, "ACTION_MODE_INSERT: startX = " + startX + "; startY = " + startY);
//                } else {
// ActionMode = ACTION_MODE_MOVE;
// MovingLine = line;
//                    Log.w(TAG, "ACTION_MODE_MOVE: startX = " + startX + "; startY = " + startY);
//                    Log.w(TAG, "ACTION_MODE_MOVE: x0 = " + line.start.x + "; y0 = " + line.start.y + "; x1 = " + line.end.x + "; y1 = " + line.end.y);
// If (startX > line.start.x - TOUCH_TOLERANCE && startX < line.start.x + TOUCH_TOLERANCE && startY > line.start.y - TOUCH_TOLERANCE && startY < line.start.y + TOUCH_TOLERANCE) {
// LineMoveType = LINE_MOVE_POINT;
// LineMovePoint = LINE_START;
//                    } else if (startX > line.end.x - TOUCH_TOLERANCE && startX < line.end.x + TOUCH_TOLERANCE && startY > line.end.y - TOUCH_TOLERANCE && startY < line.end.y + TOUCH_TOLERANCE) {
// LineMoveType = LINE_MOVE_POINT;
// LineMovePoint = LINE_END;
//                    } else {
// LineMoveType = LINE_MOVE_ENTIRE;
//                    }
// Synchronized (regionLock) {
// DeleteLine(line);
//                    }
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SetBitmap();
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// If (line.start.x > 0 && line.start.x < viewWidth && line.end.x > 0 && line.end.x < viewWidth && line.start.y > 0 && line.start.y < viewHeight && line.end.y > 0 && line.end.y < viewHeight)
// DrawLine(surfaceViewCanvas, linePaint, line.start.x, line.start.y, line.end.x, line.end.y);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
// EndX = event.getX();
// EndY = event.getY();
// If (actionMode == ACTION_MODE_INSERT) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawLine(surfaceViewCanvas, linePaint, startX, startY, endX, endY);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else if (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
// If (lineMoveType == LINE_MOVE_ENTIRE) {
// DrawLine(surfaceViewCanvas, linePaint, movingLine.start.x + biasX, movingLine.start.y + biasY, movingLine.end.x + biasX, movingLine.end.y + biasY);
//                    } else if (lineMoveType == LINE_MOVE_POINT) {
// If (lineMovePoint == LINE_START) {
// DrawLine(surfaceViewCanvas, linePaint, movingLine.start.x + biasX, movingLine.start.y + biasY, movingLine.end.x, movingLine.end.y);
//                        } else if (lineMovePoint == LINE_END) {
// DrawLine(surfaceViewCanvas, linePaint, movingLine.start.x, movingLine.start.y, movingLine.end.x + biasX, movingLine.end.y + biasY);
//                        }
//                    }
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                Log.w(TAG, "ACTION_UP");
// EndX = event.getX();
// EndY = event.getY();
// If (actionMode == ACTION_MODE_INSERT) {
//                    Log.w(TAG, "ACTION_MODE_INSERT: endX = " + endX + "; endY = " + endY);
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// If (Math.abs(endX - startX) > TOUCH_TOLERANCE || Math.abs(endY - startY) > TOUCH_TOLERANCE) {
//                        Point start = new Point((int) startX, (int) startY);
//                        Point end = new Point((int) endX, (int) endY);
// If (lines.size() < LINE_MAX_COUNT) {
// Synchronized (regionLock) {
// If (start.x > 0 && start.x < viewWidth && end.x > 0 && end.x < viewWidth && start.y > 0 && start.y < viewHeight && end.y > 0 && end.y < viewHeight)
// AddLine(new Line(start, end));
//                            }
//                            Canvas bitmapCanvas = new Canvas(regionBitmap);
// If (start.x > 0 && start.x < viewWidth && end.x > 0 && end.x < viewWidth && start.y > 0 && start.y < viewHeight && end.y > 0 && end.y < viewHeight)
// DrawLine(bitmapCanvas, linePaint, startX, startY, endX, endY);
//                        } else {
// Synchronized (regionLock) {
// If (start.x > 0 && start.x < viewWidth && end.x > 0 && end.x < viewWidth && start.y > 0 && start.y < viewHeight && end.y > 0 && end.y < viewHeight)
// AddLine(new Line(start, end));
//                            }
// SetBitmap();
//                        }
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else if (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                    Canvas bitmapCanvas = new Canvas(regionBitmap);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
// If (movingLine.start.x + biasX > 0 && movingLine.start.x + biasX < viewWidth && movingLine.end.x + biasX > 0 && movingLine.end.x + biasX < viewWidth && movingLine.start.y + biasY > 0 && movingLine.start.y + biasY < viewHeight && movingLine.end.y + biasY > 0 && movingLine.end.y + biasY < viewHeight) {
// If (Math.abs(biasX) > TOUCH_TOLERANCE || Math.abs(biasY) > TOUCH_TOLERANCE) {
// If (lineMoveType == LINE_MOVE_ENTIRE) {
// DrawLine(bitmapCanvas, linePaint, movingLine.start.x + biasX, movingLine.start.y + biasY, movingLine.end.x + biasX, movingLine.end.y + biasY);
// Synchronized (regionLock) {
//                                    Point start = new Point((int) (movingLine.start.x + biasX), (int) (movingLine.start.y + biasY));
//                                    Point end = new Point((int) (movingLine.end.x + biasX), (int) (movingLine.end.y + biasY));
// AddLine(new Line(start, end));
//                                }
//                            } else if (lineMoveType == LINE_MOVE_POINT) {
// If (lineMovePoint == LINE_START) {
// DrawLine(bitmapCanvas, linePaint, movingLine.start.x + biasX, movingLine.start.y + biasY, movingLine.end.x, movingLine.end.y);
// Synchronized (regionLock) {
//                                        Point start = new Point((int) (movingLine.start.x + biasX), (int) (movingLine.start.y + biasY));
//                                        Point end = new Point((int) (movingLine.end.x), (int) (movingLine.end.y));
// AddLine(new Line(start, end));
//                                    }
//                                } else if (lineMovePoint == LINE_END) {
// DrawLine(bitmapCanvas, linePaint, movingLine.start.x, movingLine.start.y, movingLine.end.x + biasX, movingLine.end.y + biasY);
// Synchronized (regionLock) {
//                                        Point start = new Point((int) (movingLine.start.x), (int) (movingLine.start.y));
//                                        Point end = new Point((int) (movingLine.end.x + biasX), (int) (movingLine.end.y + biasY));
// AddLine(new Line(start, end));
//                                    }
//                                }
//                            }
//                        }
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return false;
//            } else {
// Return false;
//            }
//        } else if (temperatureRegionMode == REGION_MODE_POINT) {
// If (event.getAction() == MotionEvent.ACTION_DOWN) {
// StartX = event.getX();
// StartY = event.getY();
//                Log.w(TAG, "ACTION_DOWN" + startX + "|" + startY);
//                Point point = getPoint(new Point((int) startX, (int) startY));
// If (point.equals(new Point())) {
// ActionMode = ACTION_MODE_INSERT;
// If (points.size() == POINT_MAX_COUNT) {
// Synchronized (regionLock) {
// DeletePoint();
//                        }
// SetBitmap();
//                    }
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawPoint(surfaceViewCanvas, linePaint, startX, startY);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else {
// ActionMode = ACTION_MODE_MOVE;
// MovingPoint = point;
// Synchronized (regionLock) {
// DeletePoint(point);
//                    }
// SetBitmap();
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawPoint(surfaceViewCanvas, linePaint, movingPoint.x, movingPoint.y);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
// EndX = event.getX();
// EndY = event.getY();
// If (actionMode == ACTION_MODE_INSERT) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// DrawPoint(surfaceViewCanvas, linePaint, endX, endY);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else if (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
// DrawPoint(surfaceViewCanvas, linePaint, movingPoint.x + biasX, movingPoint.y + biasY);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return true;
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
// EndX = event.getX();
// EndY = event.getY();
// If (actionMode == ACTION_MODE_INSERT) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// If (points.size() < POINT_MAX_COUNT) {
// Synchronized (regionLock) {
// AddPoint(new Point((int) endX, (int) endY));
//                        }
//                        Canvas bitmapCanvas = new Canvas(regionBitmap);
// DrawPoint(bitmapCanvas, linePaint, endX, endY);
//                    } else {
// Synchronized (regionLock) {
// AddPoint(new Point((int) endX, (int) endY));
//                        }
// SetBitmap();
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                } else if (actionMode == ACTION_MODE_MOVE) {
//                    Canvas surfaceViewCanvas = getHolder().lockCanvas();
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                    Canvas bitmapCanvas = new Canvas(regionBitmap);
// Float biasX = endX - startX;
// Float biasY = endY - startY;
// If (Math.abs(biasX) > TOUCH_TOLERANCE || Math.abs(biasY) > TOUCH_TOLERANCE) {
// DrawPoint(bitmapCanvas, linePaint, movingPoint.x + biasX, movingPoint.y + biasY);
// Synchronized (regionLock) {
// AddPoint(new Point((int) (movingPoint.x + biasX), (int) (movingPoint.y + biasY)));
//                        }
//                    }
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//                }
// Return false;
//            } else {
// Return false;
//            }
//        } else {
//
// Return false;
//        }
//    }
//
// Public void addPoint(Point point) {
// If (points.size() < POINT_MAX_COUNT) {
// Points.add(point);
//        } else {
// For (int index = 0; index < points.size() - 1; index++) {
//                Point tempPoint = points.get(index + 1);
// Points.set(index, tempPoint);
//            }
// Points.set(points.size() - 1, point);
//        }
//    }
//
// Public Point getPoint(Point point) {
//        Point point1 = new Point();
// For (int index = 0; index < points.size(); index++) {
//            Point tempPoint = points.get(index);
// If (tempPoint.x > point.x - TOUCH_TOLERANCE && tempPoint.x < point.x + TOUCH_TOLERANCE && tempPoint.y > point.y - TOUCH_TOLERANCE && tempPoint.y < point.y + TOUCH_TOLERANCE) {
// Point1 = tempPoint;
//            }
//        }
// Return point1;
//    }
//
// Public void deletePoint(Point point) {
// For (int index = 0; index < points.size(); index++) {
//            Point tempPoint = points.get(index);
// If (tempPoint.equals(point)) {
// Points.remove(index);
// Break;
//            }
//        }
//    }
//
// Public void deletePoint() {
// For (int index = 0; index < points.size() - 1; index++) {
//            Point tempPoint = points.get(index + 1);
// Points.set(index, tempPoint);
//        }
// Points.remove(points.size() - 1);
//    }
//
// Public void addLine(Line line) {
// If (lines.size() < LINE_MAX_COUNT) {
// Lines.add(line);
//        } else {
// For (int index = 0; index < lines.size() - 1; index++) {
//                Line tempLine = lines.get(index + 1);
// Lines.set(index, tempLine);
//            }
// Lines.set(lines.size() - 1, line);
//        }
//    }
//
//    /**
//     * 输入acoordinate,找出是否已经存在的直line,没有Return一条初始直line
//     */
// Public Line getLine(Point point) {
//        Line line = new Line();
// For (int index = 0; index < lines.size(); index++) {
//            Line tempLine = lines.get(index);
// Int tempDistance = ((tempLine.end.y - tempLine.start.y) * point.x - (tempLine.end.x - tempLine.start.x) * point.y + tempLine.end.x * tempLine.start.y - tempLine.start.x * tempLine.end.y);
// TempDistance = (int) (tempDistance / Math.sqrt(Math.pow(tempLine.end.y - tempLine.start.y, 2) + Math.pow(tempLine.end.x - tempLine.start.x, 2)));
//            Log.w(TAG, "tempDistance = " + tempDistance);
// If (Math.abs(tempDistance) < TOUCH_TOLERANCE && point.x > Math.min(tempLine.start.x, tempLine.end.x) - TOUCH_TOLERANCE && point.x < Math.max(tempLine.start.x, tempLine.end.x) + TOUCH_TOLERANCE) {
// Line = tempLine;
//            }
//        }
// Return line;
//    }
//
// Public void deleteLine(Line line) {
// For (int index = 0; index < lines.size(); index++) {
//            Line tempLine = lines.get(index);
// If (tempLine.start.equals(line.start) && tempLine.end.equals(line.end)) {
// Lines.remove(index);
// Break;
//            }
//        }
//    }
//
// Public void addRectangle(Rect rectangle) {
// If (rectangles.size() < RECTANGLE_MAX_COUNT) {
// Rectangles.add(rectangle);
//        } else {
// For (int index = 0; index < rectangles.size() - 1; index++) {
//                Rect tempRectangle = rectangles.get(index + 1);
// Rectangles.set(index, tempRectangle);
//            }
// Rectangles.set(rectangles.size() - 1, rectangle);
//        }
//    }
//
// Public Rect getRectangle(Point point) {
//        Rect rectangle = new Rect();
// For (int index = 0; index < rectangles.size(); index++) {
//            Rect tempRectangle = rectangles.get(index);
// If (tempRectangle.left - TOUCH_TOLERANCE < point.x && tempRectangle.right + TOUCH_TOLERANCE > point.x
//                    && tempRectangle.top - TOUCH_TOLERANCE < point.y && tempRectangle.bottom + TOUCH_TOLERANCE > point.y) {
// Rectangle = tempRectangle;
//            }
//        }
// Return rectangle;
//    }
//
// Public void deleteRectangle(Rect rect) {
// For (int index = 0; index < rectangles.size(); index++) {
//            Rect tempRectangle = rectangles.get(index);
// If (tempRectangle.equals(rect)) {
// Rectangles.remove(index);
// Break;
//            }
//        }
//    }
//
// // Private void drawPoint(Canvas canvas, Paint paint, float x1, float y1) {
// // Float[] points = new float[]{
// // X1 - POINT_SIZE, y1, x1 + POINT_SIZE, y1,
// // X1, y1 - POINT_SIZE, x1, y1 + POINT_SIZE};
// // Canvas.drawLines(points, paint);
// //    }
//
// Private void drawPoint(Canvas canvas, Paint paint, float x1, float y1) {
//        // Point的单位是int,从floatconversion,导致绘制圆point时已经精度丢失 2022-04-12
// Float x = (int) (x1 / xscale) * xscale;// Mock/SimulatedrawDot入参x1conversion方式
// Float y = (int) (y1 / yscale) * yscale;
//        // 空心十字
// Float[] points = new float[]{
// X - POINT_SIZE, y, x - DOT_RADIUS, y,
// X, y - POINT_SIZE, x, y - DOT_RADIUS,
// X + POINT_SIZE, y, x + DOT_RADIUS, y,
// X, y + POINT_SIZE, x, y + DOT_RADIUS,
//        };
// Canvas.drawLines(points, paint);
//    }
//
// // Private void drawLine(Canvas canvas, Paint paint, float x1, float y1, float x2, float y2) {
// // Float[] points = new float[]{x1, y1, x2, y2};
// // Canvas.drawLines(points, paint);
// //    }
//
// Private void drawLine(Canvas canvas, Paint paint, float x1, float y1, float x2, float y2) {
// Float xStart = (int) (x1 / xscale) * xscale;
// Float yStart = (int) (y1 / yscale) * yscale;
// Float xEnd = (int) (x2 / xscale) * xscale;
// Float yEnd = (int) (y2 / yscale) * yscale;
// Float[] points = new float[]{xStart, yStart, xEnd, yEnd};
// Canvas.drawLines(points, paint);
//    }
//
// Private void drawRectangle(Canvas canvas, Paint paint, float x1, float y1, float x2, float y2) {
// Float[] points = new float[]{x1, y1, x2, y1, x2, y1, x2, y2, x2, y2, x1, y2, x1, y2, x1, y1};
// Canvas.drawLines(points, paint);
//    }
//
// Private void drawDot(Canvas canvas, Paint paint, float x1, float y1) {
// Canvas.drawCircle(x1, y1, DOT_RADIUS, paint);
//    }
//
// Private void setBitmap() {
// RegionBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(regionBitmap);
// For (int index = 0; index < points.size(); index++) {
//            Point tempPoint = points.get(index);
// DrawPoint(canvas, linePaint, tempPoint.x, tempPoint.y);
//        }
// For (int index = 0; index < lines.size(); index++) {
//            Line tempLine = lines.get(index);
// DrawLine(canvas, linePaint, tempLine.start.x, tempLine.start.y, tempLine.end.x, tempLine.end.y);
//        }
// For (int index = 0; index < rectangles.size(); index++) {
//            Rect tempRectangle = rectangles.get(index);
// DrawRectangle(canvas, linePaint, tempRectangle.left, tempRectangle.top, tempRectangle.right, tempRectangle.bottom);
//        }
//    }
//
// Public void start() {
// Runflag = true;
// TemperatureThread = new Thread(runnable);
// If (isShow) {
// SetVisibility(VISIBLE);
//        } else {
// SetVisibility(INVISIBLE);
//        }
// TemperatureThread.start();
//    }
//
// Public void pause() {
// Runflag = false;
// IsShow = getVisibility() == View.VISIBLE;
//    }
//
// Public void clear() {
// Try {
// Points.clear();
// Lines.clear();
// Rectangles.clear();
// If (regionBitmap != null){
// RegionBitmap.eraseColor(0);
//            }
//            Canvas surfaceViewCanvas = getHolder().lockCanvas();
// If (surfaceViewCanvas != null) {
// SurfaceViewCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
// SurfaceViewCanvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// GetHolder().unlockCanvasAndPost(surfaceViewCanvas);
//            }
//            // RegionAndValueBitmap.eraseColor(0);
//            // RegionBitmap.eraseColor(0);
//            // Canvas canvas = new Canvas(regionAndValueBitmap);
//            // Canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            // Canvas.drawBitmap(regionBitmap, new Rect(0, 0, viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), null);
// For (int i = 0; i < pointResultList.size(); i++) {
// PointResultList.get(i).index = 0;
//            }
// For (int i = 0; i < lineResultList.size(); i++) {
// LineResultList.get(i).index = 0;
//            }
// For (int i = 0; i < rectangleResultList.size(); i++) {
// RectangleResultList.get(i).index = 0;
//            }
//        }catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
// Public void stop() {
//        Log.w(TAG, "temperatureThread interrupt");
// Pause();
// TemperatureThread.interrupt();
// Try {
// TemperatureThread.join();
//        } catch (InterruptedException e) {
// E.printStackTrace();
//        }
//    }
//
// Public Bitmap getRegionAndValueBitmap() {
// Synchronized (regionLock) {
// Return regionAndValueBitmap;
//        }
//    }
//
// Public Bitmap getRegionBitmap() {
// Return regionAndValueBitmap;
//    }
//
//
//
// Public float getMaxTemperature() {
// Return maxTemperature;
//    }
//
// Public float getMinTemperature() {
// Return minTemperature;
//    }
//
// Public String getRectMinTemp() {
// If (rectangles.size() > 0) {
// Return RectMinTemp;
//        }
// Return "";
//    }
//
// Public void setRectMinTemp(String rectMinTemp) {
//        RectMinTemp = rectMinTemp;
//    }
//
// Public String getRectMaxTemp() {
// If (rectangles.size() > 0) {
// Return RectMaxTemp;
//        }
// Return "";
//    }
//
// Public void setRectMaxTemp(String rectMaxTemp) {
//        RectMaxTemp = rectMaxTemp;
//    }
//
// Public Point getPoint() {
// If (points.size() > 0) {
//            Point point = new Point();
// Point.x = (int) (points.get(0).x / xscale);
// Point.y = (int) (points.get(0).y / yscale);
// Return point;
//        } else {
// Return null;
//        }
//    }
//
// Public Line getLine() {
// If (lines.size() > 0) {
//            Line line = new Line(new Point(), new Point());
// Line.start.x = (int) (lines.get(0).start.x / xscale);
// Line.start.y = (int) (lines.get(0).start.y / yscale);
// Line.end.x = (int) (lines.get(0).end.x / xscale);
// Line.end.y = (int) (lines.get(0).end.y / yscale);
// Return line;
//        } else {
// Return null;
//        }
//    }
//
// Public Rect getRectangle() {
// If (rectangles.size() > 0) {
//            Rect rect = new Rect();
// Rect.left = (int) (rectangles.get(0).left / xscale);
// Rect.top = (int) (rectangles.get(0).top / yscale);
// Rect.right = (int) (rectangles.get(0).right / xscale);
// Rect.bottom = (int) (rectangles.get(0).bottom / yscale);
// Return rect;
//        } else {
// Return null;
//        }
//    }
//
// Public void addScalePoint(Point p) {
// // Float sx = viewWidth / 192f;
// // Float sy = viewHeight / 256f;
// Float sx = viewWidth / (float) imageWidth;
// Float sy = viewHeight / (float) imageHeight;
//        Point point = new Point();
// Point.x = (int) (p.x * sx);
// Point.y = (int) (p.y * sy);
// If (points.size() < POINT_MAX_COUNT) {
// Points.add(point);
//        } else {
// For (int index = 0; index < points.size() - 1; index++) {
//                Point tempPoint = points.get(index + 1);
// Points.set(index, tempPoint);
//            }
// Points.set(points.size() - 1, point);
//        }
//    }
//
// Public void addScaleLine(Line l) {
// // Float sx = viewWidth / 192f;
// // Float sy = viewHeight / 256f;
// Float sx = viewWidth / (float) imageWidth;
// Float sy = viewHeight / (float) imageHeight;
//        Line line = new Line(new Point(), new Point());
// Line.start.x = (int) (l.start.x * sx);
// Line.start.y = (int) (l.start.y * sy);
// Line.end.x = (int) (l.end.x * sx);
// Line.end.y = (int) (l.end.y * sy);
// If (lines.size() < LINE_MAX_COUNT) {
// Lines.add(line);
//        } else {
// For (int index = 0; index < lines.size() - 1; index++) {
//                Line tempLine = lines.get(index + 1);
// Lines.set(index, tempLine);
//            }
// Lines.set(lines.size() - 1, line);
//        }
//    }
//
// Public void addScaleRectangle(Rect r) {
// // Float sx = viewWidth / 192f;
// // Float sy = viewHeight / 256f;
// Float sx = viewWidth / (float) imageWidth;
// Float sy = viewHeight / (float) imageHeight;
//        Rect rectangle = new Rect();
// Rectangle.left = (int) (r.left * sx);
// Rectangle.top = (int) (r.top * sy);
// Rectangle.right = (int) (r.right * sx);
// Rectangle.bottom = (int) (r.bottom * sy);
// If (rectangles.size() < RECTANGLE_MAX_COUNT) {
// Rectangles.add(rectangle);
//        } else {
// For (int index = 0; index < rectangles.size() - 1; index++) {
//                Rect tempRectangle = rectangles.get(index + 1);
// Rectangles.set(index, tempRectangle);
//            }
// Rectangles.set(rectangles.size() - 1, rectangle);
//        }
//    }
//
// Public void drawLine() {
// // RegionBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
// // Float sx = viewWidth / 192f;
// // Float sy = viewHeight / 256f;
// //
// //        Log.w("123", "draw line w:" + viewWidth + ", h:" + viewHeight);
// //        Canvas canvas = new Canvas(regionBitmap);
// // For (int index = 0; index < points.size(); index++) {
// //            Point tempPoint = points.get(index);
// // DrawPoint(canvas, linePaint, tempPoint.x * sx, tempPoint.y * sy);
// //        }
// // For (int index = 0; index < lines.size(); index++) {
// //            Line tempLine = lines.get(index);
// // DrawLine(canvas, linePaint, tempLine.start.x * sx, tempLine.start.y * sy, tempLine.end.x * sx, tempLine.end.y * sy);
// //        }
// // For (int index = 0; index < rectangles.size(); index++) {
// //            Rect tempRectangle = rectangles.get(index);
// // DrawRectangle(canvas, linePaint, tempRectangle.left * sx, tempRectangle.top * sy, tempRectangle.right * sx, tempRectangle.bottom * sy);
// //        }
// SetBitmap();
//    }
//
// Public Canvas getTempCanvas(){
// Return regionAndValueCanvas;
//    }
//
// Public interface TempListener {
// Void getTemp(float max, float min);
//    }
// }