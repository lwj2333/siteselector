package com.lwj.example

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface

/**
 * @author by  LWJ
 * @date on 2018/10/21
 * @describe 添加描述
 * @org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
class  CProgressDialogUtils private constructor() {
    companion object  {
       private var sCircleProgressDialog: ProgressDialog? = null
       fun showProgressDialog(activity: Activity) {
           showProgressDialog(activity, "加载中", false, null)
       }

       fun showProgressDialog(activity: Activity, listener: DialogInterface.OnCancelListener) {
           showProgressDialog(activity, "加载中", true, listener)
       }

       fun showProgressDialog(activity: Activity, msg: String) {
           showProgressDialog(activity, msg, true, null)
       }

       fun showProgressDialog(activity: Activity, msg: String, listener: DialogInterface.OnCancelListener) {
           showProgressDialog(activity, msg, true, listener)
       }

       fun showProgressDialog(activity: Activity, msg: String, cancelable: Boolean) {
           showProgressDialog(activity, msg, cancelable, null)
       }

       fun showProgressDialog(activity: Activity?, msg: String, cancelable: Boolean, listener: DialogInterface.OnCancelListener?) {
           if (activity == null || activity.isFinishing) {
               return
           }
           if (sCircleProgressDialog == null) {
               sCircleProgressDialog = ProgressDialog(activity)
               sCircleProgressDialog?.setMessage(msg)
               sCircleProgressDialog?.ownerActivity = activity
               sCircleProgressDialog?.setOnCancelListener(listener)
               sCircleProgressDialog?.setCancelable(cancelable)
           } else {
               if (activity == sCircleProgressDialog?.ownerActivity) {
                   sCircleProgressDialog?.setMessage(msg)
                   sCircleProgressDialog?.setCancelable(cancelable)
                   sCircleProgressDialog?.setOnCancelListener(listener)
               } else {
                   //不相等,所以取消任何ProgressDialog
                   cancelProgressDialog()
                   sCircleProgressDialog = ProgressDialog(activity)
                   sCircleProgressDialog?.setMessage(msg)
                   sCircleProgressDialog?.setCancelable(cancelable)
                   sCircleProgressDialog?.ownerActivity = activity
                   sCircleProgressDialog?.setOnCancelListener(listener)
               }
           }

           if (!sCircleProgressDialog!!.isShowing) {
               sCircleProgressDialog?.show()
           }

       }


       fun cancelProgressDialog(activity: Activity) {
           if (sCircleProgressDialog != null && sCircleProgressDialog!!.isShowing) {
               if (sCircleProgressDialog!!.ownerActivity === activity) {
                   sCircleProgressDialog!!.cancel()
                   sCircleProgressDialog = null
               }
           }
       }

       fun cancelProgressDialog() {
           if (sCircleProgressDialog != null && sCircleProgressDialog!!.isShowing) {
               sCircleProgressDialog!!.cancel()
               sCircleProgressDialog = null
           }
       }
    }
}