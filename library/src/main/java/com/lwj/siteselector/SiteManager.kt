package com.lwj.siteselector


import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.lwj.siteselector.adapter.RecyclerAdapter_list
import com.lwj.siteselector.adapter.ViewHolder
import com.lwj.siteselector.sqlite.CityModel
import com.lwj.siteselector.sqlite.CopyDB
import com.lwj.siteselector.sqlite.DBManage
import com.lwj.siteselector.sqlite.QueryOperation

class SiteManager private constructor(builder: Builder, var mContext: Context?) {

    private var mDialogSetting: DialogSetting? = null
    private var mQuery: QueryOperation? = null
    private var locationListener: OnLocationListener? = null
    private var dbPath: String? = null
    private var dbName: String? = null
    private var tableName: String? = null
    private var dbResource: Int = -1

    init {
        mDialogSetting = builder.mDialogSetting
        mQuery = builder.mQuery
        locationListener = builder.locationListener
        dbPath = builder.dbPath
        dbName = builder.dbName
        tableName = builder.tableName
        dbResource = builder.dbResource
    }

    private var share: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private fun initSharedPreferences() {
        share = mContext!!.getSharedPreferences("config", Context.MODE_PRIVATE)
        editor = share?.edit()
        copyDB = share?.getInt("CopyDB", 0) ?: 0
    }

    private fun putInt(key: String, value: Int) {
        editor?.putInt(key, value)
        editor?.apply()
    }

    var mHandler: Handler = Handler()


    private var adapter: RecyclerAdapter_list<CityModel>? = null
    fun initSite() {
        initSharedPreferences()
        adapter = object : RecyclerAdapter_list<CityModel>(mContext, list, R.layout.item_location) {
            override fun bindView(viewHolder: ViewHolder?, bean: CityModel?, position: Int) {
                viewHolder?.setText(R.id.tv_location, bean?.cName)
                val imgCheck = viewHolder?.getView(R.id.img_check) as ImageView?
                if (bean?.isSelect!!) {
                    imgCheck?.visibility = View.VISIBLE
                } else {
                    imgCheck?.visibility = View.GONE
                }
                val v: View? = viewHolder?.itemView
                v?.tag = position
                v?.setOnClickListener(listener)
            }
        }
    }

    private fun importDB(force: Boolean = false, count: Int) {
        if (force) {
            mDialogSetting?.showDialog(0)
        }
        copyDB(force, count)
    }

    private fun copyDB(force: Boolean, count: Int) {
        Thread(Runnable {
            try {
                val value = if (force) {
                    CopyDB(mContext!!).checkDB(dbResource, dbName, dbPath, force)
                } else {
                    val cover: Boolean = copyDB == 0
                    CopyDB(mContext!!).checkDB(dbResource, dbName, dbPath, cover)
                }
                putInt("CopyDB", value)
                mHandler.post {
                    if (force) {
                        mDialogSetting?.cancelDialog()
                        selectLocation(tempList, count - 1)
                    }
                }
            } catch (e: Exception) {
                putInt("CopyDB", 0)
                mHandler.post {
                    Toast.makeText(mContext!!, e.toString(), Toast.LENGTH_LONG).show()
                    if (force) {
                        mDialogSetting?.cancelDialog()
                    }
                }
            }
        }).start()
    }

//    @SuppressLint("CheckResult")
//    private fun copyDB(force: Boolean, count: Int) {
//        Observable.create<Int> {
//            val value = if (force) {
//                CopyDB(activity).checkDB(force)
//            } else {
//                val cover: Boolean = copyDB == 0
//                CopyDB(activity).checkDB(cover)
//            }
//            it.onNext(value)
//        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
//            app.putInt("CopyDB", it)
//            if (force) {
//                CProgressDialogUtils.cancelProgressDialog()
//                selectLocation(tempList, count - 1)
//            }
//        }, {
//            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
//            if (force) {
//                CProgressDialogUtils.cancelProgressDialog()
//            }
//            app.putInt("CopyDB", 0)
//        })
//    }

    private fun setLocation(vararg position: Int) {
        locationListener?.onLocation(locationMap, position[0])
//        var str = ""
//        val list:ArrayList<String> = ArrayList()
//        for (i in 0 until locationMap.size) {
//
//            str += if (i == locationMap.size - 1) {
//                "${locationMap[i]}"
//            } else {
//                "${locationMap[i]}-"
//            }
//
//        }

        //  tv_area.setText(str)
    }

    private fun mapToList() {
        location.clear()
        for (i in 0 until locationMap.size) {
            if (TextUtils.isEmpty(locationMap[i])) {
                return
            }
            location.add(locationMap[i])
        }
    }

    private val tempList: ArrayList<String> = ArrayList()
    private fun getLocation(siteList: List<String>?) {

        if (locationMap.size > 0) {
            mapToList()
            return
        }
        // val text = "广西-桂林市"
        if (siteList == null || siteList.isEmpty()) {
            return
        } else {
            tempList.clear()
            tempList.addAll(siteList)
        }
        location.addAll(siteList)
        val manage: DBManage? = DBManage.getInstance(mContext!!, tableName, dbName, dbPath)
        val city = manage?.queryLocation(location)
        if (city != null) {
            isSelectFinish = city.isFinish
            listMap = city.map
            listLocation = city.list
            locationMap = city.locationMap
        }
    }

    private var copyDB: Int = 0
    var isSelectFinish = true
    private val TAG = "SiteManager"
    fun selectLocation(siteList: List<String>?, count: Int = 1) {

        getLocation(siteList)
        if (listLocation.size > 0) {
            listIndex = listLocation.size - 1
            list.clear()
            list.addAll(listLocation[listIndex])
            val size = listMap.size
            SiteSelectorUtil.showDialog(mContext!!, this.adapter!!, locationListener)
            SiteSelectorUtil.batchAddTab(location, listLocation.size)
            if (size == listIndex) {
                val i = listMap[listIndex]
                if (i != null) {
                    SiteSelectorUtil.skipToIndex(i)
                }
            }
        } else {
            if (initLocation(1, 0) == null) {
                close()
                if (count > 0) {
                    importDB(true, count)
                } else {
                    Toast.makeText(mContext, "城市列表导入失败", Toast.LENGTH_LONG).show()
                }
                return
            } else {
                SiteSelectorUtil.showDialog(mContext!!, this.adapter!!, locationListener)
            }
        }
//                SiteSelectorUtil.setOnDismiss(DialogInterface.OnDismissListener {
//                    if (listMap.size < 3) {
//                        setDefault(3, 0)
//                    }
//                })

        SiteSelectorUtil.setListener(object : ScrollViewBar.OnIndexChangeListener {
            override fun onIndexChange(position: Int, finish: Boolean) {
                if (finish) {
                    Log.i(TAG, "SiteManager: $  结束 ")
                    SiteSelectorUtil.cancelDialog()
                    return
                }
                if (locationMap.size <= 0) {
                    return
                }
                listIndex = position
                val index = listMap[position]
                list.clear()
                list.addAll(listLocation[position])

                adapter!!.notifyDataSetChanged()
                if (index != null) {
                    SiteSelectorUtil.skipToIndex(index)
                } else {
                    SiteSelectorUtil.skipToIndex(0)
                }
            }

        })
    }
//    override fun onClick(v: View?) {
//        isFastDoubleClick(1000)
//        when (v?.id) {
//            R.id.img_bar_back ->
//                finish()
//            R.id.tv_ensure ->
//                ensure()
//            R.id.tv_area -> {
//
//            }
//        }
//    }


    private var listener: View.OnClickListener = View.OnClickListener {
        val position = it.tag as Int
        setClickEvent(position)
    }

    private fun setClickEvent(position: Int) {
        Log.i(TAG, "SiteManager: $position  ")
        val index = listMap[listIndex]
        if (index != null) {
            list[index].isSelect = false
        }
        listMap[listIndex] = position
        locationMap[listIndex] = list[position].cName

        list[position].isSelect = true
        val id = list[position].id
        if (index != position) {
            if (initLocation(id, 1) == null) {
                //最后一级
                isSelectFinish = true
                for (k in listMap.size - 1 downTo listIndex + 1) {
                    listMap.removeAt(k)
                    locationMap.removeAt(k)
                }
                setLocation(id)
                SiteSelectorUtil.changeBarTileChange(listIndex, list[position].cName, 0)
                adapter?.notifyDataSetChanged()
                SiteSelectorUtil.cancelDialog()
            } else {
                //还有下一级

                isSelectFinish = false
                for (k in listMap.size - 1 downTo listIndex + 1) {
                    listMap.removeAt(k)
                    locationMap.removeAt(k)
                }
                setLocation(id)
                SiteSelectorUtil.changeBarTileChange(listIndex, list[position].cName, 1)
            }
        } else {
            //重复点击已选中的
            SiteSelectorUtil.moveBarTile(listIndex)
        }
        //  setLocation(id)
    }

    private fun setDefault(count: Int, position: Int) {
        val size = listMap.size
        for (i in size until count) {
            if (list.isNotEmpty()) {
                val index = listMap[i]
                if (index != null) {
                    list[index].isSelect = false
                }
                listMap[i] = position
                locationMap[i] = list[position].cName
                list[position].isSelect = true
                val manage: DBManage? = DBManage.getInstance(mContext!!, tableName, dbName, dbPath)
                val location: ArrayList<CityModel>? = manage?.queryLocation(list[position].id)
                if (location != null) {
                    list.clear()
                    list.addAll(location)
                    listLocation.add(location)
                } else {
                    setLocation()
                    return
                }
            } else {
                setLocation()
                return
            }
        }
    }

    private var list: ArrayList<CityModel> = ArrayList()
    private var listLocation: ArrayList<ArrayList<CityModel>> = ArrayList()
    private var listMap: ArrayMap<Int, Int> = ArrayMap()
    private var locationMap: ArrayMap<Int, String> = ArrayMap()
    private var listIndex: Int = 0
    private var location: ArrayList<String?> = ArrayList()

    private fun initLocation(locationID: Int, type: Int): ArrayList<CityModel>? {
        val location: ArrayList<CityModel>? = DBManage.getInstance(
            mContext!!, tableName,
            dbName, dbPath
        )?.queryLocation(locationID)
        if (location != null) {
            when (type) {
                0 -> {
                    list.clear()
                    list.addAll(location)
                    adapter!!.notifyDataSetChanged()
                    listLocation.add(location)
                }
                1 -> {
                    for (i in listLocation.size - 1 downTo listIndex + 1) {
                        listLocation.removeAt(i)
                    }
                    listLocation.add(location)
                }
            }
        }
        return location

    }

    /**
     * 关闭数据库链接
     */
    fun close() {
        DBManage.getInstance(mContext!!, tableName, dbName, dbPath)?.close()
    }


    object Builder {

        var mDialogSetting: DialogSetting? = null
        var mQuery: QueryOperation? = null
        var locationListener: OnLocationListener? = null
        var dbPath: String? = null
        var dbName: String? = null
        var tableName: String? = null
        var dbResource: Int = -1


        fun setDBName(dbName: String?): Builder {
            this.dbName = dbName
            return this
        }

        fun setTableName(tableName: String?): Builder {
            this.tableName = tableName
            return this
        }

        fun setDBPath(dbPath: String?): Builder {
            this.dbPath = dbPath
            return this
        }

        fun setDBResource(dbResource: Int): Builder {
            this.dbResource = dbResource
            return this
        }

        fun setResultListener(listener: OnLocationListener): Builder {
            locationListener = listener
            return this
        }

        fun setDialogSetting(dialogSetting: DialogSetting): Builder {
            mDialogSetting = dialogSetting
            return this
        }

        fun setQueryOperation(query: QueryOperation): Builder {
            mQuery = query
            return this
        }

        fun build(mContext: Context?): SiteManager {
            return SiteManager(this, mContext)
        }
    }


}