package com.jumpmind.cst.pos.login;

import java.math.BigDecimal;

public class CST_AuthenticationResult {
    
    private String ldapMessage;
    private BigDecimal employeeBucks;
    
    public String getLdapMessage() {
        return ldapMessage;
    }
    public void setLdapMessage(String ldapMessage) {
        this.ldapMessage = ldapMessage;
    }
    public BigDecimal getEmployeeBucks() {
        return employeeBucks;
    }
    public void setEmployeeBucks(BigDecimal employeeBucks) {
        this.employeeBucks = employeeBucks;
    }

}
