package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.R;


/**
 * Created by suadahaji.
 */
public class CustomTextViewFont extends TextView {

    public CustomTextViewFont(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "digitals.ttf"));
        this.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
