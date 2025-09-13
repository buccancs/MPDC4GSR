// Package com.infisense.usbir.thread;
//
// Import android.graphics.Bitmap;
// Import android.os.SystemClock;
// Import android.util.Log;
//
// Import com.infisense.iruvc.sdkisp.Libirparse;
// Import com.infisense.iruvc.sdkisp.Libirprocess;
// Import com.infisense.iruvc.utils.SynchronizedBitmap;
//
// Import java.nio.ByteBuffer;
//
// Public class ImageThread extends Thread {
//
// Private static final int TYPE_TINY1B = 1;
// Private static final int TYPE_TINY1C = 0;
// Private String TAG = "ImageThread";
// Private Bitmap bitmap;
// Private SynchronizedBitmap syncImage;
// Private int imageWidth;
// Private int imageHeight;
// Private byte[] imageSrc;
// Private boolean rotate = false;
//
// Public void setSyncimage(SynchronizedBitmap syncimage) {
// This.syncImage = syncimage;
//    }
//
// Public void setImageSrc(byte[] imageSrc) {
// This.imageSrc = imageSrc;
//    }
//
// Public void setRotate(boolean rotate) {
// This.rotate = rotate;
//    }
//
//
// Public int pseudocolorMode = Libirprocess.IRPROC_COLOR_MODE_0;
//
// Public ImageThread(int imageWidth, int imageHeight) {
// This.imageWidth = imageWidth;
// This.imageHeight = imageHeight;
//    }
//
// Public void setPseudocolorMode(int pseudocolorMode) {
// This.pseudocolorMode = pseudocolorMode;
//    }
//
// Public void setBitmap(Bitmap bitmap) {
// This.bitmap = bitmap;
//    }
//
//    @Override
// Public void run() {
// Byte[] imagertemp1 = new byte[imageWidth * imageHeight * 2];
// Byte[] imagertemp2 = new byte[imageWidth * imageHeight * 4];
// Byte[] imagedst = new byte[imageWidth * imageHeight * 4];
// While (!isInterrupted()) {
//
// Synchronized (syncImage.dataLock) {
// If (syncImage.start) {
//
//                    // Uvc Width,Height
//
//                /*
// Imageprocess(imagertemp1, imagertemp2, imageRes);
//
// If(pseudocolorMode!=0) {
//                    Libirprocess.yuyv_map_to_argb_pseudocolor(imageSrc, imageHeight * imageWidth, pseudocolorMode, imagedst);
//                }else {
//                    Libirparse.yuv422_to_argb(imageSrc,imageHeight*imageWidth,imagedst);
//                }
//                 */
// If (pseudocolorMode != 0) {
//                        Libirprocess.yuyv_map_to_argb_pseudocolor(imageSrc, imageHeight * imageWidth, pseudocolorMode, imagedst);
//                    } else {
//                        Libirparse.yuv422_to_argb(imageSrc, imageHeight * imageWidth, imagedst);
//                    }
//                    // Libirprocess.rotate_180(image,imageRes,Libirprocess.IRPROC_SRC_FMT_Y14,imager180);
//                    // Libirprocess.y14_map_to_yuyv_pseudocolor(imageSrc,imageHeight*imageWidth,Libirprocess.IRPROC_COLOR_MODE_3,imagertemp2);
//
//                    // Libirparse.yuv422_to_argb(imager180,imageHeight*imageWidth,imagergb);
//
// If (syncImage.type == TYPE_TINY1B) {
//                        Libirparse.y14_to_yuv422(imageSrc, imageHeight * imageWidth, imagertemp1);
//                        // Libirparse.yuv422_to_argb(imagertemp2, imageHeight * imageWidth, imagertemp1);
//                        // Libirprocess.y14_map_to_yuyv_pseudocolor(imageSrc,imageHeight*imageWidth,Libirprocess.IRPROC_COLOR_MODE_1,imagertemp2);
//                        // Libirparse.yuv422_to_argb(imagertemp2,imageHeight*imageWidth,imagertemp1);
//                        // Libirparse.y14_to_argb(imageSrc, imageHeight * imageWidth, imagertemp1);
//
//                    } else {
// Imagertemp1 = imageSrc;
//                    }
//
// If (pseudocolorMode != 0) {
//                        Libirprocess.yuyv_map_to_argb_pseudocolor(imagertemp1, imageHeight * imageWidth, pseudocolorMode, imagertemp2);
//                    } else {
//                        Libirparse.yuv422_to_argb(imagertemp1, imageHeight * imageWidth, imagertemp2);
//                    }
//
// If (rotate) {
//                        Libirprocess.ImageRes_t imageRes = new Libirprocess.ImageRes_t();
// ImageRes.height = (char) imageWidth;
// ImageRes.width = (char) imageHeight;
//                        Libirprocess.rotate_right_90(imagertemp2, imageRes, Libirprocess.IRPROC_SRC_FMT_ARGB8888, imagedst);
//                    } else imagedst = imagertemp2;
//                }
//            }
//
//            // JpegBytes = PixelFormatConverter.yuv422ToJpeg(pseudoImage, imageWidth, imageHeight);
//
// Synchronized (syncImage.viewLock) {
// If (syncImage.valid == false) {
//                    // Bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
//                    // Bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pseudoImage));
// Bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(imagedst));
// SyncImage.valid = true;
// SyncImage.viewLock.notify();
//                }
//            }
//            SystemClock.sleep(20);
//        }
//        Log.w(TAG, "ImageThread exit:");
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