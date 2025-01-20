package com.lenardam.mydiet.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_THRESHOLD = 100;  // Minimalna odległość przesunięcia
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;  // Minimalna prędkość przesunięcia
    private Context context;
    private SwipeCallback callback;

    public SwipeGestureListener(Context context, SwipeCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {  // Sprawdzamy, czy gest był poziomy
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    // Przesunięcie w prawo
                    callback.onSwipeRight();
                } else {
                    // Przesunięcie w lewo
                    callback.onSwipeLeft();
                }
                return true;
            }
        }
        return false;
    }

    public interface SwipeCallback {
        void onSwipeLeft();
        void onSwipeRight();
    }
}