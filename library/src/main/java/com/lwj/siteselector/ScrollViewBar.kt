package com.lwj.siteselector

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView



class ScrollViewBar : FrameLayout {
    private var mContext: Context? = null
    private var viewLine: View? = null
    private var viewGroup: LinearLayout? = null

    private var sizeList: ArrayList<Int> = ArrayList()
    private var viewList: ArrayList<TextView> = ArrayList()
    private var viewXCentre: ArrayList<Int> = ArrayList()
    private var textSize: Float = 0f
    private var margin: Int = 10
    private val default: String = "请选择"

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.ScrollViewBar)
        textSize = typedArray.getDimensionPixelSize(R.styleable.ScrollViewBar_textSize, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics).toInt()).toFloat()
        margin = typedArray.getDimensionPixelSize(R.styleable.ScrollViewBar_marginLtR, 0)
        typedArray.recycle()
        initView()
    }

    private fun initView() {
        out = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        val v: View = LayoutInflater.from(mContext).inflate(R.layout.view_scroll, null)
        val include: View = v.findViewById(R.id.include)
        viewLine = include.findViewById(R.id.v_line)
        viewGroup = include.findViewById(R.id.ll_bar)
        this.addView(v)
        newTextView(default, 0)

    }

    private val TAG = "ScrollViewBar"

    private fun newTextView(text: String?, flag: Int) {

        val index: Int = viewList.size
        val tv = TextView(mContext)
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        params.marginStart = margin
        params.marginEnd = margin
        tv.layoutParams = params
        tv.tag = index
        tv.gravity = Gravity.CENTER_VERTICAL
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tv.text = text


        tv.setOnClickListener(listener)
        viewGroup?.addView(tv)
        viewList.add(tv)
        initData(index, flag, 0)
        indexListener?.onIndexChange(index)
    }

    private var out: Int = 0
    private var currentView: TextView? = null
    private fun moveLine(index: Int, flag: Int) {
        val tv = viewList[index]
        if (tv==currentView){
            return
        }
        val width = sizeList[index] + out
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt())
        viewLine?.layoutParams = params
        val x = width / 2

        val dis = viewXCentre[index] - x
        tv.setTextColor( ContextCompat.getColor(mContext!!,R.color.red))
        currentView?.setTextColor(ContextCompat.getColor(mContext!!,R.color.black))
        currentView = tv
        when (flag) {
            0 -> Instrument.getInstance().slidingToX(viewLine, dis.toFloat())
            1 -> Instrument.getInstance().smoothToX(viewLine, dis.toFloat(), 300)
        }
    }

    private fun getXCentre(index: Int): Int {
        val marginSum: Int = (2 * index * margin) + margin

        var viewWidth = 0
        val size: Int = index + 1
        for (i in 0 until size) {
            viewWidth += if (i == size - 1) {
                sizeList[i] / 2
            } else {
                sizeList[i]
            }
        }

        return marginSum + viewWidth
    }


    private val listener: OnClickListener = OnClickListener {
        val index: String = it.tag.toString()
        //   Log.i(TAG, "ScrollViewBar:$index  移动 ")
        moveLine(index.toInt(), 1)

        indexListener?.onIndexChange(index.toInt())
    }

    fun batchAddTab(list: List<String?>,count:Int) {
        if (list.isEmpty()){
            return
        }
        val length = viewList.size - 1
        viewList[length].text = list[length]
        for (i in length+1 until count) {
            if (i<list.size){
                newTextView(list[i], 0)
            }else{
                newTextView(default, 0)
            }
        }
    }

    fun addTab(s: String) {
        val length = viewList.size - 1
        viewList[length].text = s
        newTextView(default, 1)
    }

    fun addTabFinally(s: String) {

        val length = viewList.size - 1
        viewList[length].text = s


    }

    fun changeBarTileChange(index: Int, s: String?, flag: Int) {
        if (index>=viewList.size){
            return
        }
        viewList[index].text = s

        when (flag) {
            0 -> {
                initData(index, 0, 2)
            }
            1 -> {
                for (i in viewList.size - 1 downTo index + 1) {
                    viewList.removeAt(i)
                    viewGroup?.removeViewAt(i)
                    sizeList.removeAt(i)
                    viewXCentre.removeAt(i)
                }
                newTextView(default, 1)
            }
        }
    }

    fun moveBarTile(index: Int) {
        moveLine(index, 1)
    }

    private fun initData(index: Int, flag: Int, type: Int) {

        when (type) {
            0 -> {
                getSizeList(index, 0)
                getSizeList(index - 1, 1)

                getViewXCentre(index, 0)
                getViewXCentre(index - 1, 1)

            }
            1 -> {
                sizeList.clear()
                viewXCentre.clear()
                for (i in 0 until viewList.size) {
                    getSizeList(index, 0)
                }
                for (i in 0 until viewList.size) {
                    getViewXCentre(index, 0)
                }
            }
            2 -> {
                getSizeList(index, 1)
                getViewXCentre(index, 1)
            }
        }
        moveLine(index, flag)
    }

    private fun getSizeList(index: Int, flag: Int) {
        if (index < 0) {
            return
        }
        val tv: TextView = viewList[index]
        val length: Int = tv.text.length
        val width = length * textSize.toInt()
        when (flag) {
            0 -> {
                sizeList.add(width)
            }
            1 -> {
                sizeList.add(index, width)
                sizeList.removeAt(index + 1)

            }
        }

    }

    private fun getViewXCentre(index: Int, flag: Int) {
        if (index < 0) {
            return
        }
        val width: Int = getXCentre(index)

        when (flag) {
            0 -> viewXCentre.add(width)
            1 -> {
                viewXCentre.add(index, width)
                viewXCentre.removeAt(index + 1)
            }
        }
    }

    private var indexListener: OnIndexChangeListener? = null
    fun setListener(listener: OnIndexChangeListener) {
        indexListener = listener
    }

    interface OnIndexChangeListener {
        fun onIndexChange(position: Int)
    }
}