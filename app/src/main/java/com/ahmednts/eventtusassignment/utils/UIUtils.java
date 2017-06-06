package com.ahmednts.eventtusassignment.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ProgressBar;

/**
 * Created by AhmedNTS on 6/2/2017.
 */
public class UIUtils {
    public static void setProgressBarColor(Context context, ProgressBar progressBar, int color) {
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN);
    }
}
