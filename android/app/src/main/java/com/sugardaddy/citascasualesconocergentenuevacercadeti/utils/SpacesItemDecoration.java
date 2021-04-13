package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int space1;

    public SpacesItemDecoration(int space, int space1) {
        this.space = space;
        this.space1 = space1;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space1 - space;
        } else {
            outRect.top = space;
        }

    }

}
