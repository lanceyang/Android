package com.lance.library.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ZF_Develop_PC on 2017/11/7.
 */

public class SameWHFrameLayout extends FrameLayout {
    public SameWHFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SameWHFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SameWHFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height, width;
        if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(heightMeasureSpec)) {
            height = widthMeasureSpec;
            width = widthMeasureSpec;
        } else {
            width = heightMeasureSpec;
            height = heightMeasureSpec;
        }
        super.onMeasure(width, height);
    }
}
