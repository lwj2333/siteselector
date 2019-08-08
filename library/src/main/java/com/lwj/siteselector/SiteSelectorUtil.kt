package com.lwj.siteselector


import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView

/**
 * author by  LWJ
 * date on  2018/4/28
 * describe 添加描述
 */
class SiteSelectorUtil {
    companion object {
        var sv: SelectorView? = null
        fun showDialog(context: Context, adapter: RecyclerView.Adapter<*>, listener: OnLocationListener?) {
            sv = SelectorView(context).setAdapter(adapter)
            sv?.setOnLocationListener(listener)
            sv?.show()
        }

        fun setOnDismiss(dialog: DialogInterface.OnDismissListener) {
            sv?.setOnDismissListener(dialog)
        }

        fun cancelDialog() {
            sv?.dismiss()
        }

        fun batchAddTab(list: List<String?>, count: Int) {
            sv?.getBar()?.batchAddTab(list, count)
        }

        fun addTab(s: String) {
            sv?.getBar()?.addTab(s)
        }

        fun addTabFinally(s: String) {
            sv?.getBar()?.addTabFinally(s)

        }

        fun changeBarTileChange(index: Int, s: String?, flag: Int) {
            sv?.getBar()?.changeBarTileChange(index, s, flag)
        }

        fun moveBarTile(index: Int) {
            sv?.getBar()?.moveBarTile(index)
        }

        fun setListener(listener: ScrollViewBar.OnIndexChangeListener) {
            sv?.getBar()?.setListener(listener)
        }

        fun skipToIndex(index: Int) {
            sv?.skipToIndex(index)
        }
    }


}