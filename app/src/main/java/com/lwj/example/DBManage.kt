package com.lwj.example

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.support.v4.util.ArrayMap
import com.lwj.siteselector.sqlite.QueryOperation


/**
 * @author   LWJ
 * @date on 2018/4/25
 * @describe 添加描述
 */
class DBManage private constructor(){
    private val tableName = "tb_location"
    companion object {
        private var db: SQLiteDatabase? = null
        /*单例*/
        @Volatile
        private var INSTANCE: DBManage? = null

        /*获取单例*/
        fun getInstance(context: Context): DBManage? {
            if (INSTANCE == null) {
                synchronized(DBManage::class.java) {
                    if (INSTANCE == null) {
                        val dBName = "location.db"
                        val packageName = context.packageName
                        val path = Environment.getDataDirectory().absolutePath
                        val dBPath = "/data$path/$packageName/databases/$dBName"
                        INSTANCE = try {
                            db = SQLiteDatabase.openOrCreateDatabase(dBPath, null)
                            DBManage()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
            return INSTANCE
        }
    }
    var query :QueryOperation ?=null
    fun addQuery(query :QueryOperation){
        this.query =query
    }
    fun close() {
        db?.close()
        INSTANCE =null
    }

    fun queryLocation(locationID: Int): ArrayList<CityModel>? {

        var list: ArrayList<CityModel>? = null
        val sql = "select * from $tableName where ParentID=$locationID"
        val cursor = try {
            db?.rawQuery(sql, null)
        } catch (e: Exception) {
            null
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                list = ArrayList()
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("LocationID"))
                    val cName = cursor.getString(cursor.getColumnIndex("LocationName"))
                    list.add(CityModel(id, cName))
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()
        return list
    }

    fun queryLocation(str: String): Int {
        val sql = "select LocationID from $tableName where LocationName like \"$str\""
        val cursor = try {
            db?.rawQuery(sql, null)
        } catch (e: Exception) {
            null
        }
        var id = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndex("LocationID"))
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()
        return id
    }

    fun queryLocation(strList: List<String?>): CityDBModel {
        val list: ArrayList<ArrayList<CityModel>> = ArrayList()
        val map: ArrayMap<Int, Int> = ArrayMap()
        val locationMap: ArrayMap<Int, String> = ArrayMap()
        var index = 0
        var listDB = queryLocation(1)
        var canAdd = true
        while (listDB != null && index < strList.size) {
            list.add(listDB)
            canAdd = false
            for (i in 0 until listDB!!.size) {
                if (listDB[i].cName == strList[index]) {
                    map[index] = i
                    locationMap[index] = strList[index]
                    listDB[i].isSelect = true
                    listDB = queryLocation(listDB[i].id)
                    canAdd = true
                    break
                } else if (i == listDB.size - 1) {
                    listDB = null
                }
            }
            index++
        }
        var b = true
        if (canAdd && listDB != null) {
            list.add(listDB)
            b = false
        }
        return CityDBModel(list, map, locationMap, b)
    }


}