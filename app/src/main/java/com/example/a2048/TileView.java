package com.example.a2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class TileView extends AppCompatTextView {
    private static final int[] TILE_COLORS = {
            R.color.tile_0, R.color.tile_2, R.color.tile_4, R.color.tile_8,
            R.color.tile_16, R.color.tile_32, R.color.tile_64, R.color.tile_128,
            R.color.tile_256, R.color.tile_512, R.color.tile_1024, R.color.tile_2048
    };

    private static final int[] TEXT_COLORS = {
            R.color.text_0, R.color.text_2, R.color.text_4, R.color.text_8,
            R.color.text_16, R.color.text_32, R.color.text_64, R.color.text_128,
            R.color.text_256, R.color.text_512, R.color.text_1024, R.color.text_2048
    };

    public TileView(Context context) {
        super(context);
        init();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextSize(24);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setGravity(Gravity.CENTER);

        try {
            setBackgroundResource(R.drawable.tile_background);
        } catch (Resources.NotFoundException e) {
            // Fallback if drawable is missing
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tile_default));
        }
    }

    public void setValue(int value) {
        if (value == 0) {
            setText("");
        } else {
            setText(String.valueOf(value));
        }

        int colorIndex = 0;
        if (value <= 2048 && value > 0) {
            colorIndex = (int) (Math.log(value) / Math.log(2));
            colorIndex = Math.max(0, Math.min(colorIndex, TILE_COLORS.length - 1));
        } else if (value > 2048) {
            colorIndex = TILE_COLORS.length - 1;
        }

        try {
            setBackgroundColor(ContextCompat.getColor(getContext(), TILE_COLORS[colorIndex]));
            setTextColor(ContextCompat.getColor(getContext(), TEXT_COLORS[colorIndex]));
        } catch (Resources.NotFoundException e) {
            // Fallback colors if specific ones are missing
            setBackgroundColor(Color.LTGRAY);
            setTextColor(Color.BLACK);
        }

        // Adjust text size based on value length
        if (String.valueOf(value).length() <= 2) {
            setTextSize(24);
        } else if (String.valueOf(value).length() == 3) {
            setTextSize(20);
        } else {
            setTextSize(16);
        }
    }
}