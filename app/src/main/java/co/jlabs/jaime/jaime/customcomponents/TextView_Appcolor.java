package co.jlabs.jaime.jaime.customcomponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import co.jlabs.jaime.jaime.Static_Catelog;


public class TextView_Appcolor extends TextView {


    public TextView_Appcolor(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor(Static_Catelog.getcolorscheme(context)));
        }
    }

    public TextView_Appcolor(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor(Static_Catelog.getcolorscheme(context)));
        }
    }

    public TextView_Appcolor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf);
            this.setTextColor(Color.parseColor(Static_Catelog.getcolorscheme(context)));
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}