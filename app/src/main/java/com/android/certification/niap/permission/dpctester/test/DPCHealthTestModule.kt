package com.android.certification.niap.permission.dpctester.test

import android.content.Context
import androidx.core.util.Consumer


@PermissionTestModule("DPC Health Test Cases")
class DPCHealthTestModule(ctx: Context):PermissionTestModuleBase(ctx){
    override var TAG: String = DPCHealthTestModule::class.java.simpleName
    override fun start(callback: Consumer<PermissionTestRunner.Result>?) {
        PermissionTestRunner.getInstance().start(this) { result ->
            callback?.accept(result)
        }
    }
    @PermissionTest(".Owner Level",34,35)
    fun checkDeviceOwnerLevel() {
        //checkUserRestriction("no_camera")
    }
    @PermissionTest(".Assigned Permissons",34,35)
    fun listAssignedPermissions() {
        //checkUserRestriction("no_camera")
    }
}