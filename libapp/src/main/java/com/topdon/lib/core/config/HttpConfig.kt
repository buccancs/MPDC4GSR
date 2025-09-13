package com.topdon.lib.core.config

/**
 * Configuration management system for thermal imaging parameters. Handles settings and calibration for HttpConfig operations.
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
object HttpConfig {
    const val HOST = "https:// Api.topdon.com"

    const val AUTH_SECRET = "vG8XVT/yWcJiqSVlIC2zRRhBmoSTIiRU2520KGIjop4ISKwDjUWXZEADpvFEMH3DT8OgEOsnOs5Auts0WKpxbhE5AGla3YZiVJCHugkSr5UvHDSbs5Ft74wO21Lwj4cDvQw8+hewpmwZS54cpSnSgXLO+2GEcR767dKwwgXSpqx1S8j51uFoxlWwr5CFSJdXinxwQyg26EzjbaqKXa8ViaqUFgi+17Qd9A5lY0p6fsEAtOeoqspQmD5ugKkwUmoy7/HzBrQXfYRGPCXwkBUq7S0DwmM1O918wdqGIQcSm9W8xUgBqyXDVQ=="

    @Volatile
    var hasNewVersion = false
}
