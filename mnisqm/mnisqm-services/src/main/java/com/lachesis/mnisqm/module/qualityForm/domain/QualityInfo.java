package com.lachesis.mnisqm.module.qualityForm.domain;

public class QualityInfo {
    private String completionRate;

    private String agreementRate;

    private String week;

    private String month;

    private String year;

    private String indicators;

    private String issue;

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public String getAgreementRate() {
        return agreementRate;
    }

    public void setAgreementRate(String agreementRate) {
        this.agreementRate = agreementRate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
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

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}