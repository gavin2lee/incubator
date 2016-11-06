package com.harmazing.openbridge.alarm.model;

import com.harmazing.framework.common.model.BaseModel;


/**
 * Created by liyang on 2016/7/28.
 */
public class Host extends BaseModel {
    private int id;
    private String hostname;
    private String ip;
    private String agentVersion;
    private String pluginVersion;
    private long maintainBegin;
    private long maintainEnd;
    private String updateAt;

    public Host() {}

    public Host(String hostname, String ip, String agentVersion, String pluginVersion, long maintainBegin, long maintainEnd) {
        this.hostname = hostname;
        this.ip = ip;
        this.agentVersion = agentVersion;
        this.pluginVersion = pluginVersion;
        this.maintainBegin = maintainBegin;
        this.maintainEnd = maintainEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public long getMaintainBegin() {
        return maintainBegin;
    }

    public void setMaintainBegin(long maintainBegin) {
        this.maintainBegin = maintainBegin;
    }

    public long getMaintainEnd() {
        return maintainEnd;
    }

    public void setMaintainEnd(long maintainEnd) {
        this.maintainEnd = maintainEnd;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
