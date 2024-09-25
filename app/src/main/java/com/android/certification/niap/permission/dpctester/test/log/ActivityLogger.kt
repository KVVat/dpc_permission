package com.android.certification.niap.permission.dpctester.test.log

import android.util.Log
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.test.log.Logger.DebugLevel.LEVEL_ERROR
import com.android.certification.niap.permission.dpctester.test.log.Logger.DebugLevel.LEVEL_INFO
import com.android.certification.niap.permission.dpctester.test.log.Logger.DebugLevel.LEVEL_DEBUG
import com.android.certification.niap.permission.dpctester.test.log.Logger.DebugLevel.LEVEL_SYSTEM
import com.android.certification.niap.permission.dpctester.test.log.Logger.DebugLevel.LEVEL_WARN

class ActivityLogger(override val tag: String, val adaptable:LogListAdaptable):Logger{

    private val UI_LEVEL: Int = LEVEL_SYSTEM

    override fun debug(message: String) {
        Log.d(tag,message)
        if(UI_LEVEL<=LEVEL_DEBUG){
            adaptable.addLogLine(message)
        }
    }

    override fun debug(message: String, throwable: Throwable) {
        Log.d(tag,message,throwable)
        if(UI_LEVEL<=LEVEL_DEBUG){
            adaptable.addLogLine(message)
        }
    }

    override fun info(message: String) {
        Log.i(tag,message)
        if(UI_LEVEL<=LEVEL_INFO){
            adaptable.addLogLine(message)
        }
    }

    override fun info(message: String, throwable: Throwable) {
        Log.i(tag,message,throwable)
        if(UI_LEVEL<=LEVEL_INFO){
            adaptable.addLogLine(message)
        }
    }
    override fun warn(message: String) {
        Log.w(tag,message)
        if(UI_LEVEL<=LEVEL_WARN){
            adaptable.addLogLine(message)
        }
    }
    override fun error(message: String) {
        Log.e(tag,message)
        if(UI_LEVEL<=LEVEL_ERROR){
            adaptable.addLogLine(message)
        }
    }

    override fun error(message: String, throwable: Throwable) {
        Log.e(tag,message,throwable)
        if(UI_LEVEL<=LEVEL_ERROR){
            adaptable.addLogLine(message)
        }
    }

    override fun system(message: String) {
        Log.i(tag,message)
        if(UI_LEVEL<=LEVEL_SYSTEM){
            adaptable.addLogLine(message)
        }
    }
    fun logbox(box: LogBox) {


    }


    interface LogListAdaptable {
        //fun setLogAdapter()
        fun addLogLine(msg: String)
        fun addLogBox(logbox:LogBox)
    }
}