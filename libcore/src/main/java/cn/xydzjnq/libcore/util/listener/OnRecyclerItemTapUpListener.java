package cn.xydzjnq.libcore.util.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class OnRecyclerItemTapUpListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemTapUpListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new OnRecyclerItemTapUpListener.ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemTapUp(RecyclerView.ViewHolder viewHolder);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            final View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

            boolean hasChildClickable = hasChildClickable(child, e.getRawX(), e.getRawY());
            if (hasChildClickable) {
                return true;
            }
            if (child != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemTapUp(viewHolder);
            }
            return true;
        }

        public boolean hasChildClickable(View child, float x, float y) {
            if (child instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                    View child2 = ((ViewGroup) child).getChildAt(i);
                    if (!(child2 instanceof ViewGroup) && child2 != null && child2.isClickable()) {
                        final int[] locations = new int[2];
                        child2.getLocationOnScreen(locations);
                        if (locations[0] < x && x < locations[0] + child2.getWidth()
                                && locations[1] < y && y < locations[1] + child2.getHeight()) {
                            return true;
                        }
                    } else if (child2 instanceof ViewGroup) {
                        if (hasChildClickable(child2, x, y)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
