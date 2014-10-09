package de.woitek.freetrader1902scorekeeper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.woitek.freetrader1902scorekeeper.R;

public class BoxUIView extends LinearLayout {
	private int mBoxColor = Color.parseColor("#ffa400");

	private int mOwned = 0;
	private int mFilled = 0;
	private boolean mHasEmptyBoxes = false;
	private boolean mBrightBoxes = false;
	private int mNumberOfBoxes = 5;

	private int[] mBoxViewId;

	public BoxUIView(Context context) {
		super(context);
		initView(context, null, 0);
	}

	public BoxUIView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0);
	}

	public BoxUIView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs, defStyle);
	}

	private void initView(Context context, AttributeSet attrs, int defStyle) {
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.BoxUIView, defStyle, 0);

		mBoxColor = a.getColor(R.styleable.BoxUIView_boxColor, mBoxColor);
		mHasEmptyBoxes = a.getBoolean(R.styleable.BoxUIView_triStateBoxes, mHasEmptyBoxes);
		mBrightBoxes = a.getBoolean(R.styleable.BoxUIView_brightBoxes, mBrightBoxes);
		mNumberOfBoxes = a.getInt(R.styleable.BoxUIView_numberOfBoxes, mNumberOfBoxes);
		mNumberOfBoxes = Math.min(Math.max(mNumberOfBoxes, 1), 5);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_box_ui, this);

		mBoxViewId = new int[]{R.id.box1, R.id.box2, R.id.box3, R.id.box4, R.id.box5};
		for (int i = 0; i < 5; ++i) {
			findViewById(mBoxViewId[i]).setVisibility((i < mNumberOfBoxes) ? VISIBLE : INVISIBLE);
		}
	}

	public void setOwned(int mOwned) {
		this.mOwned = mOwned;
		DrawBoxes();
	}

	private void DrawBoxes() {
		final int LEN = mBoxViewId.length;
        int[] level = new int[LEN];

        if (this.mHasEmptyBoxes) {
            if (mFilled > mOwned) {
                for (int i = 0; i < mOwned; i++) {
                    level[i] = 2;
                }
                for (int i = mOwned; i < mFilled; i++) {
                    level[i] = 3;
                }
                for (int i = mFilled; i < LEN; i++) {
                    level[i] = 0;
                }
            } else {
                for (int i = 0; i < mFilled; i++) {
                    level[i] = 2;
                }
                for (int i = mFilled; i < mOwned; i++) {
                    level[i] = 1;
                }
                for (int i = mOwned; i < LEN; i++) {
                    level[i] = 0;
                }
            }
        } else {
            for (int i = 0; i < LEN; i += 1) {
                if (i < this.mOwned) {
                    level[i] = (mBrightBoxes) ? 2 : 1;
                } else {
                    level[i] = 0;
                }
            }
        }

        for (int i = 0; i < LEN; i += 1) {
            Drawable drw = ((ImageView) findViewById(mBoxViewId[i])).getDrawable();
            drw.mutate();
            drw.setLevel(level[i]);
            drw.setColorFilter(mBoxColor, PorterDuff.Mode.MULTIPLY);
            drw.invalidateSelf();
		}
	}

	public void setFilled(int mFilled) {
		this.mFilled = mFilled;
		DrawBoxes();
	}
}
