package org.jumpmind.pos.translate;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang.WordUtils;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.IUIAction;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.Workstation;
import org.jumpmind.pos.translate.ILegacyRegisterStatusService.Status;


public abstract class AbstractLegacyScreenTranslator <T extends DefaultScreen> extends AbstractScreenTranslator<T>{
    
    // TODO: Move elsewhere?
    public final static String LOCAL_NAV_PANEL_KEY = "LocalNavigationPanel";
    public final static String GLOBAL_NAV_PANEL_KEY = "GlobalNavigationPanel";
    public final static String PROMPT_RESPONSE_PANEL_KEY = "PromptAndResponsePanel";
    public final static String WORK_PANEL_KEY = "WorkPanel";
    public final static String STATUS_PANEL_KEY = "StatusPanel";

    private ILegacyUtilityManager legacyUtilityManager;

    protected ILegacyPOSBeanService legacyPOSBeanService;
    protected ILegacyStoreProperties legacyStoreProperties;
    
    public AbstractLegacyScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
    }

    public void setLegacyPOSBeanService(ILegacyPOSBeanService beanService) {
        this.legacyPOSBeanService = beanService;
    }
    
    public ILegacyPOSBeanService getLegacyPOSBeanService() {
        return this.legacyPOSBeanService;
    }
    
    public ILegacyStoreProperties getLegacyStoreProperties() {
        return legacyStoreProperties;
    }

    public void setLegacyStoreProperties(ILegacyStoreProperties legacyStoreProperties) {
        this.legacyStoreProperties = legacyStoreProperties;
    }

    @Override
    protected void buildMainContent() {
        buildBackButton();
        logAvailableLocalMenuItems();
        buildStatusItems();        
        Workstation workstation = new Workstation();
        workstation.setStoreId(legacyStoreProperties.getStoreNumber());
        workstation.setWorkstationId(legacyStoreProperties.getWorkstationNumber());
        screen.setWorkstation(workstation);
    }

    protected void buildStatusItems() {
        screen.setOperatorName(WordUtils.capitalizeFully(posSessionInfo.getOperatorName()));
        screen.setRegisterStatus(posSessionInfo.isRegisterOpen()
                .map(registerOpen -> new MenuItem((registerOpen ? DefaultScreen.TITLE_OPEN_STATUS : DefaultScreen.TITLE_CLOSED_STATUS), "", true)).orElse(null));
        screen.setStoreStatus(posSessionInfo.isStoreOpen()
                .map(storeOpen -> new MenuItem((storeOpen ? DefaultScreen.TITLE_OPEN_STATUS : DefaultScreen.TITLE_CLOSED_STATUS), "", true)).orElse(null));
        screen.setName(getScreenName());
    }
    
    protected String getScreenName() {
        ILegacyStatusBeanModel statusModel =  legacyPOSBeanService.getLegacyStatusBeanModel(legacyScreen);
        if (statusModel != null && statusModel.getScreenName() != null) {
            return statusModel.getScreenName();
        } else {
            ILegacyAssignmentSpec statusPanelSpec = legacyPOSBeanService.getLegacyAssignmentSpec(legacyScreen, STATUS_PANEL_KEY);
            String labelTag = getSpecPropertyValue(statusPanelSpec, "screenNameTag", null);
            if (labelTag != null) {                
                return legacyPOSBeanService.getLegacyUtilityManager(legacyScreen).retrieveText("StatusPanelSpec", getResourceBundleFilename(), labelTag, labelTag);
            } else {
                return null;
            }
        }
    }
    
    protected void logAvailableLocalMenuItems() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(LOCAL_NAV_PANEL_KEY);
        if (assignmentPanelSpec != null) {
            ILegacyBeanSpec localNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(this.legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> enabledState = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(localNavSpec.getButtons())
                    .filter(buttonSpec -> Optional.ofNullable(enabledState.get(buttonSpec.getActionName())).orElse(buttonSpec.getEnabled()))
                    .forEachOrdered(enabledButtonSpec -> {
                        logger.info("Available local menu action: {}", enabledButtonSpec.getActionName());
                    });
            
        }
    }
    
    protected ILegacyUIModel getLegacyUIModel() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(WORK_PANEL_KEY);
        ILegacyUIModel legacyUIModel = null;
        if (assignmentPanelSpec != null) {
            logger.trace("The work panel bean spec name was {}", assignmentPanelSpec.getBeanSpecName());

            legacyUIModel = this.legacyPOSBeanService.getLegacyUIModel(legacyScreen);
            
        }

        return legacyUIModel;
    }

    protected ILegacyBus getBus() {
        return this.legacyPOSBeanService.getLegacyBus(legacyScreen);
    }

    protected void buildBackButton() {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(GLOBAL_NAV_PANEL_KEY);
        if (null != assignmentPanelSpec) {
            ILegacyBeanSpec globalNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(this.legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> enabledState = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(globalNavSpec.getButtons())
                    .filter(buttonSpec -> Optional.ofNullable(enabledState.get(buttonSpec.getActionName())).orElse(buttonSpec.getEnabled()))
                    .forEachOrdered(enabledButtonSpec -> {
                        if ("Undo".equals(enabledButtonSpec.getLabelTag())) {
                            screen.setBackButton(new MenuItem("Back", enabledButtonSpec.getActionName(), true));
                        }
                    });
            
        }
    }

    protected String getPanelPropertyValue(String panelName, String propertyName) {
        String propertyValue = propertyName; // default to propertyName
        ILegacyAssignmentSpec assignmentSpec = this.getLegacyAssignmentSpec(panelName);
        if (assignmentSpec != null) {
            propertyValue = this.getLegacyUtilityManager().retrieveText(assignmentSpec.getBeanSpecName(), getResourceBundleFilename(), propertyName, propertyName);
        }
        return propertyValue;
        
    }
    
    protected String getWorkPanelPropertyValue(String propertyName) {
        String propertyValue = propertyName; // default to propertyName
        ILegacyAssignmentSpec assignmentSpec = this.getLegacyAssignmentSpec();
        if (assignmentSpec != null) {
            propertyValue = this.getLegacyUtilityManager().retrieveText(assignmentSpec.getBeanSpecName(), getResourceBundleFilename(), propertyName, propertyName);
        }
        return propertyValue;
    }
    
    protected String getSpecPropertyValue(ILegacyAssignmentSpec spec, String key, String modelValue) {
        if (isBlank(modelValue)) {
            String propValue = spec.getPropertyValue(key);
            if (propValue != null) {
                return propValue;
            } else {
                return null;
            }
        } else {
            return modelValue;
        }
    }
    
    protected void updatePosSessionInfo() {
        if (legacyScreen != null) {
            ILegacyRegisterStatusService registerStatusService = this.legacyPOSBeanService.getLegacyRegisterStatusService(legacyScreen);
            if (registerStatusService.isStatusDeterminable()) {
                posSessionInfo.setRegisterOpen(Optional.of(registerStatusService.getRegisterStatus() == Status.OPEN));

                Status storeStatus = registerStatusService.getStoreStatus();
                if (storeStatus != Status.UNKNOWN) {
                    posSessionInfo.setStoreOpen(Optional.of((storeStatus == Status.OPEN)));
                }
            }          
            
            ILegacyBus bus =  legacyPOSBeanService.getLegacyBus(legacyScreen);
            ILegacyCargo cargo =  bus.getLegacyCargo();
            if (cargo != null) {
                posSessionInfo.setOperatorName(cargo.getOperatorFirstLastName());
            }
            
        }
    }
    
    
    public ILegacyUtilityManager getLegacyUtilityManager() {
        if (legacyUtilityManager == null) {
            legacyUtilityManager = this.legacyPOSBeanService.getLegacyUtilityManager(legacyScreen);
        }
        
        return legacyUtilityManager;
    }

    protected String getResourceBundleFilename() {
        return this.legacyScreen.getResourceBundleFilename();
    }

    protected List<ILegacyButtonSpec> getPanelButtons(String panelKey, Optional<Boolean> enabledButtonsOnlyOpt) {
        ILegacyAssignmentSpec assignmentPanelSpec = getLegacyAssignmentSpec(panelKey);
        List<ILegacyButtonSpec> buttons = new ArrayList<>();

        if (assignmentPanelSpec != null) {
            ILegacyBeanSpec localNavSpec = this.legacyPOSBeanService.getLegacyBeanSpec(legacyScreen, assignmentPanelSpec.getBeanSpecName());
            Map<String, Boolean> buttonStateMap = parseButtonStates(assignmentPanelSpec);

            Arrays.stream(localNavSpec.getButtons()).filter(buttonSpec -> {
                if (enabledButtonsOnlyOpt.orElse(false)) {
                    // If only enabled buttons are wanted, use the button spec
                    // enabled/disabled status to
                    // only keep the enabled buttons
                    Boolean buttonState = buttonStateMap.get(buttonSpec.getActionName());
                    return buttonState != null ? buttonState : buttonSpec.getEnabled();
                } else { // Else don't filter any of the buttons
                    return true;
                }
            }).forEachOrdered(buttonSpec -> {
                buttons.add(buttonSpec);
                logger.info("Available local menu action: {}", buttonSpec.getActionName());
            });
            
        }
        return buttons;
    }

    protected ILegacyAssignmentSpec getLegacyAssignmentSpec() {
        return getLegacyAssignmentSpec(WORK_PANEL_KEY);
        
    }

    public ILegacyPromptAndResponseModel getPromptAndResponseModel() {
        return this.legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);
    }
    
    protected String retrieveCommonText(String propName, Optional<String> defaultValue) {
        String commonText = defaultValue.isPresent() ? 
                this.legacyPOSBeanService.getLegacyUIUtilities().retrieveCommonText(propName, defaultValue.get()) : 
                this.legacyPOSBeanService.getLegacyUIUtilities().retrieveCommonText(propName);
        return commonText;
    }
    
    protected String toFormattedString(Properties resourceBundle, String propertyName, Optional<String[]> args) {
        String text = (String) resourceBundle.get(propertyName);
        if (args.isPresent() && isNotBlank(text)) {
            text = this.toFormattedString(text, args.get());
        }
        return text;
    }

    protected String toFormattedString(String stringToFormat, String[] args) {
        String formattedText = this.legacyPOSBeanService.getLegacyLocaleUtilities().formatComplexMessage(stringToFormat, args);
        return formattedText;
    }
    
    protected String toFormattedString(Properties resourceBundle, String propertyName, String[] args) {
        return toFormattedString(resourceBundle, propertyName, Optional.ofNullable(args));
    }
    
    protected ILegacyAssignmentSpec getLegacyAssignmentSpec(String panelKey) {
        ILegacyAssignmentSpec spec = this.legacyPOSBeanService.getLegacyAssignmentSpec(this.legacyScreen, panelKey);
        return spec;
    }

    @Override
    protected void chooseScreenName() {
        String screenName = null;
        ILegacyStatusBeanModel statusModel = this.legacyPOSBeanService.getLegacyStatusBeanModel(legacyScreen);
        if (statusModel != null && statusModel.getScreenName() != null) {
            screenName = statusModel.getScreenName();
        } else {
            String labelTag = getSpecPropertyValue(getLegacyAssignmentSpec(STATUS_PANEL_KEY), "screenNameTag", null);
            if (labelTag != null) {
                screenName = this.getLegacyUtilityManager().retrieveText("StatusPanelSpec", getResourceBundleFilename(), labelTag, labelTag);
            }
        }
        
        if (getScreen().getName() == null) {
            getScreen().setName(screenName);
        }
    }

    protected Map<String, Boolean> parseButtonStates(ILegacyAssignmentSpec spec) {
        Map<String, Boolean> states = new HashMap<>();
        String propValue = spec.getPropertyValue("buttonStates");
        if (propValue != null) {
            String stateString = propValue;
            String[] tokens = stateString.split(",");
            for (String token : tokens) {
                token = token.trim();
                String key = token.substring(0, token.indexOf("["));
                String value = token.substring(token.indexOf("[") + 1, token.indexOf("]"));
                states.put(key, new Boolean(value));
            }
        }
        return states;
    }

    protected  <A extends IUIAction> List<A> generateUIActionsForLocalNavButtons(Class<A> actionClass) {
        return generateUIActionsForLocalNavButtons(actionClass, true);
    }
    
    protected  <A extends IUIAction> List<A> generateUIActionsForLocalNavButtons(Class<A> actionClass, boolean filterDisabledButtons) {
        List<ILegacyButtonSpec> allLocalNavButtons = this.getPanelButtons(LOCAL_NAV_PANEL_KEY, Optional.of(filterDisabledButtons));
        ILegacyAssignmentSpec localNavPanel = this.getLegacyAssignmentSpec(LOCAL_NAV_PANEL_KEY);

        // Some Buttons/Options might be disabled dynamically.
        //POSBaseBeanModel posModel = (POSBaseBeanModel) this.legacyScreen.getModel();
        List<ILegacyButtonSpec> modifiedButtons = new ArrayList<>();
        ILegacyNavigationButtonBeanModel navigationButtonBeanModel = this.legacyPOSBeanService.getLegacyPOSBaseBeanModel(legacyScreen).getLegacyLocalButtonBeanModel();
        if (navigationButtonBeanModel != null) {
            if (navigationButtonBeanModel.getModifyButtons() != null) {
                modifiedButtons.addAll(Arrays.asList(navigationButtonBeanModel.getModifyButtons()));
            }
        }
        final List<A> generatedActions = new ArrayList<A>();
        allLocalNavButtons.stream()
            .forEachOrdered(localNavButton -> {
                ILegacyButtonSpec possibleModifiedSpec = modifiedButtons.stream().filter(
                    modifiedButton -> modifiedButton.getActionName().equals(localNavButton.getActionName())
                ).findFirst().orElse(localNavButton);
                
                // If the labelTag is null, don't add the button. (That seems to be the way some sites don't show buttons)
                String labelTag = localNavButton.getLabelTag();
                if (possibleModifiedSpec.getLabelTag() != null) {
                    labelTag = possibleModifiedSpec.getLabelTag();
                }
                Boolean enabledFlag = localNavButton.getEnabledFlag();
                if (possibleModifiedSpec.getEnabledFlag() != null) {
                    enabledFlag = possibleModifiedSpec.getEnabledFlag();
                }
                // Skip adding the button if the modified spec doesn't have a value for enabled and the label tag. (this seems
                // to be the way OrPOS sites omit buttons)
                if (possibleModifiedSpec.getEnabledFlag() != null || possibleModifiedSpec.getLabelTag() != null) {
                    String buttonText = this.getLegacyUtilityManager().retrieveText(localNavPanel.getBeanSpecName(), getResourceBundleFilename(), labelTag, labelTag);
                    A actionItem;
                    try {
                        actionItem = actionClass.newInstance();
                        actionItem.setTitle(buttonText);
                        actionItem.setEnabled(enabledFlag);
                        actionItem.setAction(possibleModifiedSpec.getActionName());
                        generatedActions.add(actionItem);
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error( String.format("Failed to create action of type %s for action '%s'", actionClass.getName(), possibleModifiedSpec.getActionName()), e);
                    }
                }
            }
        );

        return generatedActions;
        
    }
   
}
