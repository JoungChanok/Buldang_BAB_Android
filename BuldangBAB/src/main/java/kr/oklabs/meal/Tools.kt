package kr.oklabs.meal

import android.content.Context
import android.net.ConnectivityManager

internal object Tools {

    fun isOnline(mContext: Context): Boolean {
        val mManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = mManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}