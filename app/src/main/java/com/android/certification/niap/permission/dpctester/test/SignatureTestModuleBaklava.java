//Auto generated file InstallPermissionTestModule.java by CoderPorterPlugin
/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.certification.niap.permission.dpctester.test;


import android.app.Activity;
import android.media.quality.MediaQualityManager;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.util.Objects;

@PermissionTestModule(name="Signature 36(Baklava) Test Cases",prflabel="Baklava(16)")
public class SignatureTestModuleBaklava extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleBaklava(@NonNull Activity activity) {
		super(activity);
	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	//**** method template for target signature SDK36
	@PermissionTest(permission="OBSERVE_PICTURE_PROFILES",sdkMin=36)
	public void testObservePictureProfiles(){
		logger.debug("The test for android.permission.OBSERVE_PICTURE_PROFILES is not implemented yet");
	}
	@PermissionTest(permission="MANAGE_GLOBAL_PICTURE_QUALITY_SERVICE",sdkMin=36)
	public void testManageGlobalPictureQualityService(){
		//https://source.corp.google.com/h/googleplex-android/platform/superproject/main/+/main:cts/tests/tests/mediaquality/src/android/media/mediaquality/cts/MediaQualityTest.java;l=97?q=getPictureProfilesByPackage&sq=repo:googleplex-android%2Fplatform%2Fsuperproject%2Fmain%20branch:main
		//getService(MediaQualityManager.class).getPictureProfilesByPackage(
		logger.debug("The test for android.permission.MANAGE_GLOBAL_PICTURE_QUALITY_SERVICE is not implemented yet");
	}
	@PermissionTest(permission="MANAGE_GLOBAL_SOUND_QUALITY_SERVICE",sdkMin=36)
	public void testManageGlobalSoundQualityService(){
		//MediaQualityManager.getSoundProfilePackageNames()
		logger.debug("The test for android.permission.MANAGE_GLOBAL_SOUND_QUALITY_SERVICE is not implemented yet");
	}
	@PermissionTest(permission="THREAD_NETWORK_TESTING",sdkMin=36)
	public void testThreadNetworkTesting(){
		//https://source.corp.google.com/h/googleplex-android/platform/superproject/main/+/main:packages/modules/Connectivity/thread/tests/unit/src/com/android/server/thread/ThreadNetworkShellCommandTest.java;l=103?q=THREAD_NETWORK_TESTING&sq=repo:googleplex-android%2Fplatform%2Fsuperproject%2Fmain%20branch:main
		//  runShellCommand("force-country-code", "enabled", "US");?
		logger.debug("The test for android.permission.THREAD_NETWORK_TESTING is not implemented yet");
	}
	@PermissionTest(permission="REMOVE_ACCOUNTS",sdkMin=36)
	public void testRemoveAccounts(){
		//AccountManagerService.removeAccountAsUser(IAccountManagerResponse response, Account account,
		//            boolean expectActivityLaunch, int userId)
		logger.debug("The test for android.permission.REMOVE_ACCOUNTS is not implemented yet");
	}
	@PermissionTest(permission="COPY_ACCOUNTS",sdkMin=36)
	public void testCopyAccounts(){
		//AccountManagerService.copyAccountToUser(IAccountManagerResponse response, Account account,
		//            boolean expectActivityLaunch, int userId)
		logger.debug("The test for android.permission.COPY_ACCOUNTS is not implemented yet");
	}
	@PermissionTest(permission="VIBRATE_VENDOR_EFFECTS",sdkMin=36)
	public void testVibrateVendorEffects(){
		//https://source.corp.google.com/h/googleplex-android/platform/superproject/main/+/main:frameworks/base/tests/permission/src/com/android/framework/permission/tests/VibratorManagerServicePermissionTest.java;l=147?q=VIBRATE_VENDOR_EFFECTS&sq=repo:googleplex-android%2Fplatform%2Fsuperproject%2Fmain%20branch:main
		//mVibratorService.startVendorVibrationSession(Process.myUid(), DEVICE_ID, PACKAGE_NAME,
		//		new int[] { 1 }, ATTRS, "testVibrate", null);
		logger.debug("The test for android.permission.VIBRATE_VENDOR_EFFECTS is not implemented yet");
	}
	@PermissionTest(permission="START_VIBRATION_SESSIONS",sdkMin=36)
	public void testStartVibrationSessions(){
		//Same as above?
		logger.debug("The test for android.permission.START_VIBRATION_SESSIONS is not implemented yet");
	}
	@PermissionTest(permission="MANAGE_ADVANCED_PROTECTION_MODE",sdkMin=36)
	public void testManageAdvancedProtectionMode(){
		logger.debug("The test for android.permission.MANAGE_ADVANCED_PROTECTION_MODE is not implemented yet");
	}
	@PermissionTest(permission="READ_INTRUSION_DETECTION_STATE",sdkMin=36)
	public void testReadIntrusionDetectionState(){
		logger.debug("The test for android.permission.READ_INTRUSION_DETECTION_STATE is not implemented yet");
	}
	@PermissionTest(permission="MANAGE_INTRUSION_DETECTION_STATE",sdkMin=36)
	public void testManageIntrusionDetectionState(){
		logger.debug("The test for android.permission.MANAGE_INTRUSION_DETECTION_STATE is not implemented yet");
	}
	@PermissionTest(permission="BIND_DEPENDENCY_INSTALLER",sdkMin=36)
	public void testBindDependencyInstaller(){
		logger.debug("The test for android.permission.BIND_DEPENDENCY_INSTALLER is not implemented yet");
	}

	@PermissionTest(permission="REQUEST_COMPANION_PROFILE_SENSOR_DEVICE_STREAMING",sdkMin=36)
	public void testRequestCompanionProfileSensorDeviceStreaming(){
		logger.debug("The test for android.permission.REQUEST_COMPANION_PROFILE_SENSOR_DEVICE_STREAMING is not implemented yet");
	}

	@PermissionTest(permission="READ_SYSTEM_PREFERENCES",sdkMin=36)
	public void testReadSystemPreferences(){
		logger.debug("The test for android.permission.READ_SYSTEM_PREFERENCES is not implemented yet");
	}
	@PermissionTest(permission="WRITE_SYSTEM_PREFERENCES",sdkMin=36)
	public void testWriteSystemPreferences(){
		logger.debug("The test for android.permission.WRITE_SYSTEM_PREFERENCES is not implemented yet");
	}
	@PermissionTest(permission="EYE_CALIBRATION",sdkMin=36)
	public void testEyeCalibration(){
		logger.debug("The test for android.permission.EYE_CALIBRATION is not implemented yet");
	}
	@PermissionTest(permission="FACE_TRACKING_CALIBRATION",sdkMin=36)
	public void testFaceTrackingCalibration(){
		logger.debug("The test for android.permission.FACE_TRACKING_CALIBRATION is not implemented yet");
	}
	@PermissionTest(permission="IMPORT_XR_ANCHOR",sdkMin=36)
	public void testImportXrAnchor(){
		logger.debug("The test for android.permission.IMPORT_XR_ANCHOR is not implemented yet");
	}
	@PermissionTest(permission="ALWAYS_BOUND_TV_INPUT",sdkMin=36)
	public void testAlwaysBoundTvInput(){
		logger.debug("The test for android.permission.ALWAYS_BOUND_TV_INPUT is not implemented yet");
	}
	@PermissionTest(permission="BYPASS_CONCURRENT_RECORD_AUDIO_RESTRICTION",sdkMin=36)
	public void testBypassConcurrentRecordAudioRestriction(){
		logger.debug("The test for android.permission.BYPASS_CONCURRENT_RECORD_AUDIO_RESTRICTION is not implemented yet");
	}
	@PermissionTest(permission="ACCESS_FINE_POWER_MONITORS",sdkMin=36)
	public void testAccessFinePowerMonitors(){
		logger.debug("The test for android.permission.ACCESS_FINE_POWER_MONITORS is not implemented yet");
	}
	@PermissionTest(permission="READ_SUBSCRIPTION_PLANS",sdkMin=36)
	public void testReadSubscriptionPlans(){
		logger.debug("The test for android.permission.READ_SUBSCRIPTION_PLANS is not implemented yet");
	}

