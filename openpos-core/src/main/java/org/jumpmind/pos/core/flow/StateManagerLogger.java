package org.jumpmind.pos.core.flow;

import static org.jumpmind.pos.util.BoxLogging.HORIZONTAL_LINE;
import static org.jumpmind.pos.util.BoxLogging.HORIZONTAL_MIDDLE;
import static org.jumpmind.pos.util.BoxLogging.LOWER_LEFT_CORNER;
import static org.jumpmind.pos.util.BoxLogging.LOWER_RIGHT_CORNER;
import static org.jumpmind.pos.util.BoxLogging.UPPER_LEFT_CORNER;
import static org.jumpmind.pos.util.BoxLogging.UPPER_RIGHT_CORNER;
import static org.jumpmind.pos.util.BoxLogging.VERITCAL_LINE;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.flow.config.SubTransition;
import org.jumpmind.pos.server.model.Action;
import org.jumpmind.pos.util.BoxLogging;
import org.slf4j.Logger;

public class StateManagerLogger {
    
    final Logger log;
    
    private final String ENTER_SUBSTATE = "<SubState>";
    private final String EXIT_SUBSTATE = "<Exit SubState>";
    
    public StateManagerLogger(Logger log) {
        this.log = log;
    }
    
    protected void logStateTransition(IState oldState, IState newState, Action action, String returnAction, boolean enterSubState, boolean exitSubState) {
        if (oldState == newState) {
            return;
        }
        if (log.isInfoEnabled()) {            
            String oldStateName = oldState != null ? oldState.getClass().getSimpleName() : "<no state>";
            String newStateName = newState.getClass().getSimpleName();
            int box1Width = Math.max(oldStateName.length()+2, 20);
            int box2Width = Math.max(newStateName.length()+2, 20);
            
            String primaryAction = null;
            String secondaryAction = null;
            if (returnAction == null) {
                primaryAction = action != null ? action.getName() : "";
                secondaryAction = "";
            } else {
                primaryAction = returnAction;
                secondaryAction = "(" + (action != null ? action.getName() : "") + ")";
            }
            
            int inbetweenWidth = Math.max(primaryAction.length()+2, 10);
            inbetweenWidth = Math.max(secondaryAction.length()+2, inbetweenWidth);
            if (enterSubState) {
                inbetweenWidth = Math.max(ENTER_SUBSTATE.length()+2, inbetweenWidth);
            } else if (exitSubState) {
                inbetweenWidth = Math.max(EXIT_SUBSTATE.length()+2, inbetweenWidth);
            }
            
            StringBuilder buff = new StringBuilder(256);
            
            int LINE_COUNT = 5;
            for (int i = 0; i < LINE_COUNT; i++) {
                switch (i) {
                    case 0:
                        buff.append(drawTop(box1Width, box2Width, inbetweenWidth));
                        break;
                    case 1:
                        buff.append(drawFillerLine(box1Width, box2Width, inbetweenWidth, enterSubState, exitSubState));
                        break;
                    case 2:
                        buff.append(drawTitleLine(box1Width, box2Width,inbetweenWidth, oldStateName, newStateName));
                        break;                    
                    case 3:
                        buff.append(drawEventLine(box1Width, box2Width,inbetweenWidth, primaryAction));
                        break;
                    case 4:
                        buff.append(drawBottom(box1Width, box2Width, inbetweenWidth, secondaryAction));
                        break;                    
                        
                }
            }
            
            log.info("Transition from " + oldState + " to " + newState + "\n" + buff.toString());
        } else {
            log.info("Transition from " + oldState + " to " + newState);
        }
    }
    
    public void logTranistionStep(Transition transition, ITransitionStep currentTransitionStep) {
        
        String stepName = currentTransitionStep.getClass().getSimpleName();
        String stepTitle = "Step: " + stepName;
        
        String boxed = BoxLogging.box(stepTitle);
        
        String fromStateName = transition.getSourceStateContext().getState() != null ? transition.getSourceStateContext().getState().getClass().getSimpleName() : "<no state>"; 
        String toStateName = transition.getTargetState() != null ? transition.getTargetState().getClass().getSimpleName() : "<no state>"; 
        
        log.info("Transition step [" + stepName + "] running between " + 
                fromStateName + " and " + toStateName + "\r\n" + boxed);
    }


    protected String drawTop(int box1Width, int box2Width, int inbetweenWidth) {
        StringBuilder buff = new StringBuilder();
        
        buff.append(UPPER_LEFT_CORNER).append(StringUtils.repeat(HORIZONTAL_LINE, box1Width-2)).append(UPPER_RIGHT_CORNER);
        buff.append(StringUtils.repeat(' ', inbetweenWidth));
        buff.append(UPPER_LEFT_CORNER).append(StringUtils.repeat(HORIZONTAL_LINE, box2Width-2)).append(UPPER_RIGHT_CORNER);
        buff.append("\r\n");
        return buff.toString();
    }
    
    protected String drawFillerLine(int box1Width, int box2Width, int inbetweenWidth, boolean enterSubState, boolean exitSubState) {
        StringBuilder buff = new StringBuilder();
        
        buff.append(VERITCAL_LINE).append(StringUtils.repeat(' ', box1Width-2)).append(VERITCAL_LINE);
        
        if (enterSubState) {
            buff.append(StringUtils.center(ENTER_SUBSTATE, inbetweenWidth));
        } else if (exitSubState) {
            buff.append(StringUtils.center(EXIT_SUBSTATE, inbetweenWidth));
        } else {            
            buff.append(StringUtils.center("", inbetweenWidth));
        }
        
        buff.append(VERITCAL_LINE).append(StringUtils.repeat(' ', box2Width-2)).append(VERITCAL_LINE);            
        buff.append("\r\n");
        
        return buff.toString();
    }
    
    protected String drawEventLine(int box1Width, int box2Width, int inbetweenWidth, String actionName) {
        StringBuilder buff = new StringBuilder();
        
        buff.append(VERITCAL_LINE).append(StringUtils.repeat(' ', box1Width-2)).append(VERITCAL_LINE);
        buff.append(StringUtils.center(actionName, inbetweenWidth));
        buff.append(VERITCAL_LINE).append(StringUtils.repeat(' ', box2Width-2)).append(VERITCAL_LINE);
        buff.append("\r\n");
        return buff.toString();
    }
    
    
    protected String drawTitleLine(int box1Width, int box2Width, int inbetweenWidth, String oldStateName, String newStateName) {
        StringBuilder buff = new StringBuilder();
        buff.append(VERITCAL_LINE).append(StringUtils.center(oldStateName, box1Width-2)).append(VERITCAL_LINE);
        buff.append(" ").append(StringUtils.repeat(HORIZONTAL_MIDDLE, inbetweenWidth-3)).append("> ");
        buff.append(VERITCAL_LINE).append(StringUtils.center(newStateName, box2Width-2)).append(VERITCAL_LINE);
        buff.append("\r\n");
        return buff.toString();
    }
    
    protected String drawBottom(int box1Width, int box2Width, int inbetweenWidth, String secondaryAction) {
        StringBuilder buff = new StringBuilder();
        
        buff.append(LOWER_LEFT_CORNER).append(StringUtils.repeat(HORIZONTAL_LINE, box1Width-2)).append(LOWER_RIGHT_CORNER);
        buff.append(StringUtils.center(secondaryAction, inbetweenWidth));
        buff.append(LOWER_LEFT_CORNER).append(StringUtils.repeat(HORIZONTAL_LINE, box2Width-2)).append(LOWER_RIGHT_CORNER);
        buff.append("\r\n");
        return buff.toString();
    }


}
