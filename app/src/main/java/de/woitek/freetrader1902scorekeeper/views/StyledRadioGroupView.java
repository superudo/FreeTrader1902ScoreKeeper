package de.woitek.freetrader1902scorekeeper.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import de.woitek.freetrader1902scorekeeper.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StyledRadioGroupView extends RadioGroup {
	private String mDisplay[] = new String[]{"1", "2"};
	private String mValues[] = null;

	public StyledRadioGroupView(Context context) {
		super(context);
		init(null, 0);
	}

	public StyledRadioGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.StyledRadioGroupView, defStyle, 0);

		if (a.hasValue(R.styleable.StyledRadioGroupView_display)) {
			mDisplay = a.getString(R.styleable.StyledRadioGroupView_display).split(";");
			if (mDisplay.length < 2) {
				mDisplay = new String[]{"1", "2"};
			}
		}

		if (a.hasValue(R.styleable.StyledRadioGroupView_values)) {
			mValues = a.getString(R.styleable.StyledRadioGroupView_values).split(";");
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
			int bgDrwId = R.drawable.radio_button_background;
			if (i == 0) {
				bgDrwId = R.drawable.radio_button_background_left;
			} else if (i == displayLength - 1) {
				bgDrwId = R.drawable.radio_button_background_right;
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
