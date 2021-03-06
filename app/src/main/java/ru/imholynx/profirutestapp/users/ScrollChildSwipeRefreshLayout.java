package ru.imholynx.profirutestapp.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout{

    private View scrollUpChild;

    public ScrollChildSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if(scrollUpChild != null)
            return ViewCompat.canScrollVertically(scrollUpChild, -1);
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View scrollUpChild) {
        this.scrollUpChild = scrollUpChild;
    }
}
