@file:Suppress("DEPRECATION")
package com.example.library_getnetworkstatus

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.format.Formatter
import java.lang.StringBuilder

@SuppressLint
internal lateinit var mActivity: Context
internal lateinit var cm: ConnectivityManager
internal var mWirelessNetworkName: String? = null
internal var mIP_address: String? = null


fun getNetworkStatus(mythis: Context, builder: StringBuilder): String {
    mActivity = mythis

    cm = mActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = cm.activeNetworkInfo
    val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

    // This must be tested on a real phone (isConnected)
    if (isConnected) {
        if (activeNetwork?.type == ConnectivityManager.TYPE_MOBILE) {
            // Get System TELEPHONY service reference
            val tManager = mythis.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            // Get carrier name (Network Operator Name)
            val carrierName = tManager.networkOperatorName
            //String voiceMailNumber=tManager.getVoiceMailNumber(); permission problem TODO
            // Get Phone model and manufacturer name
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL

            //Get the phone type
            var strphoneType: String
            val phoneType = tManager.phoneType

            when (phoneType) {
                TelephonyManager.PHONE_TYPE_CDMA -> strphoneType = "CDMA"
                TelephonyManager.PHONE_TYPE_GSM -> strphoneType = "GSM"
                TelephonyManager.PHONE_TYPE_NONE -> strphoneType = "NONE"
                else -> { // this will never happen but stops FindBugs from complaining.
                    strphoneType = "Invalid phone type"
                }
            }
            builder.append(
                mActivity.getString(R.string.mobile_connection), carrierName,
                "\nPhone type: ", strphoneType,
                "\nManufacture: ", manufacturer,
                "\nModel: ", model).toString()
//
//            textMessage.append(
//                mActivity.getString(R.string.mobile_connection), carrierName,
//                "\nPhone type: ", strphoneType,
//                "\nManufacture: ", manufacturer,
//                "\nModel: ", model
//            ).toString()
        } else if (activeNetwork?.type == ConnectivityManager.TYPE_WIFI) {

            val wifiMgr: WifiManager =
                mActivity.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiMgr.connectionInfo

//                In the connected state, access to the SSID and BSSID requires
//                permissions (ACCESS_FINE_LOCATION ).
//                If such access is not allowed, WifiInfo#getSSID will return "<unknown ssid>"
//                and WifiInfo#getBSSID will return "02:00:00:00:00:00".
//
            mWirelessNetworkName = wifiInfo.ssid

            if (mWirelessNetworkName != null) {


//                textMessage.append(getString(R.string.wirelessNetworkName, mWirelessNetworkName))

               builder.append(
                    mythis.getString(R.string.wirelessNetworkName), mWirelessNetworkName
                ).toString()
            }
            mIP_address = Formatter.formatIpAddress(wifiMgr.connectionInfo.ipAddress)

            builder.append(
                mActivity.getString(R.string.wifi_connection), mIP_address
            ).toString()
//            textMessage.append(getString(R.string.wifi_connection, mIPaddress))
        } else {
//            GetMACAddress(mActivity)
        }
    } else {
        builder.append(
            mActivity.getString(R.string.no_wifi_or_mobile)
        ).toString()

//        textMessage.append(getString(R.string.no_wifi_or_mobile))


    }
    return builder.toString()
}
