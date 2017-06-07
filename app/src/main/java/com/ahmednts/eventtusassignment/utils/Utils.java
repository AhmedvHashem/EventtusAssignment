package com.ahmednts.eventtusassignment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AhmedNTS on 6/6/2017.
 */

public class Utils {

    public static void setText(TextView textView, String text, boolean hideIfEmpty) {
        textView.setVisibility(View.VISIBLE);

        if (text != null) {
            if (text.isEmpty() && hideIfEmpty)
                textView.setVisibility(View.GONE);
            else
                textView.setText(text);
        } else
            textView.setText("");
    }

    public static boolean isOnline(Context context) {
        boolean connected;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return connected;
    }
}
