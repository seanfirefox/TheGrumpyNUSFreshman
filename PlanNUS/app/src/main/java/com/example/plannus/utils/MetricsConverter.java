package com.example.plannus.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class MetricsConverter {
    public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
