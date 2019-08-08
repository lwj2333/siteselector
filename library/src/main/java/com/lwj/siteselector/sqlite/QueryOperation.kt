package com.lwj.siteselector.sqlite

interface QueryOperation {
    fun queryLocation(locationID: Int): ArrayList<CityModel>?
    fun queryLocation(strList: List<String?>): CityDBModel
}