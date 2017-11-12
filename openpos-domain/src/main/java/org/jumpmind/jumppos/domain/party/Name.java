package org.jumpmind.jumppos.domain.party;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * This is a holder for name related data.
 */
@Embeddable
public class Name implements Serializable {

    private static final long serialVersionUID = 1L;

    String salutation;

    String firstName;

    String lastName;

    String middleNames;

    String suffix;
    
    String nickName;
    
    public Name() {}
    
    public Name(String lastName, String firstname, String middleNames, String nickname, String salutation, String suffix)
    {
        this.lastName = lastName;
        this.firstName = firstname;
        this.middleNames = middleNames;
        this.salutation = salutation;
        this.suffix = suffix;
        this.nickName = nickname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(String middleNames) {
        this.middleNames = middleNames;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return Returns the preferredName.
     */
    public String getNickName() {
        return nickName;
    }
    
    /**
     * @param preferredName The preferredName to set.
     */
    public void setNickName(String preferredName) {
        this.nickName = preferredName;
    }
}
