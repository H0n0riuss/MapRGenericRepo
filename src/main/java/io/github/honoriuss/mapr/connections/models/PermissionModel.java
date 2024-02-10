package io.github.honoriuss.mapr.connections.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author H0n0riuss
 */
public class PermissionModel {
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("group")
    private String group;
    @JsonProperty("other")
    private String other;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
