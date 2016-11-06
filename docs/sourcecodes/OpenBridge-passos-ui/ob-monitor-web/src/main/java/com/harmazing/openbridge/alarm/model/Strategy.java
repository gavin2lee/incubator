package com.harmazing.openbridge.alarm.model;

import com.harmazing.framework.common.model.BaseModel;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 9:53.
 */
public class Strategy extends BaseModel {
    private long id;
    private String metric;
    private String tags;
    private long maxStep;
    private int priority;
    private String func;
    private String op;
    private String rightValue;
    private String note;
    private String runBegin;
    private String runEnd;
    private long tplId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(long maxStep) {
        this.maxStep = maxStep;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRunBegin() {
        return runBegin;
    }

    public void setRunBegin(String runBegin) {
        this.runBegin = runBegin;
    }

    public String getRunEnd() {
        return runEnd;
    }

    public void setRunEnd(String runEnd) {
        this.runEnd = runEnd;
    }

    public long getTplId() {
        return tplId;
    }

    public void setTplId(long tplId) {
        this.tplId = tplId;
    }
}
