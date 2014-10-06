package de.woitek.libraries.styledradiogroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StyledRadioGroup extends RadioGroup {
	private String mDisplay[] = new String[]{"1", "2"};
	private String mValues[] = null;

    public StyledRadioGroup(Context context) {
        super(context);
        init(null, 0);
    }

	public StyledRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.StyledRadioGroup, defStyle, 0);

		if (a.hasValue(R.styleable.StyledRadioGroup_display)) {
			mDisplay = a.getString(R.styleable.StyledRadioGroup_display).split(";");
			if (mDisplay.length < 2) {
				mDisplay = new String[]{"1", "2"};
			}
		}

		if (a.hasValue(R.styleable.StyledRadioGroup_values)) {
			mValues = a.getString(R.styleable.StyledRadioGroup_values).split(";");
		}
		if ((mValues == null) || (mValues.length == 0)) {
			mValues = mDisplay;
		}

		boolean initialSelect = true;
		if (a.hasValue(R.styleable.StyledRadioGroup_selected)) {
			initialSelect = a.getBoolean(R.styleable.StyledRadioGroup_selected, true);
		}

        eDirection mDirection = eDirection.VERTICAL;
        if (a.hasValue(a.getInteger(R.styleable.StyledRadioGroup_layout, 0))) {
            int nDirection = a.getInteger(R.styleable.StyledRadioGroup_layout, 0);
            mDirection = (nDirection == 0)
                    ? eDirection.HORIZONTAL
					: eDirection.VERTICAL;
		}

		a.recycle();

		final int displayLength = mDisplay.length;
		final int valuesLength = mValues.length;
		final Context ctx = getContext();

		int orientation = HORIZONTAL;
		int bgFirstDrawable = R.drawable.button_background_left;
		int bgMiddleDrawable = R.drawable.button_background_horizontal;
		int bgLastDrawable = R.drawable.button_background_right;
		int width = WRAP_CONTENT;
		int height = MATCH_PARENT;

		if (mDirection == eDirection.VERTICAL) {
			orientation = VERTICAL;
			bgFirstDrawable = R.drawable.button_background_top;
			bgMiddleDrawable = R.drawable.button_background_vertical;
			bgLastDrawable = R.drawable.button_background_bottom;
			width = MATCH_PARENT;
			height = WRAP_CONTENT;
		}

		setOrientation(orientation);

		for (int i = 0; i < displayLength; ++i) {
			RadioButton rb = new RadioButton(ctx);
			rb.setId(i);
			rb.setText(mDisplay[i]);
			rb.setGravity(Gravity.CENTER);

			rb.setLayoutParams(new LinearLayout.LayoutParams(width, height, 1f));
			int bgDrwId = bgMiddleDrawable;
			if (i == 0) {
				bgDrwId = bgFirstDrawable;
			} else if (i == displayLength - 1) {
				bgDrwId = bgLastDrawable;
			}
			rb.setBackground(getResources().getDrawable(bgDrwId));
			rb.setTextColor(new ColorStateList(
					new int[][]{
							new int[]{android.R.attr.state_checked},
							new int[]{}
					},
					new int[]{
							Color.WHITE,
							Color.BLACK
					}
			));

			rb.setButtonDrawable(new StateListDrawable());
			rb.setTag((i < valuesLength) ? mValues[i] : mDisplay[i]);
			addView(rb);
		}

		setSelectedIndex((initialSelect) ? 0 : -1);
	}

	public String getSelectedItemValue() {
		RadioButton rb = (RadioButton) findViewById(getSelectedIndex());
		return (rb != null) ? (String) rb.getTag() : null;
	}

	public void setSelectedItemValue(String value) {
		RadioButton rb = (RadioButton) findViewById(getSelectedIndex());
		if (rb != null) {
			rb.setTag(value);
		}
	}

	public String getSelectedItemText() {
		RadioButton rb = (RadioButton) findViewById(getSelectedIndex());
		return (String) rb.getTag();
	}

	public int getSelectedIndex() {
		return getCheckedRadioButtonId();
	}

	public void setSelectedIndex(int index) {
		if (index < 0) {
			clearCheck();
		} else if (index < mDisplay.length) {
			check(index);
		}
	}

	public int getMaxIndex() {
		return mDisplay.length;
	}

	public void enableAll(boolean enable) {
		final int N = getChildCount();
		for (int i = 0; i < N; ++i) {
			getChildAt(i).setEnabled(enable);
		}
		setSelectedIndex((enable) ? 0 : -1);
	}

	public void enableIndex(int index, boolean enabled) {
		if (!enabled && (getSelectedIndex() == index)) {
			clearCheck();
		}
		if ((index >= 0) && (index < mDisplay.length)) {
			getChildAt(index).setEnabled(enabled);
		}
	}

	private enum eDirection {
		HORIZONTAL,
		VERTICAL
	}
}
