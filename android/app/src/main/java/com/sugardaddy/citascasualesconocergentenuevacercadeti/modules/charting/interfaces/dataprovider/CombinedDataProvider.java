package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.dataprovider;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.CombinedData;

/**
 * Created by philipp on 11/06/16.
 */
public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
