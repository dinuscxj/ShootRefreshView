package com.dinuscxj.shootrefreshview;

import android.content.Context;

public class DensityUtil {

    public static float dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }  
}