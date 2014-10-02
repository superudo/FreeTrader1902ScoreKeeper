package de.woitek.libraries.styledradiogroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

		a.recycle();

		setOrientation(HORIZONTAL);

		final int displayLength = mDisplay.length;
		final int valuesLength = mValues.length;
		final Context ctx = getContext();

		for (int i = 0; i < displayLength; ++i) {
			RadioButton rb = new RadioButton(ctx);
			rb.setId(i);
			rb.setText(mDisplay[i]);
			rb.setGravity(TEXT_ALIGNMENT_CENTER);
			rb.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1f));
			int bgDrwId = R.drawable.button_background;
			if (i == 0) {
				bgDrwId = R.drawable.button_background_left;
			} else if (i == displayLength - 1) {
				bgDrwId = R.drawable.button_background_right;
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

		setSelectedIndex(0);
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
			((RadioButton) getChildAt(i)).setEnabled(enable);
		}
		setSelectedIndex((enable) ? 0 : -1);
	}

	public void enableIndex(int index, boolean enabled) {
		if (!enabled && (getSelectedIndex() == index)) {
			clearCheck();
		}
		if ((index >= 0) && (index < mDisplay.length)) {
			((RadioButton) getChildAt(index)).setEnabled(enabled);
		}
	}
}
