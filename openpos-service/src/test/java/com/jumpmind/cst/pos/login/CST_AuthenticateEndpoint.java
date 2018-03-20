package com.jumpmind.cst.pos.login;

import java.math.BigDecimal;

import org.jumpmind.pos.login.model.AuthenticationResult;
import org.jumpmind.pos.login.model.User;
import org.jumpmind.pos.login.service.AuthenticateEndpointTest;
import org.jumpmind.pos.service.EndpointOverride;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class CST_AuthenticateEndpoint {

    @EndpointOverride("/authenticate")
    public AuthenticationResult authenticate(
            AuthenticateEndpointTest baseEndpoint,
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="password", defaultValue="") String password) {
    
    System.out.println("Override code running - changing incoming password");
    
    AuthenticationResult result = baseEndpoint.authenticate(username, password);
    
    if (result.getResultStatus() == "SUCCESS") {   
        CST_AuthenticationResult urbnResult = new CST_AuthenticationResult();
        urbnResult.setLdapMessage("This is an LDAP message on the final result object."); // modify outgoing response.
        urbnResult.setEmployeeBucks(calculateEmployeeBucks(result.getUser()));
        result.setExtension(urbnResult);
    }       
    
    return result;
}

    public BigDecimal calculateEmployeeBucks(User user) {

    // Fabricated example to show BigDecimal use in Kotlin.
    // EmployeeBucks are 5 * number of letters in first name, minus 2 * letters in last name, all time 3, divided 2. 
    
//    var employeeBucks = ((10.d * user.firstName.length.d) - (2.d * user.lastName.length.d) * 3.d) / 2.d;
      BigDecimal employeeBucks = (new BigDecimal(10).multiply(new BigDecimal(user.getFirstName().length()))).subtract( 
              ( new BigDecimal(2).multiply(new BigDecimal(user.getLastName().length())).multiply(
                      new BigDecimal(3)) ).divide(new BigDecimal(2)));  
        
    return employeeBucks;
}       
    
}
