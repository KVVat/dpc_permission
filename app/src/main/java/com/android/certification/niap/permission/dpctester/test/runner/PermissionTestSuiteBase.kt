package com.android.certification.niap.permission.dpctester.test.runner

import android.app.Activity
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

    @JvmField
    val nopermMode = activity.resources.getBoolean(R.bool.inverse_test_result)


    init {
       values.forEach { m ->
           modules.add(m)
       }
        info.count_modules = modules.size
    }
    var cbSuiteStart: Consumer<Info>?=null;
    var cbSuiteFinish: Consumer<Info>?=null;
    var cbModuleStart: Consumer<PermissionTestModuleBase.Info>?=null;
    var cbModuleFinish: Consumer<PermissionTestModuleBase.Info>?=null;

    open fun start(callback: Consumer<PermissionTestRunner.Result>?,
                   cbSuiteStart_: Consumer<Info>?,
                   cbSuiteFinish_: Consumer<Info>?,
                   cbModuleStart_: Consumer<PermissionTestModuleBase.Info>?,
                   cbModuleFinish_: Consumer<PermissionTestModuleBase.Info>?){

        info.title = title
        info.details = details

        info.ellapsed_time = 0;
        //cbSuiteStart?.accept(info)
        cbSuiteFinish = cbSuiteFinish_;
        cbSuiteStart = cbSuiteStart_;
        cbModuleStart = cbModuleStart_;
        cbModuleFinish = cbModuleFinish_;
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
        override fun toString(): String {
            return "title=$title details=$details count_modules=$count_modules count_errors=$count_errors count_bypassed=$count_bypassed ellapsedtime=$ellapsed_time"
        }
    }

}