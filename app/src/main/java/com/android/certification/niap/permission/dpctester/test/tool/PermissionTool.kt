package com.android.certification.niap.permission.dpctester.test.tool

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGateway
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGateway.DeviceOwnerLevel

class PermissionTool {
    companion object {

        fun getDeviceOwnerLevel(dpm:DevicePolicyManagerGateway): DeviceOwnerLevel {
            if(dpm.isDeviceOwnerApp){
                return DeviceOwnerLevel.DPS_DEVICE_OWNER_APP
            }
            if(dpm.isProfileOwnerApp){
                return DeviceOwnerLevel.DPS_PROFILE_OWNER_APP
            }
            if(dpm.isAdminActive){
                return DeviceOwnerLevel.DPS_ACTIVE_ADMIN_APP
            } else {
                return DeviceOwnerLevel.DPS_DISABLED
            }
        }


        fun getGrantedPermissions(ctx: Context, appPackage: String?): List<String>
        {
            val granted: MutableList<String> = ArrayList()
            try {
                val pi = ctx.packageManager.getPackageInfo(appPackage!!, PackageManager.GET_PERMISSIONS)
                for (i in pi.requestedPermissions.indices) {
                    if ((pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        granted.add(pi.requestedPermissions[i])
                    }
                }
            } catch (e: Exception) {
            }
            return granted
        }
    }
}