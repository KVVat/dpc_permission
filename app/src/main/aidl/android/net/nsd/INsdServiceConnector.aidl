
/**
 * Copyright (c) 2021, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.net.nsd;

import android.net.nsd.AdvertisingRequest;
import android.net.nsd.DiscoveryRequest;
import android.net.nsd.INsdManagerCallback;
import android.net.nsd.IOffloadEngine;
import android.net.nsd.NsdServiceInfo;
import android.os.Messenger;

/**
 * Interface that NsdService implements for each NsdManager client.
 *
 * {@hide}
 */
interface INsdServiceConnector {
    void registerService(int listenerKey, in AdvertisingRequest advertisingRequest);
    void unregisterService(int listenerKey);
    void discoverServices(int listenerKey, in DiscoveryRequest discoveryRequest);
    void stopDiscovery(int listenerKey);
    void resolveService(int listenerKey, in NsdServiceInfo serviceInfo);
    void startDaemon();
    void stopResolution(int listenerKey);
    void registerServiceInfoCallback(int listenerKey, in NsdServiceInfo serviceInfo);
    void unregisterServiceInfoCallback(int listenerKey);
    void registerOffloadEngine(String ifaceName, in IOffloadEngine cb, long offloadCapabilities, long offloadType);
    void unregisterOffloadEngine(in IOffloadEngine cb);
}