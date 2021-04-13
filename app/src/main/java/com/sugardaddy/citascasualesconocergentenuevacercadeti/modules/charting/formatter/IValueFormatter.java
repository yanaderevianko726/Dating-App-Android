package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.formatter;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.data.Entry;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.utils.ViewPortHandler;

/**
 * Interface to format all values before they are drawn as labels.
 *
 * @author Philipp Jahoda
 * @deprecated Extend {@link ValueFormatter} instead
 */
@Deprecated
public interface IValueFormatter
{

    /**
     * Called when a value (from labels inside the chart) is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value           the value to be formatted
     * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
     * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return the formatted label ready for being drawn
     *
     * @deprecated Extend {@link ValueFormatter} and override an appropriate method
     */
    @Deprecated
    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}
