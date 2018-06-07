package org.jumpmind.pos.app.state.user;

public class ManagerOverride {

    private String managerUsername;

    private String overridePermissionId;

    public ManagerOverride() {
    }

    public ManagerOverride(String managerUsername, String overridePermissionId) {
        this.managerUsername = managerUsername;
        this.overridePermissionId = overridePermissionId;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getOverridePermissionId() {
        return overridePermissionId;
    }

    public void setOverridePermissionId(String overridePermissionId) {
        this.overridePermissionId = overridePermissionId;
    }

}
