package com.example.library_isitrooted

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import java.lang.StringBuilder

fun isItRooted(mythis: Context, builder: StringBuilder): String {
//  https://stackoverflow.com/questions/3424195/determining-if-an-android-device-is-rooted-programmatically
//  https://github.com/scottyab/rootbeer/blob/master/README.md
    val rootBeer = RootBeer(mythis)
    if (rootBeer.isRooted()) {
        //we found indication of root
        builder.append(mythis.getString(R.string.isRooted))
    } else {
        //we didn't find indication of root
        builder.append(mythis.getString(R.string.isNotRooted))

    }
    return builder.toString()
}