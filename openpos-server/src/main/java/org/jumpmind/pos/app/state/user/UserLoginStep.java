package org.jumpmind.pos.app.state.user;

import static org.jumpmind.pos.context.model.TagModel.BRAND_ID_TAG;

import org.apache.commons.collections.CollectionUtils;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.InOut;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.core.flow.ui.PromptConfig;
import org.jumpmind.pos.core.screen.IPromptScreen;
import org.jumpmind.pos.i18n.service.i18nService;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.user.model.User;
import org.jumpmind.pos.user.service.AuthenticationResult;
import org.jumpmind.pos.user.service.UserMessage;
import org.jumpmind.pos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(150)
public class UserLoginStep implements ITransitionStep {

    @Autowired
    private UserService userService;

    @InOut(scope = ScopeType.Session, required=false)
    protected User currentUser;
    
    @In(scope = ScopeType.Node)
    protected IStateManager stateManager;    

    @In(scope = ScopeType.Node)
    private ContextServiceClient contextServiceClient;
    
    @In(scope = ScopeType.Node)
    private ICache i18nCache;
    
    @Autowired
    protected i18nService i18nService;    
    
    @In(scope = ScopeType.Node)
    protected DeviceModel node;    

    private String enteredUserName;
    private int userMessageIndex = 0;
    private AuthenticationResult result;
    private String oldPassword;
    private String newPassword1;
    
    protected Transition transition;
    
    public boolean isApplicable(Transition transition) {
        this.transition = transition;
        StatePermission statePermission = transition.getTargetState().getClass().getDeclaredAnnotation(StatePermission.class);
        if (statePermission != null && currentUser == null) {                
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void arrive(Transition transition) {
        promptForLogin();
    }

    protected void promptForLogin() {
        stateManager.getUI().prompt(new PromptConfig().placeholder(resource("_loginUserId")).promptText(resource("_loginUserPromptText"))
                .icon("lock").action(commonResource("_nextButton"), "UsernameEntered"));
    }

    protected void promptForPassword() {
        stateManager.getUI()
                .prompt(new PromptConfig().placeholder(resource("_loginPassword")).promptText(resource("_loginPasswordPrompt"))
                        .promptType(IPromptScreen.TYPE_ALPHANUMERICPASSWORD).icon("lock")
                        // .addOtherAction(new MenuItem("ForgotPassword",
                        // "Forgot Password"))
                        .action(commonResource("_nextButton"), "PasswordEntered").backAction("BackToUserPrompt"));

    }

    protected void processResult() {
        if (isResultSuccessful()) {
            onSuccessfulAuthentication();
        } else {
            if (CollectionUtils.isEmpty(result.getUserMessages())) {
                stateManager.getUI().notify(result.getResultMessage(), "FailureAcknowledged");
            }
        }
    }

    protected boolean isResultSuccessful() {
        return result.getResultStatus().equals("SUCCESS");
    }

    protected User getResultUser() {
        return result.getUser();
    }

    protected void onSuccessfulAuthentication() {
        this.currentUser = result.getUser();
        transition.proceed();
    }

    protected void showUserMessages() {
        if (CollectionUtils.isEmpty(result.getUserMessages()) || userMessageIndex >= result.getUserMessages().size()) {
            processResult();
            return;
        }

        UserMessage userMessage = result.getUserMessages().get(userMessageIndex++);
        switch (userMessage.getMessageCode()) {
            case "PASSWORD_EXPIRED":
            case "PASSWORD_EXPIRY_WARNING":
                stateManager.getUI().askYesNo(userMessage.getMessage() + " Would you like to change your password now?", "ChangePasswordYes",
                        "ChangePasswordNo");
                break;
        }
    }

    @ActionHandler
    public void onUsernameEntered(Action action) {
        enteredUserName = (String) action.getData();
        promptForPassword();
    }

    @ActionHandler
    public void onPasswordEntered(Action action) {
        String password = (String) action.getData();
        oldPassword = password;
        result = userService.authenticate(stateManager.getNodeId(), null, enteredUserName, password);
        userMessageIndex = 0;
        showUserMessages();
    }

    @ActionHandler
    public void onBackToUserPrompt(Action action) {
        promptForLogin();
    }

    @ActionHandler
    public void onBack(Action action) {
        transition.cancel();
    }

    @ActionHandler
    public void onFailureAcknowledged(Action action) {
        promptForLogin();
    }

    @ActionHandler
    public void onForgotPassword(Action action) {
        promptForLogin();
    }

    @ActionHandler
    public void onChangePasswordYes(Action action) {
        stateManager.getUI()
                .prompt(new PromptConfig().placeholder("New Password").promptText("Type your New Password and press enter.")
                        .promptType(IPromptScreen.TYPE_ALPHANUMERICPASSWORD).icon("lock").action("Next", "NewPassword1Entered")
                        .backAction("BackToUserPrompt"));
    }

    @ActionHandler
    public void onNewPassword1Entered(Action action) {
        newPassword1 = (String) action.getData();
        stateManager.getUI()
                .prompt(new PromptConfig().placeholder("New Password Again").promptText("Type your New Password again and press enter.")
                        .promptType(IPromptScreen.TYPE_ALPHANUMERICPASSWORD).icon("lock").action("Next", "NewPassword2Entered")
                        .backAction("BackToUserPrompt"));
    }

    @ActionHandler
    public void onNewPassword2Entered(Action action) {
        String newPassword2 = (String) action.getData();

        ServiceResult changePasswordResult = userService.changePassword(stateManager.getNodeId(), null, result.getUser().getUsername(),
                oldPassword, newPassword1, newPassword2);
        if (changePasswordResult.getResultStatus().equals("SUCCESS")) {
            stateManager.getUI().notify("Your password was changed succesful.", "PasswordChangeAcknowledged");
        } else {
            stateManager.getUI().notify(changePasswordResult.getResultMessage(), "ChangePasswordYes");
        }

    }

    @ActionHandler
    public void onPasswordChangeAcknowledged(Action action) {
        onSuccessfulAuthentication();
    }

    @ActionHandler
    public void onChangePasswordNo(Action action) {
        if (isResultSuccessful()) {
            processResult();
        } else {
            promptForLogin();
        }
    }

    @ActionHandler
    public void onMessageAcknowledged(Action action) {
        showUserMessages();
    }
    
    protected String resource(String key) {
        return i18nCache.getOrLoad(key, f -> {
            return i18nService.getString("user", key, node.getLocale(), node.getTagValue(BRAND_ID_TAG));
        });
    }

    protected String commonResource(String key) {
        return i18nCache.getOrLoad(key, f -> {
            return i18nService.getString("common", key, node.getLocale(), node.getTagValue(BRAND_ID_TAG));
        });        
    } 
}
