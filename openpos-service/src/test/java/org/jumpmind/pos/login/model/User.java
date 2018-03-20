package org.jumpmind.pos.login.model;

import java.util.Date;

public class User extends Model {
    
    private String userName;
    private String lastName;
    private String firstName;
    private Date lastLogin = new Date();
    private boolean lockedOutFlag = false;
    private boolean passwordExpiredFlag = false;
    private int passwordFailedAttempts= 0;
    private Date lastPasswordAttempt = new Date();      
    
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
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
