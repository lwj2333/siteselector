package com.lwj.example

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.util.ArrayMap
import android.text.TextUtils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.lwj.siteselector.OnLocationListener
import com.lwj.siteselector.ScrollViewBar
import com.lwj.siteselector.SiteSelectorUtil
import com.lwj.siteselector.adapter.RecyclerAdapter_list
import com.lwj.siteselector.adapter.ViewHolder
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Rationale


/**
 * @author by  LWJ
 * @date on 2018/9/27
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
class SiteUtils constructor(var activity: Activity) {

//    private val app: App by lazy {
//        activity.applicationContext as App
//    }
//
//    fun setListener(listener: OnLocationListener) {
//        locationListener = listener
//    }
//
//
//    private var adapter: RecyclerAdapter_list<CityModel>? = null
//    fun initSite() {
//        adapter = object : RecyclerAdapter_list<CityModel>(activity, list, R.layout.item_location) {
//            override fun bindView(viewHolder: ViewHolder?, bean: CityModel?, position: Int) {
//                viewHolder?.setText(R.id.tv_location, bean?.cName)
//                val imgCheck = viewHolder?.getView(R.id.img_check) as ImageView?
//                if (bean?.isSelect!!) {
//                    imgCheck?.visibility = View.VISIBLE
//                } else {
//                    imgCheck?.visibility = View.GONE
//                }
//                val v: View? = viewHolder?.itemView
//                v?.tag = position
//                v?.setOnClickListener(listener)
//            }
//        }
//    }
//
//    @Synchronized
//    private fun importDB(force: Boolean = false,count: Int) {
//        if (force) {
//            CProgressDialogUtils.showProgressDialog(activity, "正在获取城市列表")
//        }
//        AndPermission.with(activity).runtime().permission(Permission.WRITE_EXTERNAL_STORAGE
//        ).rationale(mRationale).onGranted {
//            copyDB(force,count)
//        }.onDenied {
//            if (force) {
//                CProgressDialogUtils.cancelProgressDialog()
//            }
//            if (AndPermission.hasAlwaysDeniedPermission(activity, it)) {
//                // 这些权限被用户总是拒绝。
//                HintTwoDialog(activity, R.style.DialogTheme).setListener { dialog, b ->
//                    val settingService = AndPermission.with(activity).runtime().setting()
//                    if (b) {
//                        // 如果用户同意去设置：
//                        settingService.start()
//                    }
//                    dialog.dismiss()
//                }.setContent(activity.resources?.getString(R.string.permission_dialog_half_top) + transformText(it)
//                        + activity.resources?.getString(R.string.permission_dialog_half_bottom_0)).show()
//            }
//        }.start()
//    }
//
//    private fun transformText(permissions: List<String>): String {
//        val permissionNames = Permission.transformText(activity, permissions)
//        return TextUtils.join(",", permissionNames)
//    }
//
//    private val mRationale: Rationale<List<String>> = Rationale { context, data, executor ->
//        // 这里使用一个Dialog询问用户是否继续授权。
//        HintTwoDialog(context, R.style.DialogTheme).setListener { dialog, b ->
//            dialog.dismiss()
//            if (b) {
//                // 如果用户继续：
//                executor.execute()
//            } else {
//                // 如果用户中断：
//                executor.cancel()
//            }
//
//        }.setContent(activity.resources?.getString(R.string.permission_dialog_half_top) + transformText(data) +
//                activity.resources?.getString(R.string.permission_dialog_half_bottom_1))
//                .setCancel("下次吧").setEnsure("继续授权").show()
//    }
//    private val TAG = "SiteUtils"
//    @SuppressLint("CheckResult")
//    private fun copyDB(force: Boolean,count: Int) {
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
//                selectLocation(tempList,count-1)
//            }
//        }, {
//            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
//            if (force) {
//                CProgressDialogUtils.cancelProgressDialog()
//            }
//            app.putInt("CopyDB", 0)
//        })
//    }
//
//    private fun setLocation(vararg position: Int) {
//        locationListener?.onLocation(locationMap, position[0])
////        var str = ""
////        val list:ArrayList<String> = ArrayList()
////        for (i in 0 until locationMap.size) {
////
////            str += if (i == locationMap.size - 1) {
////                "${locationMap[i]}"
////            } else {
////                "${locationMap[i]}-"
////            }
////
////        }
//
//        //  tv_area.setText(str)
//    }
//
//    private fun mapToList() {
//        location.clear()
//        for (i in 0 until locationMap.size) {
//            if (TextUtils.isEmpty(locationMap[i])) {
//                return
//            }
//            location.add(locationMap[i])
//        }
//    }
//
//    private val tempList: ArrayList<String> = ArrayList()
//    private fun getLocation(siteList: List<String>?) {
//
//        if (locationMap.size > 0) {
//            mapToList()
//            return
//        }
//        // val text = "广西-桂林市"
//        if (siteList == null || siteList.isEmpty()) {
//            return
//        } else {
//            tempList.clear()
//            tempList.addAll(siteList)
//        }
//        location.addAll(siteList)
//        val manage: DBManage? = DBManage.getInstance(activity)
//        val city = manage?.queryLocation(location)
//        if (city != null) {
//            isSelectFinish = city.isFinish
//            listMap = city.map
//            listLocation = city.list
//            locationMap = city.locationMap
//        }
//    }
//
//    private val copyDB: Int by lazy {
//        app.getInt("CopyDB")
//    }
//    var isSelectFinish = true
//    fun selectLocation(siteList: List<String>?, count: Int = 1) {
//          Log.i(TAG,"SiteUtils: $ tt ")
//        getLocation(siteList)
//        if (listLocation.size > 0) {
//            listIndex = listLocation.size - 1
//            list.clear()
//            list.addAll(listLocation[listIndex])
//            val size = listMap.size
//            SiteSelectorUtil.showDialog(activity, this.adapter!!, locationListener)
//            SiteSelectorUtil.batchAddTab(location, listLocation.size)
//            if (size == listIndex) {
//                val i = listMap[listIndex]
//                if (i != null) {
//                    SiteSelectorUtil.skipToIndex(i)
//                }
//            }
//        } else {
//            if (initLocation(1, 0) == null) {
//                close()
//                if (count > 0) {
//                    importDB(true,count)
//                }else{
//                    Toast.makeText(activity,"城市列表导入失败",Toast.LENGTH_LONG).show()
//                }
//                return
//            } else {
//                SiteSelectorUtil.showDialog(activity, this.adapter!!, locationListener)
//            }
//        }
////                SiteSelectorUtil.setOnDismiss(DialogInterface.OnDismissListener {
////                    if (listMap.size < 3) {
////                        setDefault(3, 0)
////                    }
////                })
//
//        SiteSelectorUtil.setListener(object : ScrollViewBar.OnIndexChangeListener {
//            override fun onIndexChange(position: Int) {
//                if (locationMap.size <= 0) {
//                    return
//                }
//                listIndex = position
//                val index = listMap[position]
//                list.clear()
//                list.addAll(listLocation[position])
//
//                adapter!!.notifyDataSetChanged()
//                if (index != null) {
//                    SiteSelectorUtil.skipToIndex(index)
//                } else {
//                    SiteSelectorUtil.skipToIndex(0)
//                }
//            }
//
//        })
//    }
////    override fun onClick(v: View?) {
////        isFastDoubleClick(1000)
////        when (v?.id) {
////            R.id.img_bar_back ->
////                finish()
////            R.id.tv_ensure ->
////                ensure()
////            R.id.tv_area -> {
////
////            }
////        }
////    }
//
//
//    private var listener: View.OnClickListener = View.OnClickListener {
//        val position = it.tag as Int
//        setClickEvent(position)
//
//    }
//
//    private fun setClickEvent(position: Int) {
//        val index = listMap[listIndex]
//        if (index != null) {
//            list[index].isSelect = false
//        }
//        listMap[listIndex] = position
//        locationMap[listIndex] = list[position].cName
//
//        list[position].isSelect = true
//        val id = list[position].id
//        if (index != position) {
//            if (initLocation(id, 1) == null) {
//                isSelectFinish = true
//                for (k in listMap.size - 1 downTo listIndex + 1) {
//                    listMap.removeAt(k)
//                    locationMap.removeAt(k)
//                }
//                setLocation(id)
//                SiteSelectorUtil.changeBarTileChange(listIndex, list[position].cName, 0)
//                adapter?.notifyDataSetChanged()
//                SiteSelectorUtil.cancelDialog()
//            } else {
//                isSelectFinish = false
//                for (k in listMap.size - 1 downTo listIndex + 1) {
//                    listMap.removeAt(k)
//                    locationMap.removeAt(k)
//                }
//                setLocation(id)
//                SiteSelectorUtil.changeBarTileChange(listIndex, list[position].cName, 1)
//            }
//        } else {
//            SiteSelectorUtil.moveBarTile(listIndex)
//        }
//        //  setLocation(id)
//    }
//
//    private fun setDefault(count: Int, position: Int) {
//        val size = listMap.size
//        for (i in size until count) {
//            if (list.isNotEmpty()) {
//                val index = listMap[i]
//                if (index != null) {
//                    list[index].isSelect = false
//                }
//                listMap[i] = position
//                locationMap[i] = list[position].cName
//                list[position].isSelect = true
//                val manage: DBManage? = DBManage.getInstance(activity)
//                val location: ArrayList<CityModel>? = manage?.queryLocation(list[position].id)
//                if (location != null) {
//                    list.clear()
//                    list.addAll(location)
//                    listLocation.add(location)
//                } else {
//                    setLocation()
//                    return
//                }
//            } else {
//                setLocation()
//                return
//            }
//        }
//    }
//
//    private var list: ArrayList<CityModel> = ArrayList()
//    private var listLocation: ArrayList<ArrayList<CityModel>> = ArrayList()
//    private var listMap: ArrayMap<Int, Int> = ArrayMap()
//    private var locationMap: ArrayMap<Int, String> = ArrayMap()
//    private var listIndex: Int = 0
//    private var location: ArrayList<String?> = ArrayList()
//
//    private fun initLocation(locationID: Int, type: Int): ArrayList<CityModel>? {
//        val location: ArrayList<CityModel>? = DBManage.getInstance(activity)?.queryLocation(locationID)
//
//        if (location != null) {
//            when (type) {
//                0 -> {
//                    list.clear()
//                    list.addAll(location)
//                    adapter!!.notifyDataSetChanged()
//                    listLocation.add(location)
//                }
//                1 -> {
//                    for (i in listLocation.size - 1 downTo listIndex + 1) {
//                        listLocation.removeAt(i)
//                    }
//                    listLocation.add(location)
//                }
//            }
//        }
//        return location
//
//    }
//
//    /**
//     * 关闭数据库链接
//     */
//    fun close() {
//        DBManage.getInstance(activity)?.close()
//    }
//
//    private var locationListener: OnLocationListener? = null



}