package com.lwj.example

import android.content.Context
import android.text.TextUtils


import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.Rationale

/**
 * @author by  LWJ
 * @date on 2018/9/17
 * @describe 添加描述
 */
class PermissionUtil {
    companion object {
        fun checkoutPermission(mContext: Context, listener: PermissionCallBack, vararg permission: String) {
            val mRationale: Rationale<List<String>> = Rationale { context, data, executor ->
                // 这里使用一个Dialog询问用户是否继续授权。
                HintTwoDialog(context, R.style.DialogTheme).setListener { dialog, b ->
                    dialog.dismiss()
                    if (b) {
                        // 如果用户继续：
                        executor.execute()
                    } else {
                        // 如果用户中断：
                        executor.cancel()
                    }
                }.setContent(mContext.resources?.getString(R.string.permission_dialog_half_top) + transformText(mContext, data) +
                        mContext.resources?.getString(R.string.permission_dialog_half_bottom_1))
                        .setCancel("下次吧").setEnsure("继续授权").show()
            }
            AndPermission.with(mContext).runtime().permission(permission
            ).rationale(mRationale).onGranted {
                listener.callBack()
            }.onDenied {
                if (AndPermission.hasAlwaysDeniedPermission(mContext, it)) {
                    // 这些权限被用户总是拒绝。
                    HintTwoDialog(mContext, R.style.DialogTheme).setListener { dialog, b ->
                        val settingService = AndPermission.with(mContext).runtime().setting()
                        if (b) {
                            // 如果用户同意去设置：
                            settingService.start()
                        }
                        dialog.dismiss()
                    }.setContent(mContext.resources.getString(R.string.permission_dialog_half_top) + transformText(mContext, it)
                            + mContext.resources.getString(R.string.permission_dialog_half_bottom_0)).show()
                }
            }.start()
        }

        private fun transformText(mContext: Context?, permissions: List<String>): String {
            val permissionNames = Permission.transformText(mContext, permissions)
            return TextUtils.join(",", permissionNames)
        }

    }

    interface PermissionCallBack {
        fun callBack()
    }
}