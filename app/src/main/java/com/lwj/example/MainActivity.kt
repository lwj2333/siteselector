package com.lwj.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.util.Log
import com.lwj.siteselector.DialogSetting
import com.lwj.siteselector.OnLocationListener
import com.lwj.siteselector.SiteManager
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var site :SiteManager ?=null
    var last:Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         site = SiteManager.Builder.setResultListener(object :OnLocationListener{
             override fun onLocation(
                 location: ArrayMap<Int, String>,
                 locationID: Int,
                 isLast: Boolean
             ) {
                 Log.i(TAG,"MainActivity: $location  $locationID  $isLast")
                 last =isLast
             }


            override fun onFinish() {
            Log.i(TAG,"MainActivity: $last  完成")
            }
        }).setDialogSetting(object :DialogSetting{
            override fun showDialog(type: Int) {
                Log.i(TAG,"MainActivity: $  出现")
              CProgressDialogUtils.showProgressDialog(this@MainActivity)
            }
            override fun cancelDialog() {
                Log.i(TAG,"MainActivity: $  消失")
                CProgressDialogUtils.cancelProgressDialog()
            }
        })
//             .setQueryOperation(object :QueryOperation{
//            override fun queryLocation(locationID: Int): ArrayList<CityModel>? {
//                  Log.i(TAG,"MainActivity: 查询 $locationID  ")
//                return null
//            }
//
//            override fun queryLocation(strList: List<String?>): CityDBModel? {
//               return null
//            }
//
//        })
             .build(this)
        site?.initSite()
        tv1.setOnClickListener {
            //  val str: String = tv_area?.text.toString()
            PermissionUtil.checkoutPermission(this, object : PermissionUtil.PermissionCallBack {
                override fun callBack() {
                    site?.selectLocation(null)
                }
            }, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)

        }

    }
    private val TAG ="MainActivity"
    override fun onDestroy() {
        site?.close()
        super.onDestroy()
    }
}
