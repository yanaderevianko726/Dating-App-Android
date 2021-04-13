package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.dataprovider;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.components.YAxis.AxisDependency;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.BarLineScatterCandleBubbleData;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
