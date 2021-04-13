package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.dataprovider;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.components.YAxis;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
