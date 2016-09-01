package com.anyi.report.entity;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:文书打印使用的打印纸类。
 *             1、以A4纸竖向放置的形式进行描述，因此有默认约束：高度>宽度
 *             2、单位：像素
 * <p/>
 * Created by junming.ren
 * on 2016/6/29.
 */

public class DocPrintPaper {
    private final int width;//打印纸宽度
    private final int hight;//打印纸高度
    private final int widthMargin;//宽边的页边距
    private final int hightMargin;//高边的页边距
    private final PrintPaperType paperType;//纸张类型，A4，A6等

    public enum PrintPaperType{
        A3("A3"),
        A4("A4"),
        B5("B5"),
        A6("A6");
        private String name;

        private PrintPaperType(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static PrintPaperType toEnumType(String name){
            try{
                return valueOf(name);
            }catch (Exception e){
                return A4;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHight() {
        return hight;
    }

    public int getWidthMargin() {
        return widthMargin;
    }

    public int getHightMargin() {
        return hightMargin;
    }

    public PrintPaperType getPaperType() {
        return paperType;
    }

    public static DocPrintPaper createPrintPaper(PrintPaperType paperType, int resolution){
        if(null == paperType || 0>= resolution){
            return null;
        }
        DocPrintPaper paper = new DocPrintPaper(paperType, resolution);
        //高度>宽度约束
        if(null==paper || paper.getWidth()>paper.getHight()){
            return null;
        }
        return paper;
    }

    //私有构造函数
    private DocPrintPaper(PrintPaperType paperType, int resolution){
        this.widthMargin = 10;//留给打印页码的边距为20
        this.hightMargin = 0;
        this.paperType = paperType;
        switch (paperType){
            case A3:
                //TODO
            case B5:
                //TODO
            case A6:
                //TODO
            case A4:
            default:
                switch (resolution){
                    case 120:
                        this.hight = 1754;
                        this.width = 1240;
                        break;
                    case 150:
                        this.hight = 2105;
                        this.width = 1487;
                        break;
                    case 300:
                        this.hight = 3508;
                        this.width = 2479;
                        break;
                    case 72:
                    default:
                        this.hight = 970;
                        this.width = 670;
                }
        }

    }

    public int getHight4LandPrint(){
        return (width-2*widthMargin)>0? (width-2*widthMargin):0;
    }

    public int getHight4PortPrint(){
        return (hight-2*hightMargin)>0? (hight-2*hightMargin):0;
    }
}
