package com.lwj.siteselector

interface DialogSetting {
    /**
     * @param type 0 初始化  1 查找下一项
     */
    fun showDialog(type:Int)
    fun cancelDialog()
}