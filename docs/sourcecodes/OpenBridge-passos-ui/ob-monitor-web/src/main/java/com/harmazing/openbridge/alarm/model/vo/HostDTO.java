package com.harmazing.openbridge.alarm.model.vo;

import javax.validation.constraints.NotNull;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/29 9:18.
 */
public class HostDTO extends ParentDTO {
    @NotNull(message="GroupId do not exist!")
    private long groupId;
    @NotNull(message="hostname do not exist!")
    private String hostname;
    private String ip;
    private String agentVersion;
    private String pluginVersion;
    private long maintainBegin;
    private long maintainEnd;
    private long updateAt;

    public HostDTO() {
    }

    public HostDTO(long groupId, String hostname, String ip, String agentVersion, String pluginVersion,
                   long maintainBegin, long maintainEnd) {
        this.groupId = groupId;
        this.hostname = hostname;
        this.ip = ip;
        this.agentVersion = agentVersion;
        this.pluginVersion = pluginVersion;
        this.maintainBegin = maintainBegin;
        this.maintainEnd = maintainEnd;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
