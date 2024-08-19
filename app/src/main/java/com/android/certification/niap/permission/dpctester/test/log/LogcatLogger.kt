package com.android.certification.niap.permission.dpctester.test.log

import android.util.Log

class LogcatLogger(override val tag: String):Logger{

    override fun debug(message: String) {
        Log.d(tag,message)
    }

    override fun debug(message: String, throwable: Throwable) {
        Log.d(tag,message,throwable)
    }

    override fun info(message: String) {
        Log.d(tag,message)
    }

    override fun info(message: String, throwable: Throwable) {
        Log.i(tag,message,throwable)
    }

    override fun error(message: String) {
        Log.e(tag,message)
    }

    override fun error(message: String, throwable: Throwable) {
        Log.e(tag,message,throwable)
    }

    override fun system(message: String) {
        Log.e(tag,message)
    }

    override fun warn(message: String) {
        Log.w(tag,message)
    }
}