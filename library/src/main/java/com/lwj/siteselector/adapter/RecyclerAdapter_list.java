package com.lwj.siteselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author by  LWJ
 * date on  2017/12/16.
 * describe 添加描述
 */
public abstract class RecyclerAdapter_list<T> extends RecyclerView.Adapter<ViewHolder>{
    private List<T> list;
    private Context context;
    private int layoutID;
    private LayoutInflater mInflater;
    protected RecyclerAdapter_list(Context context, List<T> list, int layoutID) {
        this.list = list;
        this.context = context;
        this.layoutID = layoutID;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(layoutID, parent, false);
        return new ViewHolder(context,view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        bindView(holder,list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list==null ? 0:list.size();
    }


   protected abstract void bindView(ViewHolder viewHolder, T bean,int position);


}
