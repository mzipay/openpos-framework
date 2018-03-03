package org.jumpmind.pos.core.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.FlowException;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.IStateManagerFactory;
import org.jumpmind.pos.core.model.ComboField;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.model.FormField;
import org.jumpmind.pos.core.model.FormListField;
import org.jumpmind.pos.core.model.IFormElement;
import org.jumpmind.pos.core.model.ToggleField;
import org.jumpmind.pos.core.model.annotations.FormButton;
import org.jumpmind.pos.core.model.annotations.FormTextField;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.DialogScreen;
import org.jumpmind.pos.core.screen.DynamicFormScreen;
import org.jumpmind.pos.core.screen.FormScreen;
import org.jumpmind.pos.core.screen.ScreenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
@CrossOrigin
@Controller
public class ScreenService implements IScreenService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    IStateManagerFactory stateManagerFactory;

    int screenSequenceNumber = 0;

    private Map<String, Map<String, DefaultScreen>> lastScreenByAppIdByNodeId = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET, value = "ping")
    @ResponseBody
    public String ping() {
        logger.info("Received a ping request");
        return "{ \"pong\": \"true\" }";
    }

    @RequestMapping(method = RequestMethod.GET, value = "app/{appId}/node/{nodeId}/{action}/{payload}")
    public void getAction(@PathVariable String appId, @PathVariable String nodeId, @PathVariable String action, @PathVariable String payload,
            HttpServletResponse resp) {
        logger.info("Received a request for {} {} {} {}", appId, nodeId, action, payload);
        IStateManager stateManager = stateManagerFactory.retreive(appId, nodeId);
        if (stateManager != null) {
            logger.info("Calling stateManager.doAction with: {}", action);
            stateManager.doAction(new Action(action, payload));
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "api/app/{appId}/node/{nodeId}/control/{controlId}")
    @ResponseBody
    public String getComponentValues(@PathVariable String appId, @PathVariable String nodeId, @PathVariable String controlId) {
        DefaultScreen defaultScreen = getLastScreen(appId, nodeId);
        DynamicFormScreen dynamicScreen = null;
        if (defaultScreen instanceof DynamicFormScreen) {
            dynamicScreen = (DynamicFormScreen) defaultScreen;
            IFormElement formElement = dynamicScreen.getForm().getFormElement(controlId);
            
            // TODO: Look at combining FormListField and ComboField or at least inheriting off of each other.
            List<String> valueList = null;
            if (formElement instanceof FormListField) {
                valueList = ((FormListField) formElement).getValues();
            } else if (formElement instanceof ComboField) {
                valueList = ((ComboField) formElement).getValues();
            } else if (formElement instanceof ToggleField) {
                valueList = ((ToggleField) formElement).getValues();
            }
            if (valueList != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    mapper.writeValue(out, valueList);
                } catch (IOException e) {
                    throw new RuntimeException("Error while serializing the component values.", e);
                }
                return new String(out.toByteArray());
            }
        }
        return "{}";
    }

    @MessageMapping("action/app/{appId}/node/{nodeId}")
    public void action(@DestinationVariable String appId, @DestinationVariable String nodeId, Action action) {
        try {
            logger.info("Received action from {}\n{}", nodeId, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(action));
        } catch (JsonProcessingException ex) {
            logger.error("Failed to write action to JSON", ex);
        }
        DefaultScreen lastScreen = getLastScreen(appId, nodeId);
        if (lastScreen instanceof DialogScreen) {
            publishToClients(appId, nodeId, "{\"clearDialog\":true }");
        }
        IStateManager stateManager = stateManagerFactory.retreive(appId, nodeId);
        if (stateManager != null) {
            logger.info("Posting action of {}", action);
            stateManager.doAction(action);
        }
    }

    @Override
    public DefaultScreen getLastScreen(String appId, String nodeId) {
        Map<String, DefaultScreen> lastScreenByNodeId = lastScreenByAppIdByNodeId.get(appId);
        if (lastScreenByNodeId != null) {
            return lastScreenByNodeId.get(nodeId);
        } else {
            return null;
        }
    }

    @Override
    public void showScreen(String appId, String nodeId, DefaultScreen screen) {
        if (screen != null) {
            screen.setSequenceNumber(++this.screenSequenceNumber);
            Object payload = screen;
            try {
                applyAnnotations(screen);
                if (screen.isScreenOfType(ScreenType.Form) && !(screen instanceof FormScreen)) {
                    Form form = buildForm(screen);
                    screen.put("form", form);
                }
                logger.info("Show screen on nodeId " + nodeId + "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
            } catch (JsonProcessingException ex) {
                if (ex.toString().contains("org.jumpmind.pos.core.screen.ChangeScreen")) {
                    logger.error("Failed to write screen to JSON. Verify the screen type has been configured by calling setType() on the screen object.", ex);
                } else {
                    logger.error("Failed to write screen to JSON", ex);
                }
            }
            publishToClients(appId, nodeId, payload);
            Map<String, DefaultScreen> lastScreenByNodeId = lastScreenByAppIdByNodeId.get(appId);
            if (lastScreenByNodeId == null) {
                lastScreenByNodeId = new HashMap<>();
                lastScreenByAppIdByNodeId.put(appId, lastScreenByNodeId);
            }
            lastScreenByNodeId.put(nodeId, screen);
        }
    }

    protected void publishToClients(String appId, String nodeId, Object payload) {
        this.template.convertAndSend("/topic/app/" + appId + "/node/" + nodeId, payload);
    }

    @Override
    public DefaultScreen deserializeScreenPayload(String appId, String nodeId, Action action) {
        Map<String, DefaultScreen> lastScreenByNodeId = lastScreenByAppIdByNodeId.get(appId);
        if (lastScreenByNodeId != null) {
            DefaultScreen lastScreen = lastScreenByNodeId.get(nodeId);
            if (lastScreen != null && 
                    (lastScreen instanceof FormScreen || lastScreen instanceof DynamicFormScreen)) {
                Form form = null;
                try {
                    form = mapper.convertValue(action.getData(), Form.class);
                    if (form != null) { // A form that has display only fields won't
                        // have any data
                        return populateFormScreen(appId, nodeId, form);
                    }
                } catch (IllegalArgumentException ex) {
                    // We should not assume a form will always be returned by the DynamicFormScreen.
                    // The barcode scanner can also return a value.
                    // TODO: Allow serializing more than the form on an action.
                }
            }
        }
        return null;
    }

    protected DefaultScreen populateFormScreen(String appId, String nodeId, Form form) {
        DefaultScreen lastScreen = null;
        Map<String, DefaultScreen> lastScreenByNodeId = lastScreenByAppIdByNodeId.get(appId);
        if (lastScreenByNodeId != null) {
            lastScreen = lastScreenByNodeId.get(nodeId);

            for (IFormElement formElement : form.getFormElements()) {
                if (formElement instanceof FormField) {
                    FormField formField = (FormField) formElement;
                    String fieldId = formField.getId();
                    if (lastScreen instanceof FormScreen) {
                        FormScreen formScreen = (FormScreen) lastScreen;
                        formScreen.setForm(form);
                    } else if (lastScreen instanceof DynamicFormScreen) {
                        DynamicFormScreen formScreen = (DynamicFormScreen) lastScreen;
                        formScreen.setForm(form);
                    } else {
                        for (Field field : lastScreen.getClass().getDeclaredFields()) {
                            FormTextField textFieldAnnotation = field.getAnnotation(FormTextField.class);
                            if (textFieldAnnotation != null) {
                                if (field.getName().equals(fieldId)) {
                                    setFieldValue(field, lastScreen, formField.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }

        return lastScreen;
    }

    protected Form buildForm(DefaultScreen screen) {
        Form form = new Form();

        for (Field field : screen.getClass().getDeclaredFields()) {
            FormTextField textFieldAnnotation = field.getAnnotation(FormTextField.class);
            if (textFieldAnnotation != null) {
                FormField formField = new FormField();
                formField.setElementType(textFieldAnnotation.fieldElementType());
                formField.setInputType(textFieldAnnotation.fieldInputType());
                formField.setId(field.getName());
                formField.setLabel(textFieldAnnotation.label());
                formField.setPlaceholder(textFieldAnnotation.placeholder());
                formField.setPattern(textFieldAnnotation.pattern());
                formField.setValue(getFieldValueAsString(field, screen));
                formField.setRequired(textFieldAnnotation.required());
                form.addFormElement(formField);
            }
            FormButton buttonAnnotation = field.getAnnotation(FormButton.class);
            if (buttonAnnotation != null) {
                org.jumpmind.pos.core.model.FormButton button = new org.jumpmind.pos.core.model.FormButton();
                button.setLabel(buttonAnnotation.label());
                button.setButtonAction(getFieldValueAsString(field, screen));
                form.addFormElement(button);
            }
        }
        return form;
    }

    protected void applyAnnotations(DefaultScreen screen) {
        org.jumpmind.pos.core.model.annotations.Screen screenAnnotation = screen.getClass()
                .getAnnotation(org.jumpmind.pos.core.model.annotations.Screen.class);
        if (screenAnnotation != null) {
            screen.setName(screenAnnotation.name());
            screen.setType(screenAnnotation.type());
        }
    }

    @Override
    public void refresh(String appId, String nodeId) {
        Map<String, DefaultScreen> lastScreenByNodeId = lastScreenByAppIdByNodeId.get(appId);
        if (lastScreenByNodeId != null) {
            showScreen(appId, nodeId, lastScreenByNodeId.get(nodeId));
        }
    }

    protected void setFieldValue(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception ex) {
            throw new FlowException("Field to set value " + value + " for field " + field + " from target " + target, ex);
        }
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
