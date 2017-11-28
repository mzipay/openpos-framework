package org.jumpmind.pos.app.model;

public class Employee {

    String firstName;
    String lastName;
    long employeeId;
    
    public Employee() {     
    }

    public Employee(long employeeId, String firstName, String lastName) {
        super();
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    
}
