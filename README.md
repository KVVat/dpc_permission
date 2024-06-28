## TestDPC as Device Owner

### Device Owner
```console
adb shell dpm set-device-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
### Device Admin
```console
adb shell dpm set-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
You should use 'set-device-admin' command, until android 14

### Profile Owner
```console
adb shell dpm set-profile-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```

### Apply DM Role to app

```console
adb shell cmd role set-bypassing-role-qualification true
adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
```



## TestDPC as DM role holder

TestDPC v9.0.5+ can be setup as Device Management Role Holder.

*   Running the following `adb` commands:

    ```console
    adb shell cmd role set-bypassing-role-qualification true
    adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
    ```

    Note: unlike DO/PO, this change is not persisted so TestDPC needs to be
    marked as role holder again if the device reboots.

## How to Uninstall Debug-Mode Device Owner app

If you once uninstall the app with release mode and set it to device owner there's no way.
Only if we install it as debug app

```console
adb shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
Go settings -> Secuirty -> More Secuirty & Privacy -> Device Admin apps -> Open app

Then uninstall it. If you can't uninstall it with some notifications. 
You should reboot the device and try unistall from shell.

```console
adb uninstall com.android.certification.niap.permission.dpctester
```
