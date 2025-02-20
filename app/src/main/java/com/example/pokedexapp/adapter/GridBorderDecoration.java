package com.example.pokedexapp.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

public class GridBorderDecoration extends RecyclerView.ItemDecoration {

    private final int borderWidth;
    private final Paint paint;

    public GridBorderDecoration(Context context, int borderWidthDp, @ColorInt int borderColor) {
        this.borderWidth = (int) (context.getResources().getDisplayMetrics().density * borderWidthDp);
        this.paint = new Paint();
        this.paint.setColor(borderColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.borderWidth);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            // Get each child’s bounding rectangle
            float left = child.getLeft();
            float top = child.getTop();
            float right = child.getRight();
            float bottom = child.getBottom();

            // Draw a rectangle around the child
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // If you want spacing between cells so borders don’t overlap,
        // adjust the item offsets. E.g., half border around each side.
        outRect.set(borderWidth / 2, borderWidth / 2, borderWidth / 2, borderWidth / 2);
    }
}

