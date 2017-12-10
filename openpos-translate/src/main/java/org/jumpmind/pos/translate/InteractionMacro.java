package org.jumpmind.pos.translate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InteractionMacro {

    private LinkedList<Object> interactionQueue = new LinkedList<>();

    public InteractionMacro sendLetter(String letter) {
        interactionQueue.add(new SendLetter(letter));
        return this;
    }

    public InteractionMacro waitForScreen(String screenName) {
        interactionQueue.add(new WaitForScreen(screenName));
        return this;
    }

    public InteractionMacro orThisScreen(String screenName) {
        if (interactionQueue.size() > 0 && interactionQueue.getLast() instanceof WaitForScreen) {
             ((WaitForScreen)interactionQueue.getLast()).screenNames.add(screenName);
        } else {
            throw new IllegalStateException("orThisScreen can only be called after waitForScreen");
        }
        return this;
    }

    public InteractionMacro doOnScreen(DoOnScreen action) {
        interactionQueue.add(action);
        return this;
    }

    public InteractionMacro doOnActiveScreen(DoOnActiveScreen action) {
        interactionQueue.add(action);
        return this;
    }
    
    public InteractionMacro checkForAbort(AbortMacro action) {
        interactionQueue.add(action);
        return this;
    }

    public InteractionMacro until(EvaluateScreenMacro action) {
        if (interactionQueue.size() > 0 && interactionQueue.getLast() instanceof WaitForScreen) {
            ((WaitForScreen)interactionQueue.getLast()).evaluator = Optional.ofNullable(action);
       } else {
           throw new IllegalStateException("until can only be called after waitForScreen");
       }
       return this;

    }
    
    public List<Object> getInteractionQueue() {
        return interactionQueue;
    }

    public interface DoOnScreen {
        void doOnScreen(ILegacyScreen screen);
    }

    public interface DoOnActiveScreen {
        void doOnScreen(ILegacyScreen screen);
    }
    
    public interface AbortMacro {
        boolean abort(ILegacyScreen screen);
    }
    
    public interface EvaluateScreenMacro {
        default void evaluate(ILegacyScreen currentScreen){};
        
        /** Skip to the next Macro */
        default boolean skip() {return false;}
        /** Go directly to the next Macro having a Class simpleName of that matches the returned String */
        default Optional<String> gotoNextOf() {return Optional.empty();}
        /** Throw the returned exception  */
        default Optional<Exception> throwException(){return Optional.empty();}
    }

    public class WaitForScreen {
        Set<String> screenNames = new HashSet<>();
        Optional<EvaluateScreenMacro> evaluator = Optional.empty();
        
        public WaitForScreen(String screenName) {
            screenNames.add(screenName);
        }
        
        public Set<String> getScreenNames() {
            return screenNames;
        }
    }

    public class SendLetter {
        String letter;

        public SendLetter(String letter) {
            this.letter = letter;
        }
        
        public String getLetter() {
            return letter;
        }
    }


}
