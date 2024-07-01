package com.android.certification.niap.permission.dpctester.test

import android.content.Context
import androidx.core.util.Consumer
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGatewayImpl


@PermissionTestModule("DPC Health Test Cases")
class DPCHealthTestModule(ctx: Context):PermissionTestModuleBase(ctx){
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
        logger.system(">"+PermissionTool.getDeviceOwnerLevel(dpm))
        //checkUserRestriction("no_camera")
    }
    @PermissionTest(".Assigned Permissons",34,35)
    fun listAssignedPermissions() {
        //checkUserRestriction("no_camera")
    }
}