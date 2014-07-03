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

/**
 *
 * @author Roger Diller <roger@flexrentalsolutions.com>
 */
public enum PaperSizeEnum {
    
    US_LETTER("us-letter","US Letter", 612, 792),
    US_LEGAL("us-legal","US Legal", 612, 1008),
    A4("A4","A4", 595, 842);

    private String code;
    private String desc;
    private int width;
    private int height;

    private PaperSizeEnum(String code, String desc, int width, int height) {
            this.code = code;
            this.desc = desc;
            this.width = width;
            this.height = height;
    }

    public static PaperSizeEnum getInstance(String code) {
        for (PaperSizeEnum size : PaperSizeEnum.values()) {
                if (size.getCode().equals(code)) {
                    return size;
                }
        }
        return null;
    }

    public static PaperSizeEnum getInstance(int width, int height){

        for(PaperSizeEnum size : PaperSizeEnum.values()){
                if(size.getWidth() == width && size.getHeight() == height){
                        return size;
                }
        }
        return null;
    }

    public String getCode() {
            return code;
    }

    public String getDescription(){
            return desc;
    }

    public void setCode(String code) {
            this.code = code;
    }

    public String toString() {
            return code;
    }

    public int getWidth() {
            return width;
    }

    public int getHeight() {
            return height;
    }
}
