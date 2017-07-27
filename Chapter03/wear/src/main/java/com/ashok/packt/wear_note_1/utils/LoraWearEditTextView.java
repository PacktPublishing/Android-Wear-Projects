package com.ashok.packt.wear_note_1.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ashok.kumar on 04/03/17.
 */

public class LoraWearEditTextView extends EditText {


    public LoraWearEditTextView(Context context) {
        super(context);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lora.ttf");
        setTypeface(tf ,1);
    }
}
