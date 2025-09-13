// Package com.infisense.usbir.thread;
//
// Import android.graphics.Bitmap;
// Import android.graphics.Color;
// Import android.os.SystemClock;
// Import android.util.Log;
//
// Import com.elvishew.xlog.XLog;
// Import com.infisense.iruvc.utils.SynchronizedBitmap;
// Import com.infisense.usbir.tools.BitmapTools;
// Import com.infisense.usbir.tools.ImageTools;
//
// Import java.nio.ByteBuffer;
//
// /**
// * bytes -> bitmap
// * 将源data转出imagephoto
// */
// Public class ImageThreadTCOld extends Thread {
//
// Private static final int TYPE_TINY1B = 1;
// Private static final int TYPE_TINY1C = 0;
// Private final String TAG = "ImageThread";
// Private Bitmap bitmap;
// Private SynchronizedBitmap syncImage;
// Private int imageWidth;
// Private int imageHeight;
// Private byte[] imageSrc;
// Private byte[] temperatureSrc;// Temperature源data
// Private int rotate = 0;
// Private float max = Float.MAX_VALUE;
// Private float min = Float.MIN_VALUE;
// Private int maxColor = 0;
// Private int minColor = 0;
//
// Public void setSyncImage(SynchronizedBitmap syncImage) {
// This.syncImage = syncImage;
//    }
//
// Public void setImageSrc(byte[] imageSrc) {
// This.imageSrc = imageSrc;
//    }
//
// Public void setTemperatureSrc(byte[] temperatureSrc) {
// This.temperatureSrc = temperatureSrc;
//    }
//
// Public void setRotate(int rotate) {
// This.rotate = rotate;
//    }
// Public byte[] imageDst = null;
//
//
// Public void setLimit(float max, float min) {
// This.max = max;
// This.min = min;
//    }
//
// Public void setLimit(float max, float min, int maxColor, int minColor) {
// This.max = max;
// This.min = min;
// This.maxColor = maxColor;
// This.minColor = minColor;
//    }
//
// Public int pseudoColorMode = Libirprocess.IRPROC_COLOR_MODE_0;
//
// Public ImageThreadTCOld(int imageWidth, int imageHeight) {
// This.imageWidth = imageWidth;
// This.imageHeight = imageHeight;
//    }
//
// Public void setPseudoColorMode(int pseudoColorMode) {
// This.pseudoColorMode = pseudoColorMode;
//    }
//
// Public void setBitmap(Bitmap bitmap) {
// This.bitmap = bitmap;
//    }
//
//    @Override
// Public void run() {
// Byte[] imagerTemp1 = new byte[imageWidth * imageHeight * 2];
// Byte[] imagerTemp2 = new byte[imageWidth * imageHeight * 4];
// ImageDst = new byte[imageWidth * imageHeight * 4];
// While (!isInterrupted()) {
//
// Synchronized (syncImage.dataLock) {
// If (syncImage.start) {
//
//                    // Uvc Width,Height
//
//                /*
// Imageprocess(imagerTemp1, imagerTemp2, imageRes);
//
// If(pseudocolorMode!=0) {
//                    Libirprocess.yuyv_map_to_argb_pseudocolor(imageSrc, imageHeight * imageWidth, pseudocolorMode, imageDst);
//                }else {
//                    Libirparse.yuv422_to_argb(imageSrc,imageHeight*imageWidth,imageDst);
//                }
//                 */
// If (pseudoColorMode != 0) {
//                        Libirprocess.yuyv_map_to_argb_pseudocolor(imageSrc, (long) imageHeight * imageWidth, pseudoColorMode, imageDst);
//                    } else {
//                        Libirparse.yuv422_to_argb(imageSrc, imageHeight * imageWidth, imageDst);
//                    }
//                    // Libirprocess.rotate_180(image,imageRes,Libirprocess.IRPROC_SRC_FMT_Y14,imager180);
//                    // Libirprocess.y14_map_to_yuyv_pseudocolor(imageSrc,imageHeight*imageWidth,Libirprocess.IRPROC_COLOR_MODE_3,imagerTemp2);
//
//                    // Libirparse.yuv422_to_argb(imager180,imageHeight*imageWidth,imagergb);
//
// If (syncImage.type == TYPE_TINY1B) {
//                        Libirparse.y14_to_yuv422(imageSrc, imageHeight * imageWidth, imagerTemp1);
//                        // Libirparse.yuv422_to_argb(imagerTemp2, imageHeight * imageWidth, imagerTemp1);
//                        // Libirprocess.y14_map_to_yuyv_pseudocolor(imageSrc,imageHeight*imageWidth,Libirprocess.IRPROC_COLOR_MODE_1,imagerTemp2);
//                        // Libirparse.yuv422_to_argb(imagerTemp2,imageHeight*imageWidth,imagerTemp1);
//                        // Libirparse.y14_to_argb(imageSrc, imageHeight * imageWidth, imagerTemp1);
//
//                    } else {
// ImagerTemp1 = imageSrc;
//                    }
//
// If (pseudoColorMode != 0) {
//                        Libirprocess.yuyv_map_to_argb_pseudocolor(imagerTemp1, (long) imageHeight * imageWidth, pseudoColorMode, imagerTemp2);
//                    } else {
//                        Libirparse.yuv422_to_argb(imagerTemp1, imageHeight * imageWidth, imagerTemp2);
//                    }
//
// //                    // ImagerTemp2二次processing (temperature原始data)
// // If (max != 0 && min != 0) {
// //                        ImageTools.INSTANCE.readFrame(imagerTemp2, temperatureSrc, max, min);
// //                    }
//
// If (rotate == 270) {
//                        Libirprocess.ImageRes_t imageRes = new Libirprocess.ImageRes_t();
// ImageRes.height = (char) imageWidth;
// ImageRes.width = (char) imageHeight;
//                        Libirprocess.rotate_right_90(imagerTemp2, imageRes, Libirprocess.IRPROC_SRC_FMT_ARGB8888, imageDst);
//                    } else if (rotate == 90) {
//                        Libirprocess.ImageRes_t imageRes = new Libirprocess.ImageRes_t();
// ImageRes.height = (char) imageWidth;
// ImageRes.width = (char) imageHeight;
//                        Libirprocess.rotate_left_90(imagerTemp2, imageRes, Libirprocess.IRPROC_SRC_FMT_ARGB8888, imageDst);
//                    } else if (rotate == 180) {
//                        Libirprocess.ImageRes_t imageRes = new Libirprocess.ImageRes_t();
// ImageRes.height = (char) imageHeight;
// ImageRes.width = (char) imageWidth;
//                        Libirprocess.rotate_180(imagerTemp2, imageRes, Libirprocess.IRPROC_SRC_FMT_ARGB8888, imageDst);
//                    } else {
// ImageDst = imagerTemp2;
//                    }
//                }
//            }
//
//            // JpegBytes = PixelFormatConverter.yuv422ToJpeg(pseudoImage, imageWidth, imageHeight);
//
//            // ImagerTemp2二次processing (temperaturerotation后data)
// If (max != Float.MAX_VALUE || min != Float.MIN_VALUE ) {
//                // 当不设高温，只settings低温时
// If (max == -273) {
//                    // 替换color的method里maximum温不能低于minimum温
// Max = 1000000;
//                }
//                // FF808080固定触发
// If (maxColor == Color.parseColor("#FF808080") && minColor == Color.parseColor("#FF808080")) {
//                    ImageTools.INSTANCE.readFrame(imageDst, temperatureSrc, max, min);// 替换grayscaleprocessing
//                } else {
//                    Log.w("123", "max:" + max + ", min: " + min);
// //                    ImageTools.INSTANCE.readFrame(imageDst, temperatureSrc, max, min,maxColor,minColor);// 替换colorprocessing
//                    BitmapTools.INSTANCE.replaceBitmapColor(imageDst, temperatureSrc, max, min,0,0);// 替换colorprocessing
//                }
//                Log.w("原始image:", imageDst.toString());
//            }
// Synchronized (syncImage.viewLock) {
// If (!syncImage.valid) {
// If (bitmap != null) {
// Bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(imageDst)); // Bitmapimagerefreshdata
//                    } else {
//                        XLog.e("ImageThreadTC copyPixelsFromBuffer(): bitmap is null");
//                    }
// SyncImage.valid = true;
//
// SyncImage.viewLock.notify();
//                }
//            }
// Try {
//                SystemClock.sleep(20);
//            } catch (Exception e) {
//                XLog.e("Image Threadrefreshexception: " + e.getMessage());
//            }
//        }
//        Log.w(TAG, "ImageThread exit:");
//    }
//
// Public Bitmap getBitmap() {
// Return bitmap;
//    }
//
// Private void imageprocess(byte[] src, byte[] dst, Libirprocess.ImageRes_t imageRes) {
// ImageRes.height = (char) imageHeight;
// ImageRes.width = (char) imageWidth;
//        Libirprocess.rotate_right_90(imageSrc, imageRes, Libirprocess.IPROC_SRC_FMT_YUV422, src);
// ImageRes.height = (char) imageWidth;
// ImageRes.width = (char) imageHeight;
//        Libirprocess.mirror(src, imageRes, Libirprocess.IRPROC_SRC_FMT_Y14, dst);
//    }
// }