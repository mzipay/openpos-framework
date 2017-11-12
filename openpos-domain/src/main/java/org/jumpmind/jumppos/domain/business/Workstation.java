package org.jumpmind.jumppos.domain.business;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.security.Operator;

@Entity
public class Workstation extends BaseEntity {

    @Id
    @Embedded
    private WorkstationId id;

    @OneToOne
    private Operator lastOperator;

    private WorkstationStatus status = WorkstationStatus.CLOSED;
    private int lastTransactionSeqId;
    private String lastOpenBusinessDate;
    private String typeCode;
    private boolean trainingModeFlag;
    private String applicationVersion;
    private String lastSqlScriptApplied;
    private String ipAddress;
    private String macAddress;
    private String hostName;
    private String databaseName;
    private String databaseVersion;
    private String osName;
    private String osVersion;
    private String osUsername;
    
    public Workstation() {     
    }
    
    public Workstation(String workstationId, BusinessUnit store) {     
         this.id = new WorkstationId(workstationId, store);         
    }    

    public void setId(WorkstationId id) {
        this.id = id;
    }

    public WorkstationId getId() {
        return id;
    }

    public WorkstationStatus getStatus() {
        return status;
    }

    public void setStatus(WorkstationStatus status) {
        this.status = status;
    }

    public String getLastOpenBusinessDate() {
        return lastOpenBusinessDate;
    }

    public void setLastOpenBusinessDate(String lastOpenBusinessDate) {
        this.lastOpenBusinessDate = lastOpenBusinessDate;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public boolean isTrainingModeFlag() {
        return trainingModeFlag;
    }

    public void setTrainingModeFlag(boolean trainingModeFlag) {
        this.trainingModeFlag = trainingModeFlag;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsUsername() {
        return osUsername;
    }

    public void setOsUsername(String osUsername) {
        this.osUsername = osUsername;
    }

    public void setLastSqlScriptApplied(String lastSqlScriptApplied) {
        this.lastSqlScriptApplied = lastSqlScriptApplied;
    }

    public String getLastSqlScriptApplied() {
        return lastSqlScriptApplied;
    }

    public void setLastTransactionSeqId(int lastTransactionSeqId) {
        this.lastTransactionSeqId = lastTransactionSeqId;
    }

    public int getLastTransactionSeqId() {
        return lastTransactionSeqId;
    }

    public void setLastOperator(Operator lastOperator) {
        this.lastOperator = lastOperator;
    }

    public Operator getLastOperator() {
        return lastOperator;
    }

}
