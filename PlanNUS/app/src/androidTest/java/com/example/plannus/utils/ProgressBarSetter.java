package com.example.plannus.utils;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

import android.view.View;
import android.widget.SeekBar;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.util.HumanReadables;

public class ProgressBarSetter {
    public static ViewAction scrubSeekBarAction(int progress) {
        return actionWithAssertions(new GeneralSwipeAction(
                Swipe.SLOW,
                new SeekBarThumbCoordinatesProvider(0),
                new SeekBarThumbCoordinatesProvider(progress),
                Press.PINPOINT));
    }

    private static class SeekBarThumbCoordinatesProvider implements CoordinatesProvider {
        int mProgress;

        public SeekBarThumbCoordinatesProvider(int progress) {
            mProgress = progress;
        }

        private static float[] getVisibleLeftTop(View view) {
            final int[] xy = new int[2];
            view.getLocationOnScreen(xy);
            return new float[]{ (float) xy[0], (float) xy[1] };
        }

        @Override
        public float[] calculateCoordinates(View view) {
            if (!(view instanceof SeekBar)) {
                throw new PerformException.Builder()
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException(String.format("SeekBar expected"))).build();
            }
            SeekBar seekBar = (SeekBar) view;
            int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
            double progress = mProgress == 0 ? seekBar.getProgress() : mProgress;
            int xPosition = (int) (seekBar.getPaddingLeft() + width * progress / seekBar.getMax());
            float[] xy = getVisibleLeftTop(seekBar);
            return new float[]{ xy[0] + xPosition, xy[1] + 10 };
        }
    }
}
