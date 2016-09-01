package com.lachesis.mnisqm.module.event.domain;

public class EventAnalysis {
    private String adverseEvent;

    private Integer nearlyDay;

    private Integer nearlyMonth;

    private Integer year;

    private Integer yesterYearDay;

    private Integer yesterYearMonth;

    private Integer yesterYear;

    public String getAdverseEvent() {
        return adverseEvent;
    }

    public void setAdverseEvent(String adverseEvent) {
        this.adverseEvent = adverseEvent;
    }

    public Integer getNearlyDay() {
        return nearlyDay;
    }

    public void setNearlyDay(Integer nearlyDay) {
        this.nearlyDay = nearlyDay;
    }

    public Integer getNearlyMonth() {
        return nearlyMonth;
    }

    public void setNearlyMonth(Integer nearlyMonth) {
        this.nearlyMonth = nearlyMonth;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYesterYearDay() {
        return yesterYearDay;
    }

    public void setYesterYearDay(Integer yesterYearDay) {
        this.yesterYearDay = yesterYearDay;
    }

    public Integer getYesterYearMonth() {
        return yesterYearMonth;
    }

    public void setYesterYearMonth(Integer yesterYearMonth) {
        this.yesterYearMonth = yesterYearMonth;
    }

    public Integer getYesterYear() {
        return yesterYear;
    }

    public void setYesterYear(Integer yesterYear) {
        this.yesterYear = yesterYear;
    }
}