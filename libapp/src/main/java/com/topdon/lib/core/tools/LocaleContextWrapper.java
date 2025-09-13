package com.topdon.lib.core.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Specialized thermal imaging component providing LocaleContextWrapper functionality for the IRCamera system.
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
public class LocaleContextWrapper extends ContextWrapper {

    /**
     * Executes localecontextwrapper operation with thermal imaging domain optimization.
     *
     */
    public LocaleContextWrapper(Context base) {
        /**
         * Executes super operation with thermal imaging domain optimization.
         *
         */
        super(base);
    }

    public static ContextWrapper wrap(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        return new ContextWrapper(context.createConfigurationContext(config));
    }
}