	@PermissionTest(permission="INSTALL_DEPENDENCY_SHARED_LIBRARIES",sdkMin=36)
	public void testInstallDependencySharedLibraries(){
		logger.debug("The test for android.permission.INSTALL_DEPENDENCY_SHARED_LIBRARIES is not implemented yet");
	}

	@PermissionTest(permission="MANAGE_KEY_GESTURES",sdkMin=36)
	public void testManageKeyGestures(){
		logger.debug("The test for android.permission.MANAGE_KEY_GESTURES is not implemented yet");
	}
	@PermissionTest(permission="LISTEN_FOR_KEY_ACTIVITY",sdkMin=36)
	public void testListenForKeyActivity(){
		logger.debug("The test for android.permission.LISTEN_FOR_KEY_ACTIVITY is not implemented yet");
	}
	@PermissionTest(permission="BACKUP_HEALTH_CONNECT_DATA_AND_SETTINGS",sdkMin=36)
	public void testBackupHealthConnectDataAndSettings(){
		logger.debug("The test for android.permission.BACKUP_HEALTH_CONNECT_DATA_AND_SETTINGS is not implemented yet");
	}
	@PermissionTest(permission="RESTORE_HEALTH_CONNECT_DATA_AND_SETTINGS",sdkMin=36)
	public void testRestoreHealthConnectDataAndSettings(){
		logger.debug("The test for android.permission.RESTORE_HEALTH_CONNECT_DATA_AND_SETTINGS is not implemented yet");
	}
	@PermissionTest(permission="CAPTURE_CONSENTLESS_BUGREPORT_DELEGATED_CONSENT",sdkMin=36)
	public void testCaptureConsentlessBugreportDelegatedConsent(){
		logger.debug("The test for android.permission.CAPTURE_CONSENTLESS_BUGREPORT_DELEGATED_CONSENT is not implemented yet");
	}
	@PermissionTest(permission="MANAGE_SECURE_LOCK_DEVICE",sdkMin=36)
	public void testManageSecureLockDevice(){
		logger.debug("The test for android.permission.MANAGE_SECURE_LOCK_DEVICE is not implemented yet");
	}
	@PermissionTest(permission="ENTER_TRADE_IN_MODE",sdkMin=36)
	public void testEnterTradeInMode(){
		logger.debug("The test for android.permission.ENTER_TRADE_IN_MODE is not implemented yet");
	}
	@PermissionTest(permission="DYNAMIC_INSTRUMENTATION",sdkMin=36)
	public void testDynamicInstrumentation(){
		logger.debug("The test for android.permission.DYNAMIC_INSTRUMENTATION is not implemented yet");
	}
	@PermissionTest(permission="RESOLVE_COMPONENT_FOR_UID",sdkMin=36)
	public void testResolveComponentForUid(){
		logger.debug("The test for android.permission.RESOLVE_COMPONENT_FOR_UID is not implemented yet");
	}
	@PermissionTest(permission="RESERVED_FOR_TESTING_SIGNATURE",sdkMin=36)
	public void testReservedForTestingSignature(){
		logger.debug("The test for android.permission.RESERVED_FOR_TESTING_SIGNATURE is not implemented yet");
	}
	@PermissionTest(permission="SINGLE_USER_TIS_ACCESS",sdkMin=36)
	public void testSingleUserTisAccess(){
		logger.debug("The test for android.permission.SINGLE_USER_TIS_ACCESS is not implemented yet");
	}
	@PermissionTest(permission="ACCESS_TEXT_CLASSIFIER_BY_TYPE",sdkMin=36)
	public void testAccessTextClassifierByType(){
		logger.debug("The test for android.permission.ACCESS_TEXT_CLASSIFIER_BY_TYPE is not implemented yet");
	}

}









