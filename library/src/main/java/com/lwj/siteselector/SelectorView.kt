package com.lwj.siteselector

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_selector.*

/**
 * author by  LWJ
 * date on  2018/4/28
 * describe 添加描述
 */
class SelectorView : Dialog {
    private var adapter: RecyclerView.Adapter<*>? = null
    private var title: String? = "所在地区"
    private var manager: LinearLayoutManager? = null

    constructor(context: Context?) : super(context, R.style.DialogTheme)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_selector)
        setCanceledOnTouchOutside(true)
        window.setGravity(Gravity.BOTTOM)
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 500f,
                context.resources.displayMetrics
            ).toInt()
        )
        window.setWindowAnimations(R.style.dialog_anim)
        initView()

    }

    private fun initView() {
        tv_title.text = title
        img_close.setOnClickListener {
            this.dismiss()
        }
        manager = LinearLayoutManager(context)
        rv_location.layoutManager = manager
        rv_location.setHasFixedSize(true)
        rv_location.adapter = adapter
    }
    fun setAdapter(adapter: RecyclerView.Adapter<*>): SelectorView {
        this.adapter = adapter
        return this
    }

    fun setTitle(text: String): SelectorView {
        this.title = text
        return this
    }

    fun getBar(): ScrollViewBar = this.sv_bar

    fun skipToIndex(index: Int) {
        val firstItem = manager?.findFirstVisibleItemPosition()
        val lastItem = manager?.findLastVisibleItemPosition()

        when {
            index <= firstItem!! -> rv_location.scrollToPosition(index)
            index <= lastItem!! -> {
                val top = rv_location.getChildAt(index - firstItem).top
                rv_location.scrollBy(0, top)
            }
            else -> rv_location.scrollToPosition(index)
        }
    }

    private var listener: OnLocationListener? = null
    fun setOnLocationListener(listener: OnLocationListener?) {
        this.listener = listener
    }

    override fun dismiss() {
        listener?.onFinish()
        super.dismiss()
    }


}