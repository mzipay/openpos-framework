package org.jumpmind.pos.core.ui.data;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class OrderCustomer implements Serializable {
    private String iconName;
    private String fullName;
    private String phone;
    private String email;
    private Address address;

    public OrderCustomer(String iconName, String firstName, String lastName, String phone, String email, String line1, String city, String state, String postalCode) {
        this.fullName = firstName.concat(" ").concat(lastName);
        this.phone = phone;
        this.email = email;
        this.address = new Address(line1, city, state, postalCode);
    }

    public OrderCustomer(String iconName, String fullName, String phone, String email, Address address) {
        this.iconName = iconName;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }


}
