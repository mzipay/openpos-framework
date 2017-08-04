package org.jumpmind.jumppos.core.web;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Payload;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.flow.FlowException;
import org.jumpmind.jumppos.core.flow.IScreenService;
import org.jumpmind.jumppos.core.flow.IStateManager;
import org.jumpmind.jumppos.core.flow.IStateManagerFactory;
import org.jumpmind.jumppos.core.model.Form;
import org.jumpmind.jumppos.core.model.FormField;
import org.jumpmind.jumppos.core.model.IScreen;
import org.jumpmind.jumppos.core.model.annotations.FormButton;
import org.jumpmind.jumppos.core.model.annotations.FormTextField;
import org.jumpmind.jumppos.core.model.annotations.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ScreenService implements IScreenService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    IStateManagerFactory stateManagerFactory;

    private Map<String, IScreen> lastScreenByNodeId = new HashMap<String, IScreen>();

    @MessageMapping("action/node/{nodeId}")
    public void action(@DestinationVariable String nodeId, Action action) {
        try {
            logger.info("Received action from {}\n{}", nodeId, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(action));
        } catch (JsonProcessingException ex) {
            logger.error("Failed to write action to JSON", ex);
        }
        IStateManager stateManager = stateManagerFactory.retreiveOrCreate(nodeId);
        stateManager.setNodeId(nodeId);
        if (stateManager != null) {
            logger.info("Posting action of {}", action);
            stateManager.doAction(action);
        }
    }

    @Override
    public IScreen getLastScreen(String nodeId) {
        return lastScreenByNodeId.get(nodeId);
    }

    @Override
    public void showScreen(String nodeId, IScreen screen) {
        if (screen != null) {
            Object payload = screen;
            try {
                applyAnnotations(screen);
                if (screen.getType() != null && screen.getType().equals(IScreen.FORM_SCREEN_TYPE)) {
                    Form form = buildForm(screen);
                    screen.put("form", form);
                }
                logger.info("Show screen on nodeId " + nodeId + "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
            } catch (JsonProcessingException ex) {
                logger.error("Failed to write screen to JSON", ex);
            }
            this.template.convertAndSend("/topic/node/" + nodeId, payload);
            lastScreenByNodeId.put(nodeId, screen);
        }
    }

    protected Form buildForm(IScreen screen) {

        Form form = new Form();

        for (Field field : screen.getClass().getDeclaredFields()) {
            FormTextField textFieldAnnotation = field.getAnnotation(FormTextField.class);
            if (textFieldAnnotation != null) {
                FormField formField = new FormField();
                formField.setElementType("input"); // TODO support other types here?
                formField.setFieldId(field.getName());
                formField.setLabel(textFieldAnnotation.label());
                formField.setPlaceholder(textFieldAnnotation.placeholder());
                formField.setValue(getFieldValueAsString(field, screen));
                form.addFormElement(formField);
            }
            FormButton buttonAnnotation = field.getAnnotation(FormButton.class);
            if (buttonAnnotation != null) {
                org.jumpmind.jumppos.core.model.FormButton button = new org.jumpmind.jumppos.core.model.FormButton();
                button.setLabel(buttonAnnotation.label());
                button.setButtonAction(getFieldValueAsString(field, screen));
                form.addFormElement(button);                
            }
        }
        return form;
    }

    protected void applyAnnotations(IScreen screen) {
        Screen screenAnnotation = screen.getClass().getAnnotation(Screen.class);
        if (screenAnnotation != null) {
            screen.setName(screenAnnotation.name());
            screen.setType(screenAnnotation.type());
        }
    }

    @Override
    public void refresh(String clientId) {
        showScreen(clientId, lastScreenByNodeId.get(clientId));
    }

    protected String getFieldValueAsString(Field field, Object target) {
        try {
            field.setAccessible(true);
            Object value = field.get(target);
            if (value != null) {
                return String.valueOf(value);
            } else {
                return null;
            }
            
        } catch (Exception ex) {
            throw new FlowException("Field to get value for field " + field + " from target " + target, ex);
        }
    }
}
