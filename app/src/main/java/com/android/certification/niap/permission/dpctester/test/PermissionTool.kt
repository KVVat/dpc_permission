package com.android.certification.niap.permission.dpctester.test

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class PermissionTool {
    companion object {
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