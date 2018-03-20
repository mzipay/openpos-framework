package org.jumpmind.pos.app.state;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Arrays;

import org.jumpmind.pos.app.model.Login;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.screen.DialogScreen;
import org.jumpmind.pos.core.screen.IPromptScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;
import org.jumpmind.pos.user.model.AuthenticationResult;
import org.jumpmind.pos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractSecureState extends AbstractState implements IState {

    @Autowired(required=false)
    protected Login login;
    @Autowired
    private UserService userService;

    @Override
    public void arrive(Action action) {
        if (login == null) {
            login = new Login();
            stateManager.setSessionScope("login", login);
        }

        if (login.isLoggedIn()) {
            secureArrive();
        } else {
            promptForLogin();
        }
    }

    protected void promptForLogin() {
        if (login.getUserName() == null) {
            PromptScreen screen = new PromptScreen();
            screen.setRefreshAlways(true);
            screen.setResponseType(IPromptScreen.TYPE_ALPHANUMERICTEXT);
            screen.setBackButton(new MenuItem("Back", "Back", true));
            screen.setPromptIcon("lock");
            screen.setIcon("lock");
            screen.setName("Username");
            screen.setResponseText(login.getUserName());
            screen.setPlaceholderText("User ID");
            screen.setText("Type your User ID and press enter to continue.");
            stateManager.showScreen(screen);
        } else {
            PromptScreen screen = new PromptScreen();
            screen.setRefreshAlways(true);
            screen.setBackButton(new MenuItem("Back", "BackToUserPrompt", true));
            screen.setPromptIcon("lock");
            screen.setIcon("lock");
            screen.setName("Password");
            screen.setResponseType(IPromptScreen.TYPE_ALPHANUMERICPASSWORD);
            screen.setPlaceholderText("Password");
            screen.setText("Type your Password and press enter.");
            stateManager.showScreen(screen);
        }
    }

    abstract protected void secureArrive();

    @ActionHandler
    public void onNext(Action action) {
        if (!login.isLoggedIn()) {
            if (isBlank(login.getUserName())) {
                login.setUserName((String) action.getData());
                promptForLogin();
            } else {
                String password = (String) action.getData();
                AuthenticationResult result = userService.authenticate(login.getUserName(), password);
                if (result.getResultStatus().equals("SUCCESS")) {
                    login.setUser(result.getUser());
                    login.setLoggedIn(true);
                    secureArrive();                    
                } else {
                    login.setUserName(null);
                    login.setLoggedIn(false);
                    
                    DialogScreen screen = new DialogScreen();
                    screen.setTitle(result.getResultMessage());
                    screen.setRefreshAlways(true);
                    screen.setButtons(Arrays.asList(new MenuItem("Ok", "Acknowledge", true) ));
                    screen.setName("LoginFailed");
                    stateManager.showScreen(screen);
                }
            }
        }
    }
    
    @ActionHandler
    public void onAcknowledge(Action action) {
        promptForLogin();
    }

    @ActionHandler
    public void onBackToUserPrompt() {
        login.setUser(null);
        login.setUserName(null);
        promptForLogin();
    }

}
