package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.dataprovider;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();
}
