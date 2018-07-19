package org.jumpmind.pos.core.screen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ApplicationState;
import org.jumpmind.pos.core.flow.ApplicationStateSerializer;
import org.jumpmind.pos.core.flow.FlowException;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.StateConfig;
import org.jumpmind.pos.core.service.IScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevToolsService {

    @Autowired
    private ApplicationStateSerializer applicationStateSerializer;

    @Autowired
    private IScreenService screenService;

    private static final String SAVE_PATH = "./savepoints";

    final Logger logger = LoggerFactory.getLogger(getClass());

    public Screen createMessage(IStateManager sm) {
        Screen screen = new Screen();
        screen.setType(ScreenType.DevTools);
        screen.setName("DevTools::Get");
        screen.setRefreshAlways(true);
        setScopes(sm, screen);
        setCurrentStateAndActions(sm, screen);
        return screen;
    }

    public Screen handleRequest(Action action, IStateManager stateManager, String appId, String nodeId) {
        if (action.getName().contains("::Save")) {
            String saveName = action.getName().substring(16);
            saveState(stateManager, saveName);
        } else if (action.getName().contains("DevTools::Load")) {
            String saveName = action.getName().substring(16);
            loadState(stateManager, saveName);
        } else if (action.getName().contains("DevTools::RemoveSave")) {
            String saveName = action.getName().substring(22);
            removeSave(saveName);
        } else if (action.getName().contains("::Remove")) {
            Map<String, String> element = action.getData();
            String scopeId = element.get("ID");
            if (action.getName().contains("::Node")) {
                stateManager.getApplicationState().getScope().removeNodeScope(scopeId);
            } else if (action.getName().contains("::Session")) {
                stateManager.getApplicationState().getScope().removeSessionScope(scopeId);
            } else if (action.getName().contains("::Conversation")) {
                stateManager.getApplicationState().getScope().removeConversationScope(scopeId);
            } else if (action.getName().contains("::Config")) {
                stateManager.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope().remove(scopeId);
            } else if (action.getName().contains("::Flow")) {
                stateManager.getApplicationState().getCurrentContext().removeFlowScope(scopeId);
            }

        }

        return createMessage(stateManager);
    }

    private void setCurrentStateAndActions(IStateManager sm, Screen screen) {
        loadSaveFiles(screen);

        List<String> actions = new ArrayList<>();
        IState currentState = sm.getCurrentState();
        FlowConfig fc = sm.getApplicationState().getCurrentContext().getFlowConfig();
        if (fc != null) {
            StateConfig sc = fc.getStateConfig(currentState);
            Set<String> keySet = sc.getActionToStateMapping().keySet();
            actions.clear();
            for (String key : keySet) {
                actions.add(key);
                actions.add(sc.getActionToStateMapping().get(key).toString().replace("class ", ""));
            }
            screen.put("actions", actions);
            screen.put("actionsSize", actions.size());
            screen.put("currentState", sm.getApplicationState().getCurrentContext().getFlowConfig().getStateConfig(currentState));
        }
    }

    public void loadSaveFiles(Screen screen) {

        List<String> saveFiles = new ArrayList<>();
        File dir = null;
        try {
            dir = new File(SAVE_PATH);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                int i = 0;
                for (File child : directoryListing) {
                    saveFiles.add(child.getName().replaceAll(".json", ""));
                    i++;
                }
                logger.info("Loaded " + i + " save files from " + dir);
            } else {
                logger.warn("No save files found in save directory.");
            }
        } catch (Exception ex) {
            logger.warn("Cannot load save files from " + SAVE_PATH);
        }

        screen.put("saveFiles", saveFiles);
    }

    private void setScopes(IStateManager sm, Screen screen) {
        try {
            Map<String, List<ScopeField>> scopes = new HashMap<>();
            scopes.put("ConversationScope", buildScope(sm.getApplicationState().getScope().getConversationScope()));
            scopes.put("NodeScope", buildScope(sm.getApplicationState().getScope().getNodeScope()));
            scopes.put("SessionScope", buildScope(sm.getApplicationState().getScope().getSessionScope()));
            scopes.put("FlowScope", buildScope(sm.getApplicationState().getCurrentContext().getFlowScope()));
            scopes.put("ConfigScope", buildConfigScope(sm.getApplicationState().getCurrentContext().getFlowConfig().getConfigScope()));
            screen.put("scopes", scopes);
        } catch (Exception ex) {
            logger.warn("Error loading in Developer Tool application scopes. Deleting local storage may fix this.", ex);
        }
    }

    private List<ScopeField> buildScope(Map<String, ScopeValue> map) {
        Set<String> keys = map.keySet();
        List<ScopeField> res = new ArrayList<>();
        for (String key : keys) {
            res.add(new ScopeField(key, map.get(key).getCreatedTime().toString(),
                    map.get(key).getCreatedStackTrace().replaceAll("at ", "\nat_")));
        }
        return res;
    }

    private List<ScopeField> buildConfigScope(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        List<ScopeField> res = new ArrayList<>();
        for (String key : keys) {
            res.add(new ScopeField(key, map.get(key).toString(), map.get(key).toString().replaceAll("at ", "\nat_")));
        }
        return res;
    }

    public void saveState(IStateManager sm, String saveName) {
        String filename = SAVE_PATH + saveName + ".json";
        try {
            applicationStateSerializer.serialize(sm, sm.getApplicationState(), filename);
            logger.info("Saved state " + filename);
        } catch (FlowException ex) {
            logger.info(ex.getMessage());
        } catch (Exception ex) {
            logger.warn("Unable to save state at " + filename, ex);
        }
    }

    public void loadState(IStateManager sm, String saveName) {
        String filename = SAVE_PATH + saveName + ".json";

        boolean resumeState = false;

        try {
            ApplicationState applicationState = new ApplicationState();
            applicationState = applicationStateSerializer.deserialize(sm, filename);
            sm.setApplicationState(applicationState);
            screenService.setApplicationState(applicationState);
            sm.getApplicationState().getScope().setNodeScope("stateManager", sm);
            resumeState = true;
            logger.info("Loaded save state " + filename);
        } catch (FlowException ex) {
            logger.info(ex.getMessage());
        } catch (Exception ex) {
            logger.warn("Failed to load save state " + filename, ex);
        }

        if (resumeState) {
            sm.refreshScreen();
        }

    }

    public void removeSave(String saveName) {
        String filename = SAVE_PATH + saveName + ".json";
        File save = null;
        try {
            save = new File(filename);
        } catch (Exception ex) {
            logger.warn("Can't find save state " + filename, ex);
        }
        if (save != null && save.delete()) {
            logger.info("Successfully deleted save state " + filename);
        } else {
            logger.warn("Failed to delete save state " + filename);
        }
    }

}
