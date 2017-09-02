package org.jumpmind.jumppos.domain.party;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * A individual of interest to the retail store or retail enterprise.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends BaseEntity {

    @Id
    String id;

    @Embedded
    Name name;

    int genderCode;

    String driversLicenseId;

    Date birthDate;
    
    String locale;

    public Person() {
        name = new Name();
    }

    public Person(Name name, Date birthDate, int genderCode) {
        this.setName(name);
        this.setBirthDate(birthDate);
        this.setGenderCode(genderCode);
    }

    public String getId() {
        return id;
    }

    public void setId(String personId) {
        this.id = personId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(int genderCode) {
        this.genderCode = genderCode;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getDriversLicenseId() {
        return driversLicenseId;
    }

    public void setDriversLicenseId(String driversLicenseId) {
        this.driversLicenseId = driversLicenseId;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public String getLocale() {
        return locale;
    }
}
