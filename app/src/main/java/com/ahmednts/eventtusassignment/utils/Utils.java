package com.ahmednts.eventtusassignment.utils;

import android.view.View;
import android.widget.TextView;

/**
 * Created by AhmedNTS on 6/6/2017.
 */

public class Utils {

    public static void setText(TextView textView, String text, boolean hideIfEmpty) {
        if ((text == null || text.isEmpty()) && hideIfEmpty)
            textView.setVisibility(View.GONE);
        else
            textView.setText(text);
    }
}
