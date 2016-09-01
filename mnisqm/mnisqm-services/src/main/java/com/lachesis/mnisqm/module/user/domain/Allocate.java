package com.lachesis.mnisqm.module.user.domain;

public class Allocate {
    private String allocateTime;

    private String allocateDept;

    private String theWorkTime;

    private String id;

    private String name;

    private String allocateName;

    public String getAllocateTime() {
        return allocateTime;
    }

    public void setAllocateTime(String allocateTime) {
        this.allocateTime = allocateTime;
    }

    public String getAllocateDept() {
        return allocateDept;
    }

    public void setAllocateDept(String allocateDept) {
        this.allocateDept = allocateDept;
    }

    public String getTheWorkTime() {
        return theWorkTime;
    }

    public void setTheWorkTime(String theWorkTime) {
        this.theWorkTime = theWorkTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllocateName() {
        return allocateName;
    }

    public void setAllocateName(String allocateName) {
        this.allocateName = allocateName;
    }
}