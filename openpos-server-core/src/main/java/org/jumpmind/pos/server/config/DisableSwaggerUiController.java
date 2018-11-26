package org.jumpmind.pos.server.config;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Profile("!swagger-ui")
@RestController
/**
 * Disables the Swagger main UI page unless the Spring profile named 'swagger-ui' is active.
 *
 */
public class DisableSwaggerUiController {

    @RequestMapping(value = "swagger-ui.html", method = RequestMethod.GET)
    public void getSwagger(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }
}