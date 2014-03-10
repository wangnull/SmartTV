package com.cstzju.poker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class PokerView extends View {
	Bitmap poker_pic = null;
	int[] poker_id = null;
	int poker_count = 0;

	float scale = 1.0f;// 缩放倍数
	Rect des = new Rect();
	int poker_height = 0;
	int poker_width = 0;

	public PokerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public PokerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public PokerView(Context context) {
		super(context);
		initGameView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}
		return result;
	}

	public void setPokerAndPokerCount(int[] id) {
		this.poker_count = id.length;
		poker_id = new int[poker_count];
		for (int i = 0; i < poker_count; i++) {
			poker_id[i] = id[i];
		}

	}

	public void setPokerBackCount(int count) {
		this.poker_count = count;
		poker_id = new int[poker_count];
		for (int i = 0; i < poker_count; i++) {
			poker_id[i] = R.drawable.back;
		}

	}

	protected void initGameView() {
		Bitmap pokerexample = BitmapFactory.decodeResource(getResources(),
				R.drawable.clu_2);
		poker_width = pokerexample.getWidth();
		poker_height = pokerexample.getHeight();
		pokerexample.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (poker_count <= 15 && poker_count > 10) {
			scale = 0.7f;
		} else if (poker_count <= 20 && poker_count > 15) {
			scale = 0.53f;
		} else if (poker_count > 20) {
			scale = 0.5f;
		}
		for (int i = 0; i < poker_count; i++) {
			des.set((int) (i * 35 * scale), 0,
					(int) ((poker_width + i * 35) * scale),
					(int) (poker_height * scale));
			poker_pic = BitmapFactory.decodeResource(getResources(),
					poker_id[i]);
			canvas.drawBitmap(poker_pic, null, des, null);
		}

	}
}
