package com.example.library_getlocationstatus

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import java.lang.StringBuilder

@SuppressLint
internal lateinit var mActivity: Context


fun getLocationStatus(mythis: Context, builder: StringBuilder): String{
    mActivity = mythis

    // Find location status
    val manager = mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        builder.append(mythis.getString(R.string.no_gps))
    } else {
        builder.append(mythis.getString(R.string.yes_gps))
    }
    return builder.toString()
}