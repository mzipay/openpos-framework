package org.jumpmind.pos.app.state;

import java.util.Arrays;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.StateManager;
import org.jumpmind.pos.core.screen.DialogScreen;
import org.jumpmind.pos.core.screen.IPromptScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;
import org.jumpmind.pos.login.model.User;
import org.jumpmind.pos.user.model.AuthenticationResult;
import org.jumpmind.pos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserLoginState implements IState {

    @Autowired
    StateManager stateManager;
    @Autowired(required=false)
    User currentUser;
    @Autowired
    private UserService userService;    

    private String enteredUserName;

    private IState sourceState;
    private IState targetState;

    public UserLoginState(IState sourceState, IState targetState) {
        this.sourceState = sourceState;
        this.targetState = targetState;
    }

    @Override
    public void arrive(Action action) {
        promptForLogin();
    }

    protected void promptForLogin() {
        PromptScreen screen = new PromptScreen();
        screen.setRefreshAlways(true);
        screen.setResponseType(IPromptScreen.TYPE_ALPHANUMERICTEXT);
        screen.setBackButton(new MenuItem("Back", "Back", true));
        screen.setPromptIcon("lock");
        screen.setIcon("lock");
        screen.setName("Username");
        screen.setPlaceholderText("User ID");
        screen.setText("Type your User ID and press enter to continue.");
        screen.setAction("Next");
        screen.setActionButton(new MenuItem("Next", "UsernameEntered", true));
        stateManager.showScreen(screen);
    }


    @ActionHandler
    public void onUsernameEntered(Action action) {
        enteredUserName = (String) action.getData();
        promptForPassword();
    }

    protected void promptForPassword() {
        PromptScreen screen = new PromptScreen();
        screen.setRefreshAlways(true);
        screen.setBackButton(new MenuItem("Back", "BackToUserPrompt", true));
        screen.setPromptIcon("lock");
        screen.setIcon("lock");
        screen.setName("Password");
        screen.setResponseType(IPromptScreen.TYPE_ALPHANUMERICPASSWORD);
        screen.setPlaceholderText("Password");
        screen.setText("Type your Password and press enter.");
        screen.setActionButton(new MenuItem("Next", "PasswordEntered", true));
        stateManager.showScreen(screen);        
    }

    @ActionHandler
    public void onPasswordEntered(Action action) {
        String password = (String) action.getData();
        AuthenticationResult result = userService.authenticate(enteredUserName, password);
        if (result.getResultStatus().equals("SUCCESS")) {
            stateManager.setSessionScope("currentUser", result.getUser());
            stateManager.transitionTo(null, targetState);
        } else {
            DialogScreen screen = new DialogScreen();
            screen.setTitle(result.getResultMessage());
            screen.setRefreshAlways(true);
            screen.setButtons(Arrays.asList(new MenuItem("Ok", "FailuedAcknowledged", true) ));
            screen.setName("LoginFailed");
            stateManager.showScreen(screen);
        }
    }

    @ActionHandler
    public void onBackToUserPrompt(Action action) {
        promptForLogin();
    }

    @ActionHandler
    public void onBack(Action action) {
        stateManager.transitionTo(null, sourceState);
    }

    @ActionHandler
    public void onFailuedAcknowledged(Action action) {
        promptForLogin();
    }
}
