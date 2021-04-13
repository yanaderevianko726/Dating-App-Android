package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.dataprovider;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
