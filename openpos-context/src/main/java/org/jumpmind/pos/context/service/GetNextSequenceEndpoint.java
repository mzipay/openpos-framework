package org.jumpmind.pos.context.service;

import org.jumpmind.pos.context.model.ContextRepository;
import org.jumpmind.pos.context.model.SequenceModel;
import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional(transactionManager = "contextTxManager")
public class GetNextSequenceEndpoint {

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    Environment env;

    @Autowired
    ContextService contextService;

    @Endpoint("/sequence/{name}/next")
    public Long getNextSequence(@RequestParam(value = "name") String name) {
        long sequenceTimeoutInMs = Long.parseLong(env.getProperty("sequence.timeout.ms", "5000"));
        long ts = System.currentTimeMillis();
        do {
            long nextVal = tryToGetNextVal(name);
            if (nextVal > 0) {
                return nextVal;
            }
        } while (System.currentTimeMillis() - sequenceTimeoutInMs < ts);

        throw new IllegalStateException(
                String.format("Timed out after %d ms trying to get the next val for %s", System.currentTimeMillis() - ts, name));

    }

    public SequenceModel currVal(String name) {
        SequenceModel sequence = contextRepository.findCurrentSequence(name);
        if (sequence == null) {
            sequence = new SequenceModel();
            sequence.setSequenceName(name);
            sequence.setCurrentValue(1l);
            contextRepository.saveSequence(sequence);
        }
        return sequence;
    }

    protected long tryToGetNextVal(String name) {
        SequenceModel currVal = currVal(name);
        long nextVal = currVal.getCurrentValue() + currVal.getIncrementBy();
        if (nextVal > currVal.getMaxValue()) {
            if (currVal.isCycleFlag()) {
                nextVal = currVal.getMinValue();
            } else {
                throw new IllegalStateException(
                        String.format("The sequence named %s has reached it's max value.  " + "No more numbers can be handled out.", name));
            }
        } else if (currVal.getCurrentValue() < currVal.getMinValue()) {
            if (currVal.isCycleFlag()) {
                nextVal = currVal.getMaxValue();
            } else {
                throw new IllegalStateException(
                        String.format("The sequence named %s has reached it's min value.  " + "No more numbers can be handled out.", name));
            }
        }

        if (contextRepository.allocateNextSequence(name, nextVal, currVal.getCurrentValue())) {
            return nextVal;
        } else {
            return -1;
        }
    }
}
