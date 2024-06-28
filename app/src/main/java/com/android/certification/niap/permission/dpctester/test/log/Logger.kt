package com.android.certification.niap.permission.dpctester.test.log

interface Logger {
    val tag: String

    companion object DebugLevel {
        val LEVEL_DEBUG: Int = 0
        val LEVEL_INFO: Int = 1
        val LEVEL_WARN: Int = 2
        val LEVEL_ERROR: Int = 3
        val LEVEL_SYSTEM: Int = 4
    }
    /**
     * Logs the provided `message` at the debug level.
     */
    fun debug(message: String)
    fun debug(message: String, throwable: Throwable)

    /**
     * Logs the provided `message` at the info level.
     */
    fun info(message: String)
    fun info(message: String, throwable: Throwable)

    /**
     * Logs the provided `message` at the error level.
     */
    fun error(message: String)
    fun error(message: String, throwable: Throwable)

    fun system(message: String)
    fun warn(message: String)
}