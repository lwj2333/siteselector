package com.lwj.siteselector.sqlite

import android.support.v4.util.ArrayMap


/**
 * @author   LWJ
 * @date on 2018/5/7
 * @describe 添加描述
 */
data class CityDBModel (var list:ArrayList<ArrayList<CityModel>>, var map : ArrayMap<Int, Int>,
                        var locationMap: ArrayMap<Int, String>, var  isFinish:Boolean=true)