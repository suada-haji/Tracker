package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by suadahaji.
 */
public class ActivityLauncher {
    public static void runIntent(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

}
