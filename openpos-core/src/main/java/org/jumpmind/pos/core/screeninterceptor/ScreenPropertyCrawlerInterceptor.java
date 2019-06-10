package org.jumpmind.pos.core.screeninterceptor;


import org.jumpmind.pos.core.ui.UIMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("device")
public class ScreenPropertyCrawlerInterceptor extends AbstractMessagePropertyCrawlerInterceptor<UIMessage> {

    Logger logger = LoggerFactory.getLogger(getClass());
}
