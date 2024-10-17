package com.android.certification.niap.permission.dpctester.test.suite

import android.app.Activity
import com.android.certification.niap.permission.dpctester.R
import com.android.certification.niap.permission.dpctester.test.RuntimeTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleBinder
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleP
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleQ
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleR
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleS
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleT
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleU
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleV
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestSuite
import java.util.function.Consumer

@PermissionTestSuite("Signature Tests","Run Signature/Runtime Test", details = "details")
class SignatureTestSuite(activity: Activity): PermissionTestSuiteBase(
    async = false,
    activity = activity,
    values = arrayOf(
    RuntimeTestModule(activity),
    SignatureTestModule(activity),
    SignatureTestModuleP(activity),
    SignatureTestModuleQ(activity),
    SignatureTestModuleR(activity),
    SignatureTestModuleS(activity),
    SignatureTestModuleT(activity),
    SignatureTestModuleU(activity),
    SignatureTestModuleV(activity),
    SignatureTestModuleBinder(activity)
        )
){

}
