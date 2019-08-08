package com.lwj.siteselector;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author by  LWJ
 * date on  2017/12/12.
 * describe 添加描述
 */
public class Instrument {
    private static Instrument instrument;
    public static Instrument getInstance() {
        if (instrument == null) {
            synchronized (Instrument.class) {
                if (instrument == null) {
                    instrument = new Instrument();
                    return instrument;
                }
            }
        }
        return instrument;
    }

    private Instrument() {
    }

    public float getTransLationY(View v) {
        if (v == null) {
            return 0;
        }

        return v.getTranslationY();
    }

    public void slidingByDeltaToY(View v, float delta) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        v.setTranslationY(delta);
    }
    public void slidingByDeltaToX(View v, float delta) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        v.setTranslationX(delta);
    }
    public void slidingToY(View v, float y) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        v.setY(y);
    }
    public void slidingToX(View v, float x) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        v.setX(x);
    }
    public void reset(View v, long duration) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        ObjectAnimator.ofFloat(v, "translationY", 0F).setDuration(duration).start();
    }

    public void smoothToY(View v, float y, long duration) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        ObjectAnimator.ofFloat(v, "translationY", y).setDuration(duration).start();
    }
    public void smoothToX(View v, float x, long duration) {
        if (v == null) {
            return;
        }
        v.clearAnimation();
        ObjectAnimator.ofFloat(v, "translationX", x).setDuration(duration).start();
    }
}
