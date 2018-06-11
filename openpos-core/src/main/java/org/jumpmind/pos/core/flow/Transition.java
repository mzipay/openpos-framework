package org.jumpmind.pos.core.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transition {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Logger logGraphical = LoggerFactory.getLogger(getClass().getName() + ".graphical");
    private final StateManagerLogger stateManagerLog = new StateManagerLogger(logGraphical);    
    
    private AtomicReference<CountDownLatch> latch = new AtomicReference<>(new CountDownLatch(1));

    private ITransitionStep currentTransitionStep;
    private List<? extends ITransitionStep> transitionSteps;
    private StateContext sourceStateContext;
    private IState targetState;
    private StateManager stateManager;
    private Action originalAction;
    private TransitionResult transitionResult;
    private int stepIndex;
    
    public Transition(List<? extends ITransitionStep> transitionSteps, StateContext sourceStateContext, IState targetState) {
        super();
        this.transitionSteps = cloneSteps(transitionSteps);
        this.sourceStateContext = sourceStateContext;
        this.targetState = targetState;
    }
    

    public TransitionResult execute(StateManager stateManager, Action originalAction) {
        this.stateManager = stateManager;
        this.originalAction = originalAction;
        currentTransitionStep = transitionSteps.get(0);
        stepIndex = 0;
        
        proceed();
        
        if (transitionResult == null) {
            throw new FlowException("Invalid: transitionResult must be non-null at this point.");
        }
        
        return transitionResult;
    }
    
    public void proceed() {
        if (stepIndex > 0) {
            CountDownLatch oldLatch = latch.get();
            latch.set(new CountDownLatch(1));
            oldLatch.countDown();
            stateManager.performOutjections(currentTransitionStep);
        }
        if (stepIndex >= transitionSteps.size()) {
            if (transitionResult == null) {                
                transitionResult = TransitionResult.PROCEED;
            }
            latch.get().countDown();
            return;
        }

        currentTransitionStep = transitionSteps.get(stepIndex++);
        
        stateManager.performInjections(currentTransitionStep);
        
        if (currentTransitionStep.isApplicable(this)) {
            stateManagerLog.logTranistionStep(this, currentTransitionStep);
            currentTransitionStep.arrive(this);
        } else {
            if (log.isDebugEnabled()) {                
                log.debug("TransitionStep" + currentTransitionStep + " was not applicable.");
            }
            proceed(); // recurse
        }
        
        try {
            latch.get().await();
        } catch (InterruptedException ex) {
            throw new FlowException("Transition await interupted.", ex);
        }        
    }
    
    public boolean handleAction(Action action) {
        return stateManager.handleAction(currentTransitionStep, action);
    }
    
    public void cancel() {
        log.info("Transition was canncelled by " + currentTransitionStep);
        latch.get().countDown();
        transitionResult = TransitionResult.CANCEL;
    }
    
    protected List<? extends ITransitionStep> cloneSteps(List<? extends ITransitionStep> steps) {
        List<ITransitionStep> clonedSteps = new ArrayList<>(steps.size());
        for (ITransitionStep step : steps) {
            try {                
                ITransitionStep clonedStep = step.getClass().newInstance();
                clonedSteps.add(clonedStep);
            } catch (Exception ex) {
                throw new FlowException("Failed to clone step " + step, ex);
            }
        }
        
        return clonedSteps;
    }    


    public ITransitionStep getCurrentTransitionStep() {
        return currentTransitionStep;
    }

    public List<? extends ITransitionStep> getTransitionSteps() {
        return transitionSteps;
    }

    public StateContext getSourceStateContext() {
        return sourceStateContext;
    }

    public IState getTargetState() {
        return targetState;
    }
    
    public Action getOriginalAction() {
        return originalAction;
    }

}
