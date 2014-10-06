package de.woitek.libraries.flexiblefonttextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FlexibleFontTextView extends TextView {
    // Attributes
    private String mFontFaceName = "cowboy_movie.ttf";

    public FlexibleFontTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public FlexibleFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FlexibleFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        text = text.toString().toUpperCase();
        super.setText(text, type);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setText(getText().toString().toUpperCase());

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FlexibleFontTextView, defStyle, 0);

        if (a.hasValue(R.styleable.FlexibleFontTextView_fontFaceName)) {
            mFontFaceName = a.getString(R.styleable.FlexibleFontTextView_fontFaceName);
        }

        a.recycle();

        if (!isInEditMode()) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mFontFaceName);
            setTypeface(myTypeface);
        }
    }
}
