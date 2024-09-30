package com.android.certification.niap.permission.dpctester.test.runner

import android.app.Activity
import android.util.Log
import com.android.certification.niap.permission.dpctester.R
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestSuite
import java.util.function.Consumer

open class PermissionTestSuiteBase(val activity: Activity, val async: Boolean, vararg values:PermissionTestModuleBase) {
    var TAG: String = PermissionTestSuiteBase::class.java.simpleName
    var title: String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.name
    var label: String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.label
    var details:String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.details

    val info:Info = Info()
    val modules:MutableList<PermissionTestModuleBase> = mutableListOf()
    var methodCallback:Consumer<PermissionTestRunner.Result>?=null;
    //@JvmField
    val testCount:Int
        get() {
            var count = 0
            modules.forEach { m ->
                count += m.testSize + m.additionalTestSize
            }
            //Log.d(TAG, "testCount=$count")
            return count;
        }

    init {
       values.forEach { m ->
           modules.add(m)
           //testCount+=m.testSize
       }
        info.count_modules = modules.size
    }
    var cbSuiteStart: Consumer<Info>?=null;
    var cbSuiteFinish: Consumer<Info>?=null;
    var cbModuleControl:Consumer<PermissionTestModuleBase.Info>?=null
    var cbTestControl:Consumer<PermissionTestModuleBase.Info>?=null
    var cbModuleStart: Consumer<PermissionTestModuleBase.Info>?=null;
    var cbModuleFinish: Consumer<PermissionTestModuleBase.Info>?=null;

    open fun start(callback: Consumer<PermissionTestRunner.Result>?,
                   cbSuiteStart_: Consumer<Info>?,
                   cbSuiteFinish_: Consumer<Info>?,
                   cbModuleStart_: Consumer<PermissionTestModuleBase.Info>?,
                   cbModuleFinish_: Consumer<PermissionTestModuleBase.Info>?,
                   cbModuleControl_:Consumer<PermissionTestModuleBase.Info>?=null,
                   cbTestControl_:Consumer<PermissionTestModuleBase.Info>?=null
    ){

        info.title = title
        info.details = details

        info.ellapsed_time = 0;
        //cbSuiteStart?.accept(info)
        cbSuiteFinish = cbSuiteFinish_
        cbSuiteStart = cbSuiteStart_
        cbModuleStart = cbModuleStart_
        cbModuleFinish = cbModuleFinish_
        cbModuleControl = cbModuleControl_
        cbTestControl = cbTestControl_
        //
        PermissionTestRunner.getInstance().start(this,callback)

    }

    open class Info {
        var title: String? = null
        var details:String? = null
        var count_modules: Int = 0
        var count_errors: Int = 0
        var count_bypassed: Int =0
        var ellapsed_time: Long = 0
        var start_time: Long=0
        override fun toString(): String {
            return "title=$title details=$details count_modules=$count_modules count_errors=$count_errors count_bypassed=$count_bypassed ellapsedtime=$ellapsed_time"
        }
    }

}