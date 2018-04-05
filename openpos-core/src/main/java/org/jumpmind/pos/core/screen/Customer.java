package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class Customer implements Serializable {

	private String fullName;
	private String firstName;
	private String lastName;
	private String fullAddress;
	private String addressLine;
	private String city;
	private String state;
	private String postalCode;
	private String email;
	private String birthday;
	private String loyaltyId;
	
	
    private static final long serialVersionUID = 1L;
    
    public Customer() {
    }
    
    public Customer(String fullName) {
        super();
        this.fullName = fullName;
    }
    
    public Customer(String firstName, String lastName) {
    		super();
    		this.firstName = firstName;
    		this.lastName = lastName;
    		this.fullName = firstName + " " + lastName;
    }
    
    public Customer(String firstName, String lastName, String loyaltyId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
		this.loyaltyId = loyaltyId;
}
    
    public Customer(String fullName, String addressLine, String city, String state, String postalCode, String email, String birthday, String loyaltyId) {
    		super();
    		this.fullName = fullName;
    		this.addressLine = addressLine;
    		this.city = city;
    		this.state = state;
    		this.postalCode = postalCode;
    		this.email = email;
    		this.birthday = birthday;
    		this.loyaltyId = loyaltyId;
    		this.fullAddress = formatFullAddress();
    }
    
    public Customer(String firstName, String lastName, String addressLine, String city, String state, String postalCode, String email, String birthday, String loyaltyId) {
    		super();
    		this.firstName = firstName;
    		this.lastName = lastName;
    		this.fullName = firstName + " " + lastName;
    		this.addressLine = addressLine;
    		this.city = city;
    		this.state = state;
    		this.postalCode = postalCode;
    		this.email = email;
    		this.birthday = birthday;
    		this.loyaltyId = loyaltyId;
    		this.fullAddress = formatFullAddress();
    }
    
    public String getName() {
        return fullName;
    }
    
    public void setName(String name) {
        this.fullName = name;
    }
    
    public String getFirstName() {
    		return firstName;
    }
    
    public void setFirstName(String firstName) { 
    		this.firstName = firstName;
    }
    
    public String getLastName() {
    		return lastName;
    }
    
    public void setLastName(String lastName) {
    		this.lastName = lastName;
    }
    
    public String getAddressLine() {
    		return addressLine;
    }
    
    public void setAddressLine(String addressLine) {
    		this.addressLine = addressLine;
    }
    
    public String getCity() {
    		return city;
    }
    
    public void setCity(String city) {
    		this.city = city;
    }
    
    public String getState() {
    		return state;
    }
    
    public void setState(String state) {
    		this.state = state;
    }
    
    public String getPostalCode() {
    		return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
    		this.postalCode = postalCode;
    }
    
    private String formatFullAddress() {
    		if(addressLine != null) {
			String address = addressLine;
			if(city != null) {
				address += ", " + city;
			}
			if(state != null) {
				address += ", " + state;
			}
			if(postalCode != null) {
				address += " " + postalCode;
			}
			return address;
		}
		return null;
    }
    
    public String getFullAddress() {
    		return fullAddress;
    }
    
    public String getEmail() {
    		return email;
    }
    
    public void setEmail(String email) {
    		this.email = email;
    }
    
    public String getBirthday() {
    		return birthday;
    }
    
    public void setBirthday(String birthday) {
    		this.birthday = birthday;
    }
    
    public String getLoyaltyId() {
    		return loyaltyId;
    }
    
    public void setLoyaltyId(String loyaltyId) {
    		this.loyaltyId = loyaltyId;
    }
}
