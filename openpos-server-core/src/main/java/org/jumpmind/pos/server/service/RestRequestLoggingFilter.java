package org.jumpmind.pos.server.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class RestRequestLoggingFilter extends AbstractRequestLoggingFilter {

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if (log(request)) {
            log.info(message);
        }
    }

    protected boolean log(HttpServletRequest request) {
        return !request.getRequestURI().contains("/sql") && !request.getRequestURI().contains("/api/websocket") && !request.getRequestURI().contains("/logs/upload");
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if (log(request)) {
            log.info(message);
        }
    }

}
