/*
 * Copyright (C) 2014 Roger Diller <roger@flexrentalsolutions.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package com.frs.jasperreports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;

/**
 *
 * @author Roger Diller <roger@flexrentalsolutions.com>
 */
public class JasperReportsScalerUtils {

    public static JasperDesign scaleJasperDesign(JasperDesign jasperDesign, PaperSizeEnum paperSize, PageOrientationEnum orientation){

        return scaleJasperDesign(jasperDesign, 
			paperSize == null ? null : paperSize.getWidth(), paperSize == null ? null : paperSize.getHeight(), orientation);
    }
 
    public static JasperDesign scaleJasperDesign(JasperDesign jasperDesign, Integer width, Integer height, PageOrientationEnum orientation){
		
        if(width == null || height == null){
            //do nothing so that report generates based on JRXML file
            return jasperDesign;
        }

        if(orientation == null){
            orientation = PageOrientationEnum.PORTRAIT;
            
            if(jasperDesign.getOrientationValue() != null){
                switch(jasperDesign.getOrientationValue()){
                    case PORTRAIT:
                        orientation = PageOrientationEnum.PORTRAIT;
                        break;
                    case LANDSCAPE:
                        orientation = PageOrientationEnum.LANDSCAPE;
                        break;
                }
            }	
        }

        int margins = jasperDesign.getLeftMargin() + jasperDesign.getRightMargin();

        Integer origWidth = jasperDesign.getPageWidth() - margins;

        switch(orientation){
            case PORTRAIT:
                jasperDesign.setOrientation(OrientationEnum.PORTRAIT);
                jasperDesign.setPageWidth(width);
                jasperDesign.setPageHeight(height);
                break;
            case LANDSCAPE:
                jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
                jasperDesign.setPageWidth(height);
                jasperDesign.setPageHeight(width);
                break;
        }

        Integer afterWidth = jasperDesign.getPageWidth() - margins;

        //everything scales according to ratio
        double ratio = afterWidth.doubleValue() / origWidth.doubleValue();			

        Double columnWidth = Double.valueOf(jasperDesign.getColumnWidth()) * ratio;
        Double columnSpacing = Double.valueOf(jasperDesign.getColumnSpacing()) * ratio;

        jasperDesign.setColumnWidth(new BigDecimal(columnWidth).setScale(0, RoundingMode.DOWN).intValue());
        jasperDesign.setColumnSpacing(new BigDecimal(columnSpacing).setScale(0, RoundingMode.DOWN).intValue());			

        for(JRBand band : jasperDesign.getAllBands()){
            for(JRElement element : band.getElements()){		

                    scaleElement(element, ratio);	
            }
        }
        
        return jasperDesign;
    }

    private static void scaleElement(JRElement element, double ratio){

        //100
        Integer origElementWidth = element.getWidth();

        //100 + 100 = 200
        Integer totalX = element.getX() + origElementWidth;

        double newWidthDbl = element.getWidth() * ratio;

        //90
        int newWidth = new BigDecimal(newWidthDbl).setScale(0, RoundingMode.DOWN).intValue();

        //10
        int diff = origElementWidth - newWidth;

        //180
        Integer totalXScaled = new BigDecimal(totalX.doubleValue() * ratio).setScale(0, RoundingMode.DOWN).intValue();

        //200 - (180 + 10) = 10
        Integer moveX = ((totalX - (totalXScaled + diff)) * -1);

        if(element instanceof JRCrosstab && ratio < 1.0){
            //do NOT scale crosstabs down, can create "Not enough space to render the crosstab." error 
        }else{
            element.setWidth(newWidth);		
            element.setX(element.getX() + moveX);
        }

        JRElement [] childElements = null;
        
        double elementRatio = newWidth / origElementWidth.doubleValue();

        if(element instanceof JRElementGroup){
            childElements = ((JRElementGroup)element).getElements();
        }else if(element instanceof JRComponentElement){

            Component comp = ((JRComponentElement)element).getComponent();

            if(comp instanceof ListComponent){

                DesignListContents listContents = (DesignListContents) ((ListComponent)comp).getContents();
                listContents.setWidth(element.getWidth());

                childElements = ((ListComponent)comp).getContents().getElements();
            }
        }

        if(childElements != null){            
            for(JRElement childElement : childElements){
                scaleElement(childElement, elementRatio);
            }
        }
    }
    
}
