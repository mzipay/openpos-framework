package org.jumpmind.pos.user.model;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(description="Security Identifier granting and denying access to the systems of the retail enterprise, and recorded upon the transaction originating from those systems.")
public class User extends Entity {

    @Column(naturalKey=true)
    private String username;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Column
    private String nickname;    
    @Column
    private Date lastLogin;
    @Column
    private boolean lockedOutFlag;
    @Column
    private boolean passwordExpiredFlag = false;
    @Column
    private int passwordFailedAttempts= 0;
    @Column
    private Date lastPasswordAttempt;
    
    private List<PasswordHistory> passwordHistory = new ArrayList<>();
    
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

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public List<PasswordHistory> getPasswordHistory() {
        return passwordHistory;
    }
    public void setPasswordHistory(List<PasswordHistory> passwordHistory) {
        this.passwordHistory = passwordHistory;
    }
    
    public void addPasswordHistory(PasswordHistory passwordHistoryEntry) {
        passwordHistoryEntry.setUsername(this.getUsername());
        passwordHistoryEntry.setPasswordSequence(passwordHistory.size()+1);
        passwordHistory.add(passwordHistoryEntry);
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
