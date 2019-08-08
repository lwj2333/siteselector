package com.lwj.example;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * author BuyProductActivity  LWJ
 * date on  2017/11/14.
 * describe 添加描述
 * org  http://www.gdjiuji.com(广东九极生物科技有限公司)
 */
public class App extends Application {
    private SharedPreferences share;
    private Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }



    private void init() {
        share = getSharedPreferences("config", MODE_PRIVATE);
        editor = share.edit();
    }

    /**
     * http://121.40.68.244:8080
     * 存进字符串
     *
     * @param key   建
     * @param value 值
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return share.getString(key, null);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return share.getInt(key, -1);
    }

    public void putBoolean(String key, boolean b) {
        editor.putBoolean(key, b);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return share.getBoolean(key, false);
    }

}
