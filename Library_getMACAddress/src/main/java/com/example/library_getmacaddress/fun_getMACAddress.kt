package com.example.library_getmacaddress

import android.annotation.SuppressLint
import android.content.Context
import java.net.NetworkInterface
import java.util.*
@SuppressLint
internal lateinit var mActivity: Context

fun getMACAddress(mythis: Context, builder: StringBuilder): String {
    val res1 = StringBuilder()

    try {
        mActivity = mythis
        // Get MAC address
        // get all the interfaces
        val all = Collections.list(NetworkInterface.getNetworkInterfaces())
        //find network interface wlan0
        for (networkInterface in all) {
            if (!networkInterface.name.equals("wlan0", ignoreCase = true)) continue
            //get the hardware address (MAC) of the interface
            val macBytes = networkInterface.hardwareAddress
            if (macBytes == null) {
                //return "";
                return builder.append(mActivity.getString(R.string.no_mac_address), mActivity)
                    .toString()
            }
            for (b in macBytes) {
                res1.append(String.format("%02X", b) + ":")
            }
            if (res1.isNotEmpty()) {
                res1.deleteCharAt(res1.length - 1)
            }
        }
    } catch (ex: Exception) { // Handles when macBytes us null. Never happened yet.
        return builder.append(
            mActivity.getString(R.string.mac_address),
            "GetMACAddress Exception " + ex.message,
            mActivity
        ).toString()
    }
    return builder.append(
        mActivity.getString(R.string.mac_address), res1.toString()
    ).toString()
}