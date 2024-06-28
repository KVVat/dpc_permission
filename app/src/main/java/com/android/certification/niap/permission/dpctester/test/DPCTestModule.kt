package com.android.certification.niap.permission.dpctester.test

import android.Manifest.permission.MANAGE_DEVICE_POLICY_APPS_CONTROL
import android.Manifest.permission.MANAGE_DEVICE_POLICY_ASSIST_CONTENT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL
import android.Manifest.permission.MANAGE_DEVICE_POLICY_BLUETOOTH
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CALLS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CAMERA
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CAMERA_TOGGLE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CERTIFICATES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CONTENT_PROTECTION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_DISPLAY
import android.Manifest.permission.MANAGE_DEVICE_POLICY_FUN
import android.Manifest.permission.MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCALE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCATION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MICROPHONE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MODIFY_USERS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PRINTING
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PROFILES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SAFE_BOOT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SCREEN_CONTENT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SMS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_TIME
import android.Manifest.permission.MANAGE_DEVICE_POLICY_VPN
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WALLPAPER
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WINDOWS
import android.content.Context
import android.os.UserManager
import androidx.annotation.RequiresApi
import androidx.core.util.Consumer
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGatewayImpl


@PermissionTestModule("DPC Test Cases")
class DPCTestModule(ctx: Context):PermissionTestModuleBase(ctx){
    override var TAG: String = DPCTestModule::class.java.simpleName
    val dpm = DevicePolicyManagerGatewayImpl(ctx)

    override fun start(callback: Consumer<PermissionTestRunner.Result>?) {
        PermissionTestRunner.getInstance().start(this) { result ->
            //we can evaluate the results here
            callback?.accept(result)
        }
    }


    @PermissionTest(MANAGE_DEVICE_POLICY_CERTIFICATES,34,35)
    fun testCertificates() {
        //it is require to call from transaction and flag should be enabled
        // DeviceConfig.getBoolean(
        //                NAMESPACE_DEVICE_POLICY_MANAGER,
        //                PERMISSION_BASED_ACCESS_EXPERIMENT_FLAG,
        //                DEFAULT_VALUE_PERMISSION_BASED_ACCESS_FLAG);

        //dpm.installCertificate(null)

    }
    @PermissionTest(MANAGE_DEVICE_POLICY_APPS_CONTROL,34,35)
    fun testAppsControl() {
        // read below code carfulle
        //checkCanExecuteOrThrowUnsafe(
        //                DevicePolicyManager.OPERATION_SET_USER_CONTROL_DISABLED_PACKAGES?
        //depend or DevicePolicySafetyCheker also
        dpm.setUserControlDisabledPackages(listOf("com.package","com.package2"),{},{})
        //checkUserRestriction("no_apps_control")
    }


    @PermissionTest(MANAGE_DEVICE_POLICY_CAMERA,34)
    fun testCamera() {
        checkUserRestriction("no_camera")
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_AUDIO_OUTPUT,34)
    fun testAudioOutput(){
        checkUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_BLUETOOTH,34)
    fun testBluetooth(){
        checkUserRestriction(UserManager.DISALLOW_BLUETOOTH);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_CALLS,34)
    fun testCalls(){
        checkUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES,34)
    fun testDebuggingFeatures(){
        //should always false for running test cases
        clearUserRestriction(UserManager.DISALLOW_DEBUGGING_FEATURES)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_DISPLAY,34)
    fun testDisplay(){
        checkUserRestriction(UserManager.DISALLOW_AMBIENT_DISPLAY);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES,34)
    fun testInstallUnknownSources(){
        checkUserRestriction(UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCALE,34)
    fun testLocale(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_LOCALE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCATION,34)
    fun testLocation(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_LOCATION);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MOBILE_NETWORK,34)
    fun testMobileNetwork(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MODIFY_USERS,34)
    fun testModifyUsers(){
        checkUserRestriction(UserManager.DISALLOW_ADD_USER);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION,34)
    fun testNearbyCommunication(){
        checkUserRestriction(UserManager.DISALLOW_OUTGOING_BEAM);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA,34)
    fun testPhysicalMedia(){
        checkUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PRINTING,34)
    fun testPrinting(){
        checkUserRestriction(UserManager.DISALLOW_PRINTING);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PROFILES,34)
    fun testProfiles(){
        checkUserRestriction(UserManager.ALLOW_PARENT_PROFILE_APP_LINKING);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS,34)
    fun testRestrictPrivateDns(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_PRIVATE_DNS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND,34)
    fun testRunInBackground(){
        checkUserRestriction("no_run_in_background");
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SAFE_BOOT,34)
    fun testSafeBoot(){
        checkUserRestriction(UserManager.DISALLOW_SAFE_BOOT);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SCREEN_CONTENT,34)
    fun testScreenContent(){
        checkUserRestriction(UserManager.DISALLOW_CONTENT_CAPTURE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MICROPHONE,34)
    fun testMicrophone(){
        checkUserRestriction(UserManager.DISALLOW_MICROPHONE_TOGGLE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SMS,34)
    fun testSMS(){
        checkUserRestriction(UserManager.DISALLOW_SMS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_FUN,34)
    fun testFun(){
        checkUserRestriction(UserManager.DISALLOW_FUN);
    }

    //Clear User Transaction Tests
    @PermissionTest(MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS,34)
    fun testSystemDialogs(){
        clearUserRestriction(UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_TIME,34)
    fun testTime(){
        clearUserRestriction(UserManager.DISALLOW_CONFIG_DATE_TIME);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_VPN,34)
    fun testVPN(){
        clearUserRestriction(UserManager.DISALLOW_CONFIG_VPN);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WALLPAPER,34)
    fun testWallpaper(){
        clearUserRestriction(UserManager.DISALLOW_SET_WALLPAPER);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WINDOWS,34)
    fun testWindows(){
        checkUserRestriction(UserManager.DISALLOW_CREATE_WINDOWS);
    }
    //For Android 15
    //UserManger + DevicePolicyService related Permissions
    @RequiresApi(35)
    @PermissionTest(MANAGE_DEVICE_POLICY_CAMERA_TOGGLE,35)
    fun testCameraToggle(){
        checkUserRestriction(UserManager.DISALLOW_CAMERA_TOGGLE);
    }
    @RequiresApi(35)
    @PermissionTest(MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE,35)
    fun testMicrophoneToggle(){
        checkUserRestriction(UserManager.DISALLOW_MICROPHONE_TOGGLE);
    }
    @RequiresApi(35)
    @PermissionTest(MANAGE_DEVICE_POLICY_ASSIST_CONTENT,35)
    fun testAssistContent(){
        //Also blocked by system flag?
        checkUserRestriction(UserManager.DISALLOW_ASSIST_CONTENT);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL,35)
    fun testUninstallBlocked(){
        dpm.setUninstallBlocked(
            "test.package.name",false,{},{e->throw e})
    }
    /*
    @PermissionTest(MANAGE_DEVICE_POLICY_AUDIT_LOGGING,35)
    fun testAuditLogginb(){
        dpm.setUninstallBlocked(
            "test.package.name",false,{},{e->throw e})
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_CONTENT_PROTECTION,35)
    fun testContentProtectionPolicy(){
        val flag = 1 shl 7 //See enterprisepolicy.java
        dpm.setContentProtectionPolicy(flag)
    }

    @PermissionTest("MANAGE_DEVICE_POLICY_STORAGE_LIMIT",35)
    fun testStorageLimit(){
        dpm.forceSetMaxPolicyStorageLimit(10000000);
       //dpm.setContentProtectionPolicy(
       //     "test.package.name",false,{},{e->throw e})

    }


    /* ?
    @PermissionTest(MANAGE_DEVICE_POLICY_THREAD_NETWORK,35)
    fun testThreadNetwork(){
        checkUserRestriction(UserManager.DISALLOW_THREAD_NETWORK);
    }*/


    private fun clearUserRestriction(aRestriction:String){
        //Make sure to always set testing value as false
        dpm.setUserRestriction(aRestriction, false,{},{e->throw e})
    }
    //UserManager + DevicePolicyService related Permissions
    private fun checkUserRestriction(aRestriction:String){
        val value = dpm.userRestrictions.contains(aRestriction)
        //Make sure to reset the testing value
        dpm.setUserRestriction(aRestriction, value,{},{e->throw e})
        dpm.setUserRestriction(aRestriction, !value,{},{e->throw e})
    }
}