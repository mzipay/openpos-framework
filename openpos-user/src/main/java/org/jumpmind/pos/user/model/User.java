package org.jumpmind.pos.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    
    private String id;
    private String username;
    private String lastName;
    private String firstName;
    private List<PasswordHistory> passwordHistory = new ArrayList<>();
    private Date lastLogin = new Date();
    private boolean lockedOutFlag;
    private boolean passwordExpiredFlag = false;
    private int passwordFailedAttempts= 0;
    private Date lastPasswordAttempt = new Date();
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public List<PasswordHistory> getPasswordHistory() {
        return passwordHistory;
    }
    public void setPasswordHistory(List<PasswordHistory> passwordHistory) {
        this.passwordHistory = passwordHistory;
    }
    public Date getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    public boolean isLockedOutFlag() {
        return lockedOutFlag;
    }
    public void setLockedOutFlag(boolean lockedOutFlag) {
        this.lockedOutFlag = lockedOutFlag;
    }
    public boolean isPasswordExpiredFlag() {
        return passwordExpiredFlag;
    }
    public void setPasswordExpiredFlag(boolean passwordExpiredFlag) {
        this.passwordExpiredFlag = passwordExpiredFlag;
    }
    public int getPasswordFailedAttempts() {
        return passwordFailedAttempts;
    }
    public void setPasswordFailedAttempts(int passwordFailedAttempts) {
        this.passwordFailedAttempts = passwordFailedAttempts;
    }
    public Date getLastPasswordAttempt() {
        return lastPasswordAttempt;
    }
    public void setLastPasswordAttempt(Date lastPasswordAttempt) {
        this.lastPasswordAttempt = lastPasswordAttempt;
    }    
}
