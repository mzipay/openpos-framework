package org.jumpmind.jumppos.domain.security;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.party.Worker;


/**
 * An employee or any other authorized individual who may enter transactions
 * into the stores workstations. The OPERATOR is accountable for the tender
 * collected and dispensed while assigned to a WORKSTATION.
 */
@Entity
public class Operator extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy="operator")
    private Worker worker;
    
    @Id
    private String userName;

    @Lob
    private String hashedPassword;
    
    @Lob
    private String encryptedPassword;
    
    private int accessLevel;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    public int getAccessLevel() {
        return accessLevel;
    }
}
