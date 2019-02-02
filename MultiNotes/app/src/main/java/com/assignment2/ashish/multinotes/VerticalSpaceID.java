package com.assignment2.ashish.multinotes;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceID extends RecyclerView.ItemDecoration {

    private final int bottom = 10;
    private final int left = 10;
    private final int right = 10;
    private final int top = 20;

    public VerticalSpaceID() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = bottom;
        outRect.top = top;
        outRect.left = left;
        outRect.right = right;
    }
}
