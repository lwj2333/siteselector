package com.lwj.siteselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



/**
 * author by  LWJ
 * date on  2017/12/16.
 * describe 添加描述
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private int type;
    private View itemView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        this.itemView = itemView;

    }

    public ViewHolder(Context context, View itemView, int type) {
        super(itemView);
        mContext = context;
        this.itemView = itemView;
        this.type = type;
    }

    public View getView(int id) {
        return itemView.findViewById(id);
    }

    public View getItemView() {
        return itemView;
    }

    public void setText(int id, String string) {
        TextView textView = (TextView) getView(id);
        textView.setText(string);

    }

    public void setEditText(int id, String string) {
        EditText editText = (EditText) getView(id);
        editText.setText(string);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
