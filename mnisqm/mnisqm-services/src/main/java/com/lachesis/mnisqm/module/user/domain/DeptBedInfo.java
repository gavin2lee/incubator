package com.lachesis.mnisqm.module.user.domain;

public class DeptBedInfo {
    private String leaderNurse;

    private String dutyNurse;

    private String manageBeds;

    private String criticalBeds;

    private String surgeryBeds;

    public String getLeaderNurse() {
        return leaderNurse;
    }

    public void setLeaderNurse(String leaderNurse) {
        this.leaderNurse = leaderNurse;
    }

    public String getDutyNurse() {
        return dutyNurse;
    }

    public void setDutyNurse(String dutyNurse) {
        this.dutyNurse = dutyNurse;
    }

    public String getManageBeds() {
        return manageBeds;
    }

    public void setManageBeds(String manageBeds) {
        this.manageBeds = manageBeds;
    }

    public String getCriticalBeds() {
        return criticalBeds;
    }

    public void setCriticalBeds(String criticalBeds) {
        this.criticalBeds = criticalBeds;
    }

    public String getSurgeryBeds() {
        return surgeryBeds;
    }

    public void setSurgeryBeds(String surgeryBeds) {
        this.surgeryBeds = surgeryBeds;
    }
}