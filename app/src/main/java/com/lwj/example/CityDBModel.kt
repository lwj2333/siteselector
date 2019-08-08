package com.lwj.example

import android.support.v4.util.ArrayMap


/**
 * @author BuyProductActivity  LWJ
 * @date on 2018/5/7
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
data class CityDBModel (var list:ArrayList<ArrayList<CityModel>>, var map : ArrayMap<Int, Int>,
                        var locationMap: ArrayMap<Int, String>, var  isFinish:Boolean=true)