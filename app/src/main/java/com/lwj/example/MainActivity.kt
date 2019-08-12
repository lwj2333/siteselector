package com.lwj.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lwj.siteselector.SiteManager
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val site = SiteManager.Builder.build(this)
        site.initSite()
        tv1.setOnClickListener {
            //  val str: String = tv_area?.text.toString()
            PermissionUtil.checkoutPermission(this, object : PermissionUtil.PermissionCallBack {
                override fun callBack() {
                    site.selectLocation(null)
                }
            }, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)

        }
    }
}
