package org.jumpmind.pos.app.model;

public class Empl extends AbstractObject {

    private static final long serialVersionUID = 1L;
    
    String firstName;
    String lastName;
    long emplId;
    
    public Empl() {     
    }

    public Empl(long employeeId, String firstName, String lastName) {
        this.emplId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    
}
