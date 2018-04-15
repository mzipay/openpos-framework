package org.jumpmind.pos.translate.service;

import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.IStateManagerFactory;
import org.jumpmind.pos.translate.ILegacyStartupService;
import org.jumpmind.pos.translate.ITranslationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "register")
public class RegisterService {

    @Autowired
    protected ILegacyStartupService startupService;
    
    @Autowired
    protected IStateManagerFactory stateManagerFactory;
    
    @Autowired
    protected ITranslationManager translationManager;

    @RequestMapping(method = RequestMethod.GET, value = "restart/node/{nodeId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void restart(@PathVariable String nodeId) {
        this.startupService.restart(nodeId);
        IStateManager stateManager = stateManagerFactory.retrieve("pos", nodeId);
        stateManager.endConversation();
        stateManager.doAction("Restart");
    }
}
