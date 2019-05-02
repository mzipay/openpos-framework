package org.jumpmind.pos.util;

public class Version {

    private String componentName;
    private String buildNumber;
    private String buildName;
    private String gitHash;
    private String gitBranch;
    private String version;
    private String buildTime;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getGitHash() {
        return gitHash;
    }

    public void setGitHash(String gitHash) {
        this.gitHash = gitHash;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public void setGitBranch(String gitBranch) {
        this.gitBranch = gitBranch;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    @Override
    public String toString() {
        return "Version [componentName=" + componentName + ", version=" + version + ", buildNumber=" + buildNumber + ", buildName="
                + buildName + ", buildTime=" + buildTime + ", gitHash=" + gitHash + ", gitBranch=" + gitBranch + "]";
    }
    
    

}
