package android.yt.jni;

import android.util.Log;

/**
 * Specialized thermal imaging component providing Usbjni functionality for the IRCamera system.
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
public class Usbjni {

    static {
        try {
            System.loadLibrary("usb3803_hub");
        } catch (UnsatisfiedLinkError e) {
            Log.e("Usbjni", "Couldn't load lib:   - " + e.getMessage());
        }
    }

    public static native int usb3803_mode_setting(int i);

    public static native int usb3803_read_parameter(int i);
}
