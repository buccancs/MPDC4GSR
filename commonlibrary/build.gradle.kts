/**
 * After Android Studio version upgrade
 * Use new import method to solve Direct local .aar file dependencies issue
 */
configurations.maybeCreate("default")
artifacts.add("default", file("libcommon_1.0.5_23051509.aar"))