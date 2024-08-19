package com.android.certification.niap.permission.dpctester.test.log

class LoggerFactory {
    companion object {

        @JvmStatic
        fun createDefaultLogger(tag:String) : Logger{
           return LogcatLogger(tag)
        }
        @JvmStatic
        fun createActivityLogger(tag:String,frontEnd: ActivityLogger.LogListAdaptable):Logger{
            return ActivityLogger(tag,frontEnd)
        }
    }
}