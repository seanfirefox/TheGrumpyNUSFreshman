package com.example.plannus.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


public class RecyclerViewChecker {
    public static Matcher<View> hasItem(Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    int type = adapter.getItemViewType(position);
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
