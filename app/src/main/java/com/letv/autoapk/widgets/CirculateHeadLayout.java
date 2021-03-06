package com.letv.autoapk.widgets;

import com.letv.autoapk.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CirculateHeadLayout extends FrameLayout {


	public CirculateHeadLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CirculateHeadLayout(Context context) {
		super(context);
	}

	public CirculateHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			int heightMode = MeasureSpec.getMode(heightMeasureSpec);
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = width * 10 / 18;
			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
			}
			setMeasuredDimension(width, height);
			measureChildren(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
		}

	}
}
