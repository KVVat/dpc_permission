package com.android.certification.niap.permission.dpctester.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.os.UserHandle;

import com.android.certification.niap.permission.dpctester.common.Util;
import com.android.certification.niap.permission.dpctester.comp.BindDeviceAdminServiceHelper;
import com.android.certification.niap.permission.dpctester.comp.DeviceOwnerService;
import com.android.certification.niap.permission.dpctester.comp.IDeviceOwnerService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            if (!Util.isProfileOwner(context)
                    || Util.getBindDeviceAdminTargetUsers(context).size() == 0) {
                return;
            }
            // We are a profile owner and can bind to the device owner - let's notify the device
            // owner that we are up and running (i.e. our user was just started and/or unlocked)
            UserHandle targetUser = Util.getBindDeviceAdminTargetUsers(context).get(0);
            BindDeviceAdminServiceHelper<IDeviceOwnerService> helper =
                    createBindDeviceOwnerServiceHelper(context, targetUser);
            helper.crossUserCall(service -> service.notifyUserIsUnlocked(Process.myUserHandle()));
        }
    }

    private BindDeviceAdminServiceHelper<IDeviceOwnerService> createBindDeviceOwnerServiceHelper(
            Context context, UserHandle targetUserHandle) {
        return new BindDeviceAdminServiceHelper<>(
                context, DeviceOwnerService.class, IDeviceOwnerService.Stub::asInterface, targetUserHandle);
    }
}
