package com.example.plannus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.plannus.utils.MetricsConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Resources.class)
@PowerMockIgnore({"android.content.res.Resources"})
@SuppressStaticInitializationFor({"Resources"})
public class MetricsConverterUtilTest {

    @Test
    public void testForDaysandHours() {
        PowerMockito.mockStatic(Resources.class, RETURNS_MOCKS);
        Resources res = mock(Resources.class);
        Mockito.when(Resources.getSystem()).thenReturn(res);
        DisplayMetrics metrics = mock(DisplayMetrics.class);
        metrics.densityDpi = 440;
        Mockito.when(res.getDisplayMetrics()).thenReturn(metrics);

        float dp = 20f;
        int pixel = MetricsConverter.convertDpToPixel(dp);
        assertEquals(55, pixel);
    }

}

