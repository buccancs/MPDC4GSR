// Package com.infisense.usbir.camera;
//
// Import android.content.Context;
// Import android.hardware.usb.UsbDevice;
// Import android.os.SystemClock;
// Import android.util.Log;
//
// Import com.elvishew.xlog.XLog;
// Import com.infisense.iruvc.sdkisp.LibIRProcess;
// Import com.infisense.iruvc.sdkisp.Libircmd;
// Import com.infisense.iruvc.sdkisp.Libirprocess;
// Import com.infisense.iruvc.usb.DeviceFilter;
// Import com.infisense.iruvc.usb.IFrameCallback;
// Import com.infisense.iruvc.usb.USBMonitor;
// Import com.infisense.iruvc.usb.UVCCamera;
// Import com.infisense.iruvc.utils.CommonParams;
// Import com.infisense.iruvc.utils.SynchronizedBitmap;
// Import com.infisense.iruvc.uvc.ConnectCallback;
// Import com.infisense.usbir.R;
// Import com.infisense.usbir.config.MsgCode;
// Import com.infisense.usbir.event.IRMsgEvent;
// Import com.infisense.usbir.utils.USBMonitorCallback;
// Import com.topdon.lib.core.bean.event.device.DeviceCameraEvent;
// Import com.topdon.lib.core.bean.event.device.ResetConnectEvent;
//
// Import org.greenrobot.eventbus.EventBus;
//
// Import java.util.List;
//
// /**
// * device -> bytes
// * infrared出图核心工具class
// */
// Public class IRUVCTC {
//
// Private static final String TAG = "IRUVC";
// Private final int TinyB = 0x3901;
// Private final IFrameCallback iFrameCallback;
// Private final Context context;
// Public UVCCamera uvcCamera;
// Private USBMonitor mUSBMonitor;
// Private int cameraWidth;
// Private int cameraHeight;
// Private byte[] image;
// Private byte[] temperature;
// Private SynchronizedBitmap syncimage;
//    // DevicePID白名单
// Private int pids[] = {0x5840, 0x3901, 0x5830, 0x5838};
// Public boolean auto_gain_switch = false;
// Private boolean auto_over_portect = false;
//    /**
//     * 自动gainswitch
//     */
// Private LibIRProcess.AutoGainSwitchInfo_t auto_gain_switch_info = new LibIRProcess.AutoGainSwitchInfo_t();
// Private LibIRProcess.GainSwitchParam_t gain_switch_param = new LibIRProcess.GainSwitchParam_t();
// Private int count = 0;
// Private int rotate = 0;
// Long timeLog = 0L;// Record时间
//
// Private byte[] imageTemp = null;
// Private byte[] temperatureTemp = null;
// Private int countTemp = 0;
// Public byte[] imageEditTemp = null;
//    Long updateTime = 0L;
//
//    /**
//     * @param cameraHeight
//     * @param cameraWidth
//     * @param context
//     * @param syncimage
//     */
// Public IRUVCTC(int cameraHeight, int cameraWidth, Context context, SynchronizedBitmap syncimage,
//                   CommonParams.DataFlowMode dataFlowMode, boolean isUseIRISP, boolean isUseGPU,
//                   ConnectCallback connectCallback, USBMonitorCallback usbMonitorCallback) {
// This.mContext = context;
// This.syncimage = syncimage;
// This.isUseIRISP = isUseIRISP;
// This.isUseGPU = isUseGPU;
// This.mConnectCallback = connectCallback;
// This.defaultDataFlowMode = dataFlowMode;
// Init(cameraHeight, cameraWidth, context);
//
//
//        // 注意：USBMonitor的所有Callbackfunction都是运行在line程中的
// MUSBMonitor = new USBMonitor(context, new USBMonitor.OnDeviceConnectListener() {
//
//            // Called by checking usb device
//            // Do request device permission
//            @Override
// Public void onAttach(UsbDevice device) {
//                XLog.tag(TAG).w("onAttach");
// If (isIRpid(device.getProductId())) {
// If (uvcCamera == null || !uvcCamera.getOpenStatus()) {
// MUSBMonitor.requestPermission(device);
//                    }
//                }
//            }
//
//            @Override
// Public void onGranted(UsbDevice usbDevice, boolean b) {
//
//            }
//
//            // Called by connect to usb camera
//            // Do open camera,start previewing
//            @Override
// Public void onConnect(final UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
//                XLog.tag(TAG).w("onConnect");
// If (isIRpid(device.getProductId())) {
// If (createNew) {
// Open(ctrlBlock);
// Start();
//                    }
//                }
//                EventBus.getDefault().post(new ResetConnectEvent(1));
//            }
//
//            // Called by disconnect to usb camera
//            // Do nothing
//            @Override
// Public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
//                XLog.tag(TAG).w("onDisconnect");
//            }
//
//            // Called by taking out usb device
//            // Do close camera
//            @Override
// Public void onDettach(UsbDevice device) {
//                XLog.tag(TAG).w("onDetach");
// If (isIRpid(device.getProductId())) {
// If (uvcCamera != null && uvcCamera.getOpenStatus()) {
// Stop();
//                    }
//                }
//            }
//
//            @Override
// Public void onCancel(UsbDevice device) {
//                // 在usb permissionGet/Retrieve无效时触发
//                XLog.tag(TAG).w("onCancel");
//
//            }
//        });
//        // Auto gain switch parameter
// Gain_switch_param.above_pixel_prop = 0.1f;    // 用于high -> low gain,device像素总area积的百分比
// Gain_switch_param.above_temp_data = (int) ((130 + 273.15) * 16 * 4); // 用于high -> low gain,高gain向低gainswitch的触发temperature
// Gain_switch_param.below_pixel_prop = 0.95f;   // 用于low -> high gain,device像素总area积的百分比
// Gain_switch_param.below_temp_data = (int) ((110 + 273.15) * 16 * 4);// 用于low -> high gain,低gain向高gainswitch的触发temperature
// Auto_gain_switch_info.switch_frame_cnt = 5 * 15; // Continuous满足触发条件帧数超过该阈值会触发自动gainswitch(假设出图速度为15帧每秒，则5 * 15大概为5秒)
// Auto_gain_switch_info.waiting_frame_cnt = 7 * 15;// 触发自动gainswitch之后，会间隔该阈值的帧数不进行gainswitch监测(假设出图速度为15帧每秒，则7 * 15大概为7秒)
//        // Over_portect parameter
// Int low_gain_over_temp_data = (int) ((550 + 273.15) * 16 * 4);
// Int high_gain_over_temp_data = (int) ((100 + 273.15) * 16 * 4);
// Float pixel_above_prop = 0.02f;         // 0-1
//
//        // Listener读取deviceinfrareddata
// IFrameCallback = frame -> {
//            Log.d(TAG, "frame: " + "refresh："+(System.currentTimeMillis()-updateTime));
// UpdateTime = System.currentTimeMillis();
//            // Test帧率，可以根据实际需要决定是否保留
// If (count++ >= 25) {
// Count = 1;
//                Log.d(TAG, "frame: " + frame.length);
//            }
// If (syncimage == null) return;
// Syncimage.start = true;
// Synchronized (syncimage.dataLock) {
//                // 判断坏帧，出现坏帧则重启sensor
// Int length = frame.length - 1;
// If (frame[length] == 1) {
//                    EventBus.getDefault().post(new IRMsgEvent(MsgCode.RESTART_USB));
//                    XLog.tag(TAG).i("RESTART_USB");
// Return;
//                }
//                /**
//                 * copyinfrareddata到imagearray中
//                 * 出图的framearray中前半部分是infrareddata，后半部分是temperaturedata，
//                 * 例如256*384分辨率的device，前area的256*192是infrareddata，后area的256*192是temperaturedata，
//                 * 其中的data是rotation90度的，需要rotation回来。
//                 */
// If (imageEditTemp != null && imageEditTemp.length >= length) {
//                    // 部分场景不需要saved帧data
//                    System.arraycopy(frame, 0, imageEditTemp, 0, length);
//                }
//                System.arraycopy(frame, 0, image, 0, length / 2);
//                Libirprocess.ImageRes_t imageRes = new Libirprocess.ImageRes_t();
// ImageRes.height = (char) (cameraHeight / 2);
// ImageRes.width = (char) cameraWidth;
// //                Libirprocess.rotate_right_90(frame, imageRes, Libirprocess.IRPROC_SRC_FMT_Y14, imageEditTemp);
// //                // Get/Retrieve原始temperaturedata
// //                System.arraycopy(frame, length / 2, temperatureSrc, 0, length / 2);
//
// //                // SavedTestdata
// // CountTemp++;
// // If (countTemp == 100) {
// // ImageTemp = new byte[length / 2];
// // TemperatureTemp = new byte[length / 2];
// //
// //                    System.arraycopy(frame, 0, imageTemp, 0, length / 2);
// //                    XLog.tag("ahh").i("imageTemp: " + ByteUtils.INSTANCE.toHexString(imageTemp, " "));
// //
// //                    System.arraycopy(frame, length / 2, temperatureTemp, 0, length / 2);
// //                    XLog.tag("ahh").i("temperatureTemp: " + ByteUtils.INSTANCE.toHexString(temperatureTemp, " "));
// //                }
//
// If (rotate == 270) {
//                    // 270
// Byte[] temp = new byte[length / 2];
//                    System.arraycopy(frame, length / 2, temp, 0, length / 2);
//                    Libirprocess.rotate_right_90(temp, imageRes, Libirprocess.IRPROC_SRC_FMT_Y14, temperature);
//                } else if (rotate == 90) {
//                    // 90
// Byte[] temp = new byte[length / 2];
//                    System.arraycopy(frame, length / 2, temp, 0, length / 2);
//                    Libirprocess.rotate_left_90(temp, imageRes, Libirprocess.IRPROC_SRC_FMT_Y14, temperature);
//                } else if (rotate == 180) {
//                    // 180
// Byte[] temp = new byte[length / 2];
//                    System.arraycopy(frame, length / 2, temp, 0, length / 2);
//                    Libirprocess.rotate_180(temp, imageRes, Libirprocess.IRPROC_SRC_FMT_Y14, temperature);
//                } else {
//                    // 0
//                    System.arraycopy(frame, length / 2, temperature, 0, length / 2);
//                }
//                // 自动gainswitch，不effective的话请您的device是否支持自动gainswitch
// If (auto_gain_switch) {
//                    Libircmd.auto_gain_switch(temperature, imageRes, auto_gain_switch_info, gain_switch_param, uvcCamera.nativePtr);
//                }
//                // 防灼烧保护
// If (auto_over_portect) {
//                    Libircmd.avoid_overexposure(temperature, imageRes, low_gain_over_temp_data,
// High_gain_over_temp_data, pixel_above_prop, 15 * 25, uvcCamera.nativePtr);
//                }
//            }
//        };
//    }
//
//    /**
//     * @param rotate
//     */
// Public void setRotate(int rotate) {
// This.rotate = rotate;
//    }
//
//    /**
//     * @param image
//     */
// Public void setImage(byte[] image) {
// This.image = image;
//    }
//
//    /**
//     * @param temperature
//     */
// Public void setTemperature(byte[] temperature) {
// This.temperature = temperature;
//    }
//
// Public void setImageEditSrc(byte[] imageEditTemp) {
// This.imageEditTemp = imageEditTemp;
//    }
//
//    /**
//     * 判断是否是infrareddevice，请把您的device的PIDadd进devicePID白名单
//     *
//     * @param devpid
//     * @return
//     */
// Private boolean isIRpid(int devpid) {
// For (int x : pids) {
// If (x == devpid) return true;
//        }
// Return false;
//    }
//
//    /**
//     * @param cameraHeight
//     * @param cameraWidth
//     * @param context
//     */
// Public void init(int cameraHeight, int cameraWidth, Context context) {
//        XLog.tag(TAG).w("init");
// UvcCamera = new UVCCamera(cameraWidth, cameraHeight, context);
// UvcCamera.create();
//        EventBus.getDefault().post(new DeviceCameraEvent(100));
//    }
//
//    /**
//     *
//     */
// Public void registerUSB() {
// If (mUSBMonitor != null) {
// MUSBMonitor.register();
//        }
//    }
//
//    /**
//     *
//     */
// Public void unregisterUSB() {
// If (mUSBMonitor != null) {
// MUSBMonitor.unregister();
//        }
//    }
//
//    /**
//     * @return
//     */
// Public List<UsbDevice> getUsbDeviceList() {
// //        List<DeviceFilter> deviceFiltersTemp = DeviceFilter.getDeviceFilters(context, R.xml.device_filter);
//        List<DeviceFilter> deviceFilters = DeviceFilter.getDeviceFilters(context, R.xml.ir_device_filter);
// If (mUSBMonitor == null || deviceFilters == null)
// // Throw new NullPointerException("mUSBMonitor ="+mUSBMonitor+"deviceFilters=;"+deviceFilters);
// Return null;
//        // Matching all of filter devices
// Return mUSBMonitor.getDeviceList(deviceFilters);
//    }
//
//    /**
//     * @param index
//     */
// Public void requestPermission(int index) {
//        List<UsbDevice> devList = getUsbDeviceList();
// If (devList == null || devList.size() == 0) {
// Return;
//        }
// Int count = devList.size();
// If (index >= count)
// New IllegalArgumentException("index illegal,should be < devList.size()");
// If (mUSBMonitor != null) {
// MUSBMonitor.requestPermission(getUsbDeviceList().get(index));
//        }
//    }
//
//    /**
//     * @param ctrlBlock
//     */
// Public void open(USBMonitor.UsbControlBlock ctrlBlock) {
// If (ctrlBlock.getProductId() == TinyB) {
// If (syncimage != null) {
// Syncimage.type = 1;
//            }
//        }
// If (uvcCamera == null) {
// Init(cameraHeight, cameraWidth, context);
//        }
// UvcCamera.open(ctrlBlock);
//    }
//
//    /**
//     *
//     */
// Public void start() {
// Try {
//            XLog.tag(TAG).w("start");
// UvcCamera.setOpenStatus(true);
// UvcCamera.setFrameCallback(iFrameCallback); // RegisterListenerEvent
//            // UvcCamera.setgetframemode(uvcCamera.GET_FRAME_ASYNC);
//            // Default sync mode for some devices  Lost-Packet
//            // UvcCamera.DEFAULT_BANDWIDTH=0.3f;// Hub
// UvcCamera.startPreview(); // Start读取data
// New Thread(() -> {
// Try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
// E.printStackTrace();
//                }
//                EventBus.getDefault().post(new DeviceCameraEvent(101));
//                // 打快门
// If (uvcCamera != null) {
// If (syncimage.type == 1) {
//                        Libircmd.tiny1b_shutter_manual(uvcCamera.nativePtr);
//                    } else {
//                        // 源码settings快门
//                        Libircmd.ooc_b_update(Libircmd.B_UPDATE, uvcCamera.nativePtr);
//                    }
//                }
//            }).start();
//        }catch (Exception e){
//            Log.w("infraredsdkexception", e.getMessage());
//        }
//
//    }
//
//    /**
//     *
//     */
// Public void stop() {
//        XLog.tag(TAG).w("stop");
// // If (uvcCamera != null) {
// // If (uvcCamera.getOpenStatus()) {
// // UvcCamera.stopPreview();
// //            }
// // Final UVCCamera camera;
// // Camera = uvcCamera;
// // UvcCamera = null;
// //            SystemClock.sleep(200);
// // Camera.destroy();
// //            EventBus.getDefault().post(new ResetConnectEvent(3));
// //        }
//    }
//
// //    Disposable disposable = null;
// // Private boolean isRun = false;
// //
// // Private void monitor() {
// // If (disposable != null) {
// // Disposable.dispose();
// //        }
// // Disposable = Observable.interval(1L, TimeUnit.SECONDS).take(1000)
// //                .subscribeOn(Schedulers.io())
// //                .subscribe(aLong -> {
// //                    Log.w("123", "aLong" + aLong);
// /// /                    if (isRun) {
// /// /                        if (timeLog != 0 && System.currentTimeMillis() - timeLog > 1000) {
// /// /                            // Notification超时
// /// /                            EventBus.getDefault().post(new DeviceConnectEvent(false, null));
// /// /                            XLog.w("超过1s没data采集,Exit界area");
// /// ///                ToastTools.INSTANCE.showShort("超过1s没data采集,Exit界area");
// /// /                        }
// /// /                        timeLog = System.currentTimeMillis();
// /// /                    }
// //                });
// //        Log.w("123", "Observable.timer");
// //    }
// //
// // Private void cancelMonitor() {
// // IsRun = false;
// // If (disposable != null) {
// // Disposable.dispose();
// //        }
// //    }
//
// }
