package org.jumpmind.pos.core.screeninterceptor;


import org.jumpmind.pos.core.screen.Toast;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("device")
public class ToastPropertyCrawlerInterceptor extends AbstractMessagePropertyCrawlerInterceptor<Toast> {
}
