package com.lachesis.mnisqm.module.event.domain;

public class RiskManage {
    private String adverseEvent;

    private String weeks;

    private String month;

    private String year;

    private String pressureSores;

    private String fallDown;

    private String pipeSlip;

    public String getAdverseEvent() {
        return adverseEvent;
    }

    public void setAdverseEvent(String adverseEvent) {
        this.adverseEvent = adverseEvent;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPressureSores() {
        return pressureSores;
    }

    public void setPressureSores(String pressureSores) {
        this.pressureSores = pressureSores;
    }

    public String getFallDown() {
        return fallDown;
    }

    public void setFallDown(String fallDown) {
        this.fallDown = fallDown;
    }

    public String getPipeSlip() {
        return pipeSlip;
    }

    public void setPipeSlip(String pipeSlip) {
        this.pipeSlip = pipeSlip;
    }
}