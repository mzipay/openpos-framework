package org.jumpmind.pos.app.model;

public class Login {

    boolean loggedIn;
    
    String userName;

    Empl employee;

    public Empl getEmployee() {
        return employee;
    }

    public void setEmployee(Empl employee) {
        this.employee = employee;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }

}
