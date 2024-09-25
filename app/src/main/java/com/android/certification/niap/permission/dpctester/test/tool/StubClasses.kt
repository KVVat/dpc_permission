package com.android.certification.niap.permission.dpctester.test.tool

import android.net.netstats.provider.INetworkStatsProvider

//import android.net.netstats.provider.INetworkStatsProvider

//import android.net.netstats.provider.INetworkStatsProvider
/**
 * A shim class that allows [TestableNetworkStatsProviderBinder] to be built against
 * different SDK versions.
 */
open class NetworkStatsProviderStubCompat : INetworkStatsProvider.Stub() {
    override fun onRequestStatsUpdate(token: Int) {}

    // Removed and won't be called in S+.
    fun onSetLimit(iface: String?, quotaBytes: Long) {}

    override fun onSetAlert(bytes: Long) {}

    // Added in S.
    override fun onSetWarningAndLimit(iface: String?, warningBytes: Long, limitBytes: Long) {}
}

