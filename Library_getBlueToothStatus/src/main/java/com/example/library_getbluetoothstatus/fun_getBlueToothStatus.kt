package com.example.library_getbluetoothstatus

import android.bluetooth.BluetoothAdapter
import android.content.Context
import java.lang.StringBuilder


fun getBlueToothStatus(mythis: Context, builder: StringBuilder): String {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled) {
        builder.append(mythis.getString(R.string.bluetooth))
        // Device does not support Bluetooth
    } else {
        builder.append(mythis.getString(R.string.no_bluetooth))
    }
    return builder.toString()
}
