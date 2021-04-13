package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.interfaces.datasets.IScatterDataSet;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.utils.Utils;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.charting.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 * Created at Time 09:08
 */
public class XShapeRenderer implements IShapeRenderer
{


    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        final float shapeHalf = dataSet.getScatterShapeSize() / 2f;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        c.drawLine(
                posX - shapeHalf,
                posY - shapeHalf,
                posX + shapeHalf,
                posY + shapeHalf,
                renderPaint);
        c.drawLine(
                posX + shapeHalf,
                posY - shapeHalf,
                posX - shapeHalf,
                posY + shapeHalf,
                renderPaint);

    }

}