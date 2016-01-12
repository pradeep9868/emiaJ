package co.jlabs.jaime.jaime.customcomponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import co.jlabs.jaime.jaime.Static_Catelog;


public class TextView_White extends TextView {


    public TextView_White(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public TextView_White(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public TextView_White(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}