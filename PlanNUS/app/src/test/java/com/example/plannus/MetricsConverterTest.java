package com.example.plannus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.plannus.utils.MetricsConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClassToMock.class)
public class MetricsConverterTest {

    @Test
    public void testForDaysandHours() {
        PowerMockito.mockStatic(Resources.class,RETURNS_MOCKS);
        Resources res = mock(Resources.getSystem().getClass());
        DisplayMetrics metrics = mock(DisplayMetrics.class);
        metrics.widthPixels = 300;
        PowerMockito.when(res.getDisplayMetrics()).thenReturn(metrics);
        float dp = 20f;
        int pixel = MetricsConverter.convertDpToPixel(dp);
        assertEquals(1, pixel);
    }
}
