package com.android.certification.niap.permission.dpctester.test.suite

import android.app.Activity
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.google.common.base.CaseFormat

class SingleModuleTestSuite(activity: Activity, aModule: PermissionTestModuleBase): PermissionTestSuiteBase(
    async = false,
    activity = activity,
    values = arrayOf(aModule)
){
    init {
        title =  aModule.javaClass.getAnnotation(PermissionTestModule::class.java)?.name
        label =  aModule.javaClass.getAnnotation(PermissionTestModule::class.java)?.label
        key =  CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, aModule.javaClass.simpleName)
        //StaticLogger.debug("title="+title+" label="+label)
        details = "read from module"
    }

}
