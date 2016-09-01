package com.lachesis.mnisqm.module.event.domain;

public class NursingInfo {
    private String deptName;

    private Integer nutritionSum;

    private Integer fallSum;

    private Integer pressureSoresSum;

    private String nutritionRate;

    private String fallRate;

    private String pressureSoresRate;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getNutritionSum() {
        return nutritionSum;
    }

    public void setNutritionSum(Integer nutritionSum) {
        this.nutritionSum = nutritionSum;
    }

    public Integer getFallSum() {
        return fallSum;
    }

    public void setFallSum(Integer fallSum) {
        this.fallSum = fallSum;
    }

    public Integer getPressureSoresSum() {
        return pressureSoresSum;
    }

    public void setPressureSoresSum(Integer pressureSoresSum) {
        this.pressureSoresSum = pressureSoresSum;
    }

    public String getNutritionRate() {
        return nutritionRate;
    }

    public void setNutritionRate(String nutritionRate) {
        this.nutritionRate = nutritionRate;
    }

    public String getFallRate() {
        return fallRate;
    }

    public void setFallRate(String fallRate) {
        this.fallRate = fallRate;
    }

    public String getPressureSoresRate() {
        return pressureSoresRate;
    }

    public void setPressureSoresRate(String pressureSoresRate) {
        this.pressureSoresRate = pressureSoresRate;
    }
}