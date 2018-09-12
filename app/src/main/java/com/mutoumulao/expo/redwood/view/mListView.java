package com.mutoumulao.expo.redwood.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class mListView extends ListView {

	public mListView(Context context) {
		super(context);
	}

	public mListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public mListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
