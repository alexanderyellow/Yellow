package com.yellow.adviceby.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SheykinAV on 27.08.2015.
 */
public class MToast {

    public static void createToast(Context context, String text) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
