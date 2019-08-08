package com.lwj.siteselector

import android.support.v4.util.ArrayMap


/**
 * author by  LWJ
 * date on  2018/9/28.
 * describe 添加描述
 */
interface OnLocationListener {
    fun onLocation(location: ArrayMap<Int, String>, vararg locationID: Int)
    fun onFinish()
}