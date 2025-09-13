// Package com.infisense.iruvc.usb;
//
// Import android.annotation.SuppressLint;
// Import android.annotation.TargetApi;
// Import android.app.PendingIntent;
// Import android.content.BroadcastReceiver;
// Import android.content.Context;
// Import android.content.Intent;
// Import android.content.IntentFilter;
// Import android.hardware.usb.UsbDevice;
// Import android.hardware.usb.UsbDeviceConnection;
// Import android.hardware.usb.UsbInterface;
// Import android.hardware.usb.UsbManager;
// Import android.os.Build;
// Import android.os.Handler;
// Import android.text.TextUtils;
// Import android.util.Log;
// Import android.util.SparseArray;
//
// Import com.infisense.iruvc.utils.BuildCheck;
// Import com.infisense.iruvc.utils.HandlerThreadHandler;
//
// Import java.io.UnsupportedEncodingException;
// Import java.lang.ref.WeakReference;
// Import java.util.ArrayList;
// Import java.util.HashMap;
// Import java.util.Iterator;
// Import java.util.List;
// Import java.util.Locale;
// Import java.util.Set;
// Import java.util.concurrent.ConcurrentHashMap;
//
// /**
// * 替换libusbirsdk_1.2.0.aarclass,为兼容android 12
// */
// Public class USBMonitor {
// Private static final boolean DEBUG = false;
// Private static final String TAG = "USBMonitor";
// Private static final String ACTION_USB_PERMISSION_BASE = "com.serenegiant.USB_PERMISSION.";
// Private final String ACTION_USB_PERMISSION = "com.serenegiant.USB_PERMISSION." + this.hashCode();
// Public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
// Private final ConcurrentHashMap<UsbDevice, UsbControlBlock> mCtrlBlocks = new ConcurrentHashMap();
// Private final SparseArray<WeakReference<UsbDevice>> mHasPermissions = new SparseArray();
// Private final WeakReference<Context> mWeakContext;
// Private final UsbManager mUsbManager;
// Private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener;
// Private PendingIntent mPermissionIntent = null;
// Private List<DeviceFilter> mDeviceFilters = new ArrayList();
// Private final Handler mAsyncHandler;
// Private volatile boolean destroyed;
// Private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
// Public void onReceive(Context context, Intent intent) {
// If (!USBMonitor.this.destroyed) {
//                String action = intent.getAction();
// If (USBMonitor.this.ACTION_USB_PERMISSION.equals(action)) {
// Synchronized (USBMonitor.this) {
//                        UsbDevice devicex = (UsbDevice) intent.getParcelableExtra("device");
// If (intent.getBooleanExtra("permission", false)) {
// If (devicex != null) {
//                                USBMonitor.this.processConnect(devicex);
//                            }
//                        } else {
//                            USBMonitor.this.processCancel(devicex);
//                        }
//                    }
//                } else {
//                    UsbDevice device;
// If ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
// Device = (UsbDevice) intent.getParcelableExtra("device");
//                        USBMonitor.this.updatePermission(device, USBMonitor.this.hasPermission(device));
//                        USBMonitor.this.processAttach(device);
//                    } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
// Device = (UsbDevice) intent.getParcelableExtra("device");
// If (device != null) {
//                            USBMonitor.UsbControlBlock ctrlBlock = (USBMonitor.UsbControlBlock) USBMonitor.this.mCtrlBlocks.remove(device);
// If (ctrlBlock != null) {
// CtrlBlock.close();
//                            }
//
//                            USBMonitor.this.mDeviceCounts = 0;
//                            USBMonitor.this.processDettach(device);
//                        }
//                    }
//                }
//
//            }
//        }
//    };
// Private volatile int mDeviceCounts = 0;
// Private final Runnable mDeviceCheckRunnable = new Runnable() {
// Public void run() {
// If (!USBMonitor.this.destroyed) {
//                List<UsbDevice> devices = USBMonitor.this.getDeviceList();
// Int n = devices.size();
// Int hasPermissionCounts;
// Int m;
// Synchronized (USBMonitor.this.mHasPermissions) {
// HasPermissionCounts = USBMonitor.this.mHasPermissions.size();
//                    USBMonitor.this.mHasPermissions.clear();
//                    Iterator var6 = devices.iterator();
//
// While (true) {
// If (!var6.hasNext()) {
// M = USBMonitor.this.mHasPermissions.size();
// Break;
//                        }
//
//                        UsbDevice device = (UsbDevice) var6.next();
//                        USBMonitor.this.hasPermission(device);
//                    }
//                }
//
// If (n > USBMonitor.this.mDeviceCounts || m > hasPermissionCounts) {
//                    USBMonitor.this.mDeviceCounts = n;
// If (USBMonitor.this.mOnDeviceConnectListener != null) {
// For (int i = 0; i < n; ++i) {
// Final UsbDevice devicex = (UsbDevice) devices.get(i);
//                            USBMonitor.this.mAsyncHandler.post(new Runnable() {
// Public void run() {
//                                    USBMonitor.this.mOnDeviceConnectListener.onAttach(devicex);
//                                }
//                            });
//                        }
//                    }
//                }
//
//                USBMonitor.this.mAsyncHandler.postDelayed(this, 2000L);
//            }
//        }
//    };
// Private static final int USB_DIR_OUT = 0;
// Private static final int USB_DIR_IN = 128;
// Private static final int USB_TYPE_MASK = 96;
// Private static final int USB_TYPE_STANDARD = 0;
// Private static final int USB_TYPE_CLASS = 32;
// Private static final int USB_TYPE_VENDOR = 64;
// Private static final int USB_TYPE_RESERVED = 96;
// Private static final int USB_RECIP_MASK = 31;
// Private static final int USB_RECIP_DEVICE = 0;
// Private static final int USB_RECIP_INTERFACE = 1;
// Private static final int USB_RECIP_ENDPOINT = 2;
// Private static final int USB_RECIP_OTHER = 3;
// Private static final int USB_RECIP_PORT = 4;
// Private static final int USB_RECIP_RPIPE = 5;
// Private static final int USB_REQ_GET_STATUS = 0;
// Private static final int USB_REQ_CLEAR_FEATURE = 1;
// Private static final int USB_REQ_SET_FEATURE = 3;
// Private static final int USB_REQ_SET_ADDRESS = 5;
// Private static final int USB_REQ_GET_DESCRIPTOR = 6;
// Private static final int USB_REQ_SET_DESCRIPTOR = 7;
// Private static final int USB_REQ_GET_CONFIGURATION = 8;
// Private static final int USB_REQ_SET_CONFIGURATION = 9;
// Private static final int USB_REQ_GET_INTERFACE = 10;
// Private static final int USB_REQ_SET_INTERFACE = 11;
// Private static final int USB_REQ_SYNCH_FRAME = 12;
// Private static final int USB_REQ_SET_SEL = 48;
// Private static final int USB_REQ_SET_ISOCH_DELAY = 49;
// Private static final int USB_REQ_SET_ENCRYPTION = 13;
// Private static final int USB_REQ_GET_ENCRYPTION = 14;
// Private static final int USB_REQ_RPIPE_ABORT = 14;
// Private static final int USB_REQ_SET_HANDSHAKE = 15;
// Private static final int USB_REQ_RPIPE_RESET = 15;
// Private static final int USB_REQ_GET_HANDSHAKE = 16;
// Private static final int USB_REQ_SET_CONNECTION = 17;
// Private static final int USB_REQ_SET_SECURITY_DATA = 18;
// Private static final int USB_REQ_GET_SECURITY_DATA = 19;
// Private static final int USB_REQ_SET_WUSB_DATA = 20;
// Private static final int USB_REQ_LOOPBACK_DATA_WRITE = 21;
// Private static final int USB_REQ_LOOPBACK_DATA_READ = 22;
// Private static final int USB_REQ_SET_INTERFACE_DS = 23;
// Private static final int USB_REQ_STANDARD_DEVICE_SET = 0;
// Private static final int USB_REQ_STANDARD_DEVICE_GET = 128;
// Private static final int USB_REQ_STANDARD_INTERFACE_SET = 1;
// Private static final int USB_REQ_STANDARD_INTERFACE_GET = 129;
// Private static final int USB_REQ_STANDARD_ENDPOINT_SET = 2;
// Private static final int USB_REQ_STANDARD_ENDPOINT_GET = 130;
// Private static final int USB_REQ_CS_DEVICE_SET = 32;
// Private static final int USB_REQ_CS_DEVICE_GET = 160;
// Private static final int USB_REQ_CS_INTERFACE_SET = 33;
// Private static final int USB_REQ_CS_INTERFACE_GET = 161;
// Private static final int USB_REQ_CS_ENDPOINT_SET = 34;
// Private static final int USB_REQ_CS_ENDPOINT_GET = 162;
// Private static final int USB_REQ_VENDER_DEVICE_SET = 32;
// Private static final int USB_REQ_VENDER_DEVICE_GET = 160;
// Private static final int USB_REQ_VENDER_INTERFACE_SET = 33;
// Private static final int USB_REQ_VENDER_INTERFACE_GET = 161;
// Private static final int USB_REQ_VENDER_ENDPOINT_SET = 34;
// Private static final int USB_REQ_VENDER_ENDPOINT_GET = 162;
// Private static final int USB_DT_DEVICE = 1;
// Private static final int USB_DT_CONFIG = 2;
// Private static final int USB_DT_STRING = 3;
// Private static final int USB_DT_INTERFACE = 4;
// Private static final int USB_DT_ENDPOINT = 5;
// Private static final int USB_DT_DEVICE_QUALIFIER = 6;
// Private static final int USB_DT_OTHER_SPEED_CONFIG = 7;
// Private static final int USB_DT_INTERFACE_POWER = 8;
// Private static final int USB_DT_OTG = 9;
// Private static final int USB_DT_DEBUG = 10;
// Private static final int USB_DT_INTERFACE_ASSOCIATION = 11;
// Private static final int USB_DT_SECURITY = 12;
// Private static final int USB_DT_KEY = 13;
// Private static final int USB_DT_ENCRYPTION_TYPE = 14;
// Private static final int USB_DT_BOS = 15;
// Private static final int USB_DT_DEVICE_CAPABILITY = 16;
// Private static final int USB_DT_WIRELESS_ENDPOINT_COMP = 17;
// Private static final int USB_DT_WIRE_ADAPTER = 33;
// Private static final int USB_DT_RPIPE = 34;
// Private static final int USB_DT_CS_RADIO_CONTROL = 35;
// Private static final int USB_DT_PIPE_USAGE = 36;
// Private static final int USB_DT_SS_ENDPOINT_COMP = 48;
// Private static final int USB_DT_CS_DEVICE = 33;
// Private static final int USB_DT_CS_CONFIG = 34;
// Private static final int USB_DT_CS_STRING = 35;
// Private static final int USB_DT_CS_INTERFACE = 36;
// Private static final int USB_DT_CS_ENDPOINT = 37;
// Private static final int USB_DT_DEVICE_SIZE = 18;
//
// Public USBMonitor(Context context, USBMonitor.OnDeviceConnectListener listener) {
// If (listener == null) {
// Throw new IllegalArgumentException("OnDeviceConnectListener should not null.");
//        } else {
// This.mWeakContext = new WeakReference(context);
// This.mUsbManager = (UsbManager) context.getSystemService("usb");
// This.mOnDeviceConnectListener = listener;
// This.mAsyncHandler = HandlerThreadHandler.createHandler("USBMonitor");
// This.destroyed = false;
//        }
//    }
//
// Public void destroy() {
// This.unregister();
// If (!this.destroyed) {
// This.destroyed = true;
//            Set<UsbDevice> keys = this.mCtrlBlocks.keySet();
// If (keys != null) {
// Try {
//                    Iterator var3 = keys.iterator();
//
// While (var3.hasNext()) {
//                        UsbDevice key = (UsbDevice) var3.next();
//                        USBMonitor.UsbControlBlock ctrlBlock = (USBMonitor.UsbControlBlock) this.mCtrlBlocks.remove(key);
// If (ctrlBlock != null) {
// CtrlBlock.close();
//                        }
//                    }
//                } catch (Exception var6) {
//                    Log.e("USBMonitor", "destroy:", var6);
//                }
//            }
//
// This.mCtrlBlocks.clear();
//
// Try {
// This.mAsyncHandler.getLooper().quit();
//            } catch (Exception var5) {
//                Log.e("USBMonitor", "destroy:", var5);
//            }
//        }
//
//    }
//
// Public synchronized void register() throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// If (this.mPermissionIntent == null) {
//                Context context = (Context) this.mWeakContext.get();
// If (context != null) {
//                    // Fixed: Android 12+ PendingIntent compatibility - properly handle FLAG_IMMUTABLE requirement
// Int flag;
// If (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                        // Android 12+ requires FLAG_IMMUTABLE for explicit intents to prevent crashes
// Flag = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
//                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        // Android 6+ supports FLAG_IMMUTABLE but doesn't require it
// Flag = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
//                    } else {
//                        // Pre-Android 6 doesn't support FLAG_IMMUTABLE
// Flag = PendingIntent.FLAG_UPDATE_CURRENT;
//                    }
// This.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(this.ACTION_USB_PERMISSION), flag);
//                    IntentFilter filter = new IntentFilter(this.ACTION_USB_PERMISSION);
// Filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
// Filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
// Context.registerReceiver(this.mUsbReceiver, filter);
//                }
//
// This.mDeviceCounts = 0;
// This.mAsyncHandler.postDelayed(this.mDeviceCheckRunnable, 1000L);
//            }
//
//        }
//    }
//
// Public synchronized void unregister() throws IllegalStateException {
// This.mDeviceCounts = 0;
// If (!this.destroyed) {
// This.mAsyncHandler.removeCallbacks(this.mDeviceCheckRunnable);
//        }
//
// If (this.mPermissionIntent != null) {
//            Context context = (Context) this.mWeakContext.get();
//
// Try {
// If (context != null) {
// Context.unregisterReceiver(this.mUsbReceiver);
//                }
//            } catch (Exception var3) {
//                Log.w("USBMonitor", var3);
//            }
//
// This.mPermissionIntent = null;
//        }
//
//    }
//
// Public synchronized boolean isRegistered() {
// Return !this.destroyed && this.mPermissionIntent != null;
//    }
//
// Public void setDeviceFilter(DeviceFilter filter) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.clear();
// This.mDeviceFilters.add(filter);
//        }
//    }
//
// Public void addDeviceFilter(DeviceFilter filter) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.add(filter);
//        }
//    }
//
// Public void removeDeviceFilter(DeviceFilter filter) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.remove(filter);
//        }
//    }
//
// Public void setDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.clear();
// This.mDeviceFilters.addAll(filters);
//        }
//    }
//
// Public void addDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.addAll(filters);
//        }
//    }
//
// Public void removeDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// This.mDeviceFilters.removeAll(filters);
//        }
//    }
//
// Public int getDeviceCount() throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// Return this.getDeviceList().size();
//        }
//    }
//
// Public List<UsbDevice> getDeviceList() throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// Return this.getDeviceList(this.mDeviceFilters);
//        }
//    }
//
// Public List<UsbDevice> getDeviceList(List<DeviceFilter> filters) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
//            HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
//            List<UsbDevice> result = new ArrayList();
// If (deviceList != null) {
// If (filters != null && !filters.isEmpty()) {
//                    Iterator var4 = deviceList.values().iterator();
//
// Label42:
// While (var4.hasNext()) {
//                        UsbDevice device = (UsbDevice) var4.next();
//                        Iterator var6 = filters.iterator();
//
// While (true) {
//                            DeviceFilter filter;
// Do {
// If (!var6.hasNext()) {
// Continue label42;
//                                }
//
// Filter = (DeviceFilter) var6.next();
//                            } while ((filter == null || !filter.matches(device)) && (filter == null || filter.mSubclass != device.getDeviceSubclass()));
//
// If (!filter.isExclude) {
// Result.add(device);
//                            }
//                        }
//                    }
//                } else {
// Result.addAll(deviceList.values());
//                }
//            }
//
// Return result;
//        }
//    }
//
// Public List<UsbDevice> getDeviceList(DeviceFilter filter) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
//            HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
//            List<UsbDevice> result = new ArrayList();
// If (deviceList != null) {
//                Iterator var4 = deviceList.values().iterator();
//
// While (true) {
//                    UsbDevice device;
// Do {
// If (!var4.hasNext()) {
// Return result;
//                        }
//
// Device = (UsbDevice) var4.next();
//                    } while (filter != null && (!filter.matches(device) || filter.isExclude));
//
// Result.add(device);
//                }
//            } else {
// Return result;
//            }
//        }
//    }
//
// Public Iterator<UsbDevice> getDevices() throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
//            Iterator<UsbDevice> iterator = null;
//            HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
// If (list != null) {
// Iterator = list.values().iterator();
//            }
//
// Return iterator;
//        }
//    }
//
// Public final void dumpDevices() {
//        HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
// If (list != null) {
//            Set<String> keys = list.keySet();
// If (keys != null && keys.size() > 0) {
//                StringBuilder sb = new StringBuilder();
//                Iterator var4 = keys.iterator();
//
// While (var4.hasNext()) {
//                    String key = (String) var4.next();
//                    UsbDevice device = (UsbDevice) list.get(key);
// Int num_interface = device != null ? device.getInterfaceCount() : 0;
// Sb.setLength(0);
//
// For (int i = 0; i < num_interface; ++i) {
// Sb.append(String.format(Locale.US, "interface%d:%s", i, device.getInterface(i).toString()));
//                    }
//
//                    Log.i("USBMonitor", "key=" + key + ":" + device + ":" + sb.toString());
//                }
//            } else {
//                Log.i("USBMonitor", "no device");
//            }
//        } else {
//            Log.i("USBMonitor", "no device");
//        }
//
//    }
//
// Public final boolean hasPermission(UsbDevice device) throws IllegalStateException {
// If (this.destroyed) {
// Throw new IllegalStateException("already destroyed");
//        } else {
// Return this.updatePermission(device, device != null && this.mUsbManager.hasPermission(device));
//        }
//    }
//
// Private boolean updatePermission(UsbDevice device, boolean hasPermission) {
// Int deviceKey = getDeviceKey(device, true);
// Synchronized (this.mHasPermissions) {
// If (hasPermission) {
// If (this.mHasPermissions.get(deviceKey) == null) {
// This.mHasPermissions.put(deviceKey, new WeakReference(device));
//                }
//            } else {
// This.mHasPermissions.remove(deviceKey);
//            }
//
// Return hasPermission;
//        }
//    }
//
// Public synchronized boolean requestPermission(UsbDevice device) {
// Boolean result = false;
// If (this.isRegistered()) {
// If (device != null) {
// If (this.mUsbManager.hasPermission(device)) {
// This.processConnect(device);
//                } else {
// Try {
// This.mUsbManager.requestPermission(device, this.mPermissionIntent);
//                    } catch (Exception var4) {
//                        Log.w("USBMonitor", var4);
// This.processCancel(device);
// Result = true;
//                    }
//                }
//            } else {
// This.processCancel(device);
// Result = true;
//            }
//        } else {
// This.processCancel(device);
// Result = true;
//        }
//
// Return result;
//    }
//
// Public USBMonitor.UsbControlBlock openDevice(UsbDevice device) throws SecurityException {
// If (this.hasPermission(device)) {
//            USBMonitor.UsbControlBlock result = (USBMonitor.UsbControlBlock) this.mCtrlBlocks.get(device);
// If (result == null) {
// Result = new USBMonitor.UsbControlBlock(this, device);
// This.mCtrlBlocks.put(device, result);
//            }
//
// Return result;
//        } else {
// Throw new SecurityException("has no permission");
//        }
//    }
//
// Private final void processConnect(final UsbDevice device) {
// If (!this.destroyed) {
// This.updatePermission(device, true);
// This.mAsyncHandler.post(new Runnable() {
// Public void run() {
//                    USBMonitor.UsbControlBlock ctrlBlock = (USBMonitor.UsbControlBlock) USBMonitor.this.mCtrlBlocks.get(device);
// Boolean createNew;
// If (ctrlBlock == null) {
// CtrlBlock = new USBMonitor.UsbControlBlock(USBMonitor.this, device);
//                        USBMonitor.this.mCtrlBlocks.put(device, ctrlBlock);
// CreateNew = true;
//                    } else {
// CreateNew = false;
//                    }
//
// If (USBMonitor.this.mOnDeviceConnectListener != null) {
//                        USBMonitor.this.mOnDeviceConnectListener.onConnect(device, ctrlBlock, createNew);
//                    }
//
//                }
//            });
//        }
//    }
//
// Private final void processCancel(final UsbDevice device) {
// If (!this.destroyed) {
// This.updatePermission(device, false);
// If (this.mOnDeviceConnectListener != null) {
// This.mAsyncHandler.post(new Runnable() {
// Public void run() {
//                        USBMonitor.this.mOnDeviceConnectListener.onCancel(device);
//                    }
//                });
//            }
//
//        }
//    }
//
// Private final void processAttach(final UsbDevice device) {
// If (!this.destroyed) {
// If (this.mOnDeviceConnectListener != null) {
// This.mAsyncHandler.post(new Runnable() {
// Public void run() {
//                        USBMonitor.this.mOnDeviceConnectListener.onAttach(device);
//                    }
//                });
//            }
//
//        }
//    }
//
// Private final void processDettach(final UsbDevice device) {
// If (!this.destroyed) {
// If (this.mOnDeviceConnectListener != null) {
// This.mAsyncHandler.post(new Runnable() {
// Public void run() {
//                        USBMonitor.this.mOnDeviceConnectListener.onDettach(device);
//                    }
//                });
//            }
//
//        }
//    }
//
// Public static final String getDeviceKeyName(UsbDevice device) {
// Return getDeviceKeyName(device, (String) null, false);
//    }
//
// Public static final String getDeviceKeyName(UsbDevice device, boolean useNewAPI) {
// Return getDeviceKeyName(device, (String) null, useNewAPI);
//    }
//
//    @SuppressLint({"NewApi"})
// Public static final String getDeviceKeyName(UsbDevice device, String serial, boolean useNewAPI) {
// If (device == null) {
// Return "";
//        } else {
//            StringBuilder sb = new StringBuilder();
// Sb.append(device.getVendorId());
// Sb.append("#");
// Sb.append(device.getProductId());
// Sb.append("#");
// Sb.append(device.getDeviceClass());
// Sb.append("#");
// Sb.append(device.getDeviceSubclass());
// Sb.append("#");
// Sb.append(device.getDeviceProtocol());
// If (!TextUtils.isEmpty(serial)) {
//            }
//
// Return sb.toString();
//        }
//    }
//
// Public static final int getDeviceKey(UsbDevice device) {
// Return device != null ? getDeviceKeyName(device, (String) null, false).hashCode() : 0;
//    }
//
// Public static final int getDeviceKey(UsbDevice device, boolean useNewAPI) {
// Return device != null ? getDeviceKeyName(device, (String) null, useNewAPI).hashCode() : 0;
//    }
//
// Public static final int getDeviceKey(UsbDevice device, String serial, boolean useNewAPI) {
// Return device != null ? getDeviceKeyName(device, serial, useNewAPI).hashCode() : 0;
//    }
//
// Private static String getString(UsbDeviceConnection connection, int id, int languageCount, byte[] languages) {
// Byte[] work = new byte[256];
//        String result = null;
//
// For (int i = 1; i <= languageCount; ++i) {
// Int ret = connection.controlTransfer(128, 6, 768 | id, languages[i], work, 256, 0);
// If (ret > 2 && work[0] == ret && work[1] == 3) {
// Try {
// Result = new String(work, 2, ret - 2, "UTF-16LE");
// If (!"Љ".equals(result)) {
// Break;
//                    }
//
// Result = null;
//                } catch (UnsupportedEncodingException var9) {
//                }
//            }
//        }
//
// Return result;
//    }
//
// Public USBMonitor.UsbDeviceInfo getDeviceInfo(UsbDevice device) {
// Return updateDeviceInfo(this.mUsbManager, device, (USBMonitor.UsbDeviceInfo) null);
//    }
//
// Public static USBMonitor.UsbDeviceInfo getDeviceInfo(Context context, UsbDevice device) {
// Return updateDeviceInfo((UsbManager) context.getSystemService("usb"), device, new USBMonitor.UsbDeviceInfo());
//    }
//
//    @TargetApi(23)
// Public static USBMonitor.UsbDeviceInfo updateDeviceInfo(UsbManager manager, UsbDevice device, USBMonitor.UsbDeviceInfo _info) {
//        USBMonitor.UsbDeviceInfo info = _info != null ? _info : new USBMonitor.UsbDeviceInfo();
// Info.clear();
// If (device != null) {
// If (BuildCheck.isLollipop()) {
// Info.manufacturer = device.getManufacturerName();
// Info.product = device.getProductName();
// Info.serial = device.getSerialNumber();
//            }
//
// If (BuildCheck.isMarshmallow()) {
// Info.usb_version = device.getVersion();
//            }
//
// If (manager != null && manager.hasPermission(device)) {
//                UsbDeviceConnection connection = manager.openDevice(device);
// If (connection == null) {
// Return null;
//                }
//
// Byte[] desc = connection.getRawDescriptors();
// If (TextUtils.isEmpty(info.usb_version)) {
// Info.usb_version = String.format("%x.%02x", desc[3] & 255, desc[2] & 255);
//                }
//
// If (TextUtils.isEmpty(info.version)) {
// Info.version = String.format("%x.%02x", desc[13] & 255, desc[12] & 255);
//                }
//
// If (TextUtils.isEmpty(info.serial)) {
// Info.serial = connection.getSerial();
//                }
//
// Byte[] languages = new byte[256];
// Int languageCount = 0;
//
// Try {
// Int result = connection.controlTransfer(128, 6, 768, 0, languages, 256, 0);
// If (result > 0) {
// LanguageCount = (result - 2) / 2;
//                    }
//
// If (languageCount > 0) {
// If (TextUtils.isEmpty(info.manufacturer)) {
// Info.manufacturer = getString(connection, desc[14], languageCount, languages);
//                        }
//
// If (TextUtils.isEmpty(info.product)) {
// Info.product = getString(connection, desc[15], languageCount, languages);
//                        }
//
// If (TextUtils.isEmpty(info.serial)) {
// Info.serial = getString(connection, desc[16], languageCount, languages);
//                        }
//                    }
//                } finally {
// Connection.close();
//                }
//            }
//
// If (TextUtils.isEmpty(info.manufacturer)) {
// Info.manufacturer = USBVendorId.vendorName(device.getVendorId());
//            }
//
// If (TextUtils.isEmpty(info.manufacturer)) {
// Info.manufacturer = String.format("%04x", device.getVendorId());
//            }
//
// If (TextUtils.isEmpty(info.product)) {
// Info.product = String.format("%04x", device.getProductId());
//            }
//        }
//
// Return info;
//    }
//
// Public static final class UsbControlBlock implements Cloneable {
// Private final WeakReference<USBMonitor> mWeakMonitor;
// Private final WeakReference<UsbDevice> mWeakDevice;
// Protected UsbDeviceConnection mConnection;
// Protected final USBMonitor.UsbDeviceInfo mInfo;
// Private final int mBusNum;
// Private final int mDevNum;
// Private final SparseArray<SparseArray<UsbInterface>> mInterfaces;
//
// Private UsbControlBlock(USBMonitor monitor, UsbDevice device) {
// This.mInterfaces = new SparseArray();
// This.mWeakMonitor = new WeakReference(monitor);
// This.mWeakDevice = new WeakReference(device);
// This.mConnection = monitor.mUsbManager.openDevice(device);
// This.mInfo = USBMonitor.updateDeviceInfo(monitor.mUsbManager, device, (USBMonitor.UsbDeviceInfo) null);
//            String name = device.getDeviceName();
//            String[] v = !TextUtils.isEmpty(name) ? name.split("/") : null;
// Int busnum = 0;
// Int devnum = 0;
// If (v != null) {
// Busnum = Integer.parseInt(v[v.length - 2]);
// Devnum = Integer.parseInt(v[v.length - 1]);
//            }
//
// This.mBusNum = busnum;
// This.mDevNum = devnum;
// If (this.mConnection != null) {
// Int desc = this.mConnection.getFileDescriptor();
// Byte[] rawDesc = this.mConnection.getRawDescriptors();
//                Log.i("USBMonitor", String.format(Locale.US, "name=%s,desc=%d,busnum=%d,devnum=%d,rawDesc=", name, desc, busnum, devnum) + rawDesc);
//            } else {
//                Log.e("USBMonitor", "could not connect to device " + name);
//            }
//
//        }
//
// Private UsbControlBlock(USBMonitor.UsbControlBlock src) throws IllegalStateException {
// This.mInterfaces = new SparseArray();
//            USBMonitor monitor = src.getUSBMonitor();
//            UsbDevice device = src.getDevice();
// If (device == null) {
// Throw new IllegalStateException("device may already be removed");
//            } else {
// This.mConnection = monitor.mUsbManager.openDevice(device);
// If (this.mConnection == null) {
// Throw new IllegalStateException("device may already be removed or have no permission");
//                } else {
// This.mInfo = USBMonitor.updateDeviceInfo(monitor.mUsbManager, device, (USBMonitor.UsbDeviceInfo) null);
// This.mWeakMonitor = new WeakReference(monitor);
// This.mWeakDevice = new WeakReference(device);
// This.mBusNum = src.mBusNum;
// This.mDevNum = src.mDevNum;
//                }
//            }
//        }
//
// Public USBMonitor.UsbControlBlock clone() throws CloneNotSupportedException {
// Try {
//                USBMonitor.UsbControlBlock ctrlblock = new USBMonitor.UsbControlBlock(this);
// Return ctrlblock;
//            } catch (IllegalStateException var3) {
// Throw new CloneNotSupportedException(var3.getMessage());
//            }
//        }
//
// Public USBMonitor getUSBMonitor() {
// Return (USBMonitor) this.mWeakMonitor.get();
//        }
//
// Public final UsbDevice getDevice() {
// Return (UsbDevice) this.mWeakDevice.get();
//        }
//
// Public String getDeviceName() {
//            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
// Return device != null ? device.getDeviceName() : "";
//        }
//
// Public int getDeviceId() {
//            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
// Return device != null ? device.getDeviceId() : 0;
//        }
//
// Public String getDeviceKeyName() {
// Return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get());
//        }
//
// Public String getDeviceKeyName(boolean useNewAPI) throws IllegalStateException {
// If (useNewAPI) {
// This.checkConnection();
//            }
//
// Return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, useNewAPI);
//        }
//
// Public int getDeviceKey() throws IllegalStateException {
// This.checkConnection();
// Return USBMonitor.getDeviceKey((UsbDevice) this.mWeakDevice.get());
//        }
//
// Public int getDeviceKey(boolean useNewAPI) throws IllegalStateException {
// If (useNewAPI) {
// This.checkConnection();
//            }
//
// Return USBMonitor.getDeviceKey((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, useNewAPI);
//        }
//
// Public String getDeviceKeyNameWithSerial() {
// Return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, false);
//        }
//
// Public int getDeviceKeyWithSerial() {
// Return this.getDeviceKeyNameWithSerial().hashCode();
//        }
//
// Public synchronized UsbDeviceConnection getConnection() {
// Return this.mConnection;
//        }
//
// Public synchronized int getFileDescriptor() throws IllegalStateException {
// This.checkConnection();
// Return this.mConnection.getFileDescriptor();
//        }
//
// Public synchronized byte[] getRawDescriptors() throws IllegalStateException {
// This.checkConnection();
// Return this.mConnection.getRawDescriptors();
//        }
//
// Public int getVenderId() {
//            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
// Return device != null ? device.getVendorId() : 0;
//        }
//
// Public int getProductId() {
//            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
// Return device != null ? device.getProductId() : 0;
//        }
//
// Public String getUsbVersion() {
// Return this.mInfo.usb_version;
//        }
//
// Public String getManufacture() {
// Return this.mInfo.manufacturer;
//        }
//
// Public String getProductName() {
// Return this.mInfo.product;
//        }
//
// Public String getVersion() {
// Return this.mInfo.version;
//        }
//
// Public String getSerial() {
// Return this.mInfo.serial;
//        }
//
// Public int getBusNum() {
// Return this.mBusNum;
//        }
//
// Public int getDevNum() {
// Return this.mDevNum;
//        }
//
// Public synchronized UsbInterface getInterface(int interface_id) throws IllegalStateException {
// Return this.getInterface(interface_id, 0);
//        }
//
// Public synchronized UsbInterface getInterface(int interface_id, int altsetting) throws IllegalStateException {
// This.checkConnection();
//            SparseArray<UsbInterface> intfs = (SparseArray) this.mInterfaces.get(interface_id);
// If (intfs == null) {
// Intfs = new SparseArray();
// This.mInterfaces.put(interface_id, intfs);
//            }
//
//            UsbInterface intf = (UsbInterface) intfs.get(altsetting);
// If (intf == null) {
//                UsbDevice device = (UsbDevice) this.mWeakDevice.get();
// Int n = device.getInterfaceCount();
//
// For (int i = 0; i < n; ++i) {
//                    UsbInterface temp = device.getInterface(i);
// If (temp.getId() == interface_id && temp.getAlternateSetting() == altsetting) {
// Intf = temp;
// Break;
//                    }
//                }
//
// If (intf != null) {
// Intfs.append(altsetting, intf);
//                }
//            }
//
// Return intf;
//        }
//
// Public synchronized void claimInterface(UsbInterface intf) {
// This.claimInterface(intf, true);
//        }
//
// Public synchronized void claimInterface(UsbInterface intf, boolean force) {
// This.checkConnection();
// This.mConnection.claimInterface(intf, force);
//        }
//
// Public synchronized void releaseInterface(UsbInterface intf) throws IllegalStateException {
// This.checkConnection();
//            SparseArray<UsbInterface> intfs = (SparseArray) this.mInterfaces.get(intf.getId());
// If (intfs != null) {
// Int index = intfs.indexOfValue(intf);
// Intfs.removeAt(index);
// If (intfs.size() == 0) {
// This.mInterfaces.remove(intf.getId());
//                }
//            }
//
// This.mConnection.releaseInterface(intf);
//        }
//
// Public synchronized void close() {
// If (this.mConnection != null) {
// Int n = this.mInterfaces.size();
//
// For (int i = 0; i < n; ++i) {
//                    SparseArray<UsbInterface> intfs = (SparseArray) this.mInterfaces.valueAt(i);
// If (intfs != null) {
// Int m = intfs.size();
//
// For (int j = 0; j < m; ++j) {
//                            UsbInterface intf = (UsbInterface) intfs.valueAt(j);
// This.mConnection.releaseInterface(intf);
//                        }
//
// Intfs.clear();
//                    }
//                }
//
// This.mInterfaces.clear();
// This.mConnection.close();
// This.mConnection = null;
//                USBMonitor monitor = (USBMonitor) this.mWeakMonitor.get();
// If (monitor != null) {
// If (monitor.mOnDeviceConnectListener != null) {
// Monitor.mOnDeviceConnectListener.onDisconnect((UsbDevice) this.mWeakDevice.get(), this);
//                    }
//
// Monitor.mCtrlBlocks.remove(this.getDevice());
//                }
//            }
//
//        }
//
// Public boolean equals(Object o) {
// If (o == null) {
// Return false;
//            } else if (o instanceof USBMonitor.UsbControlBlock) {
//                UsbDevice device = ((USBMonitor.UsbControlBlock) o).getDevice();
// Return device == null ? this.mWeakDevice.get() == null : device.equals(this.mWeakDevice.get());
//            } else {
// Return o instanceof UsbDevice ? o.equals(this.mWeakDevice.get()) : super.equals(o);
//            }
//        }
//
// Private synchronized void checkConnection() throws IllegalStateException {
// If (this.mConnection == null) {
// Throw new IllegalStateException("already closed");
//            }
//        }
//    }
//
// Public static class UsbDeviceInfo {
// Public String usb_version;
// Public String manufacturer;
// Public String product;
// Public String version;
// Public String serial;
//
// Public UsbDeviceInfo() {
//        }
//
// Private void clear() {
// This.usb_version = this.manufacturer = this.product = this.version = this.serial = null;
//        }
//
// Public String toString() {
// Return String.format("UsbDevice:usb_version=%s,manufacturer=%s,product=%s,version=%s,serial=%s", this.usb_version != null ? this.usb_version : "", this.manufacturer != null ? this.manufacturer : "", this.product != null ? this.product : "", this.version != null ? this.version : "", this.serial != null ? this.serial : "");
//        }
//    }
//
// Public interface OnDeviceConnectListener {
// Void onAttach(UsbDevice var1);
//
// Void onDettach(UsbDevice var1);
//
// Void onConnect(UsbDevice var1, USBMonitor.UsbControlBlock var2, boolean var3);
//
// Void onDisconnect(UsbDevice var1, USBMonitor.UsbControlBlock var2);
//
// Void onCancel(UsbDevice var1);
//    }
// }
