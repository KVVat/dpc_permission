package com.android.certification.niap.permission.dpctester.test

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGatewayImpl
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTool
import java.util.function.Consumer


@PermissionTestModule("DPC Health Test Cases")
class DPCHealthTestModule(ctx: Activity): PermissionTestModuleBase(ctx){
    override var TAG: String = DPCHealthTestModule::class.java.simpleName
    val dpm = DevicePolicyManagerGatewayImpl(ctx)

    //val pm  =ctx.packageManager
    override fun start(callback: Consumer<PermissionTestRunner.Result>?) {
        PermissionTestRunner.getInstance().start(this) { result ->
            callback?.accept(result)
        }


    }
    @PermissionTest(".Owner Level",34,35)
    fun checkDeviceOwnerLevel() {
        //logger.system(">"+dpm.admin)
        logger.system(">"+ PermissionTool.getDeviceOwnerLevel(dpm))
        //checkUserRestriction("no_camera")
    }
    @PermissionTest(".Assigned Permissons",34,35)
    fun listAssignedPermissions() {
        //checkUserRestriction("no_camera")
        //val a = test<ConnectivityManager>(ConnectivityManager.class)
    }
}