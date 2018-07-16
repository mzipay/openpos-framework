package org.jumpmind.pos.trans.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.jumpmind.pos.trans.model.TransactionModel;
import org.jumpmind.pos.trans.model.TransactionQueueModel;
import org.jumpmind.pos.trans.model.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SaveTransactionQueueEndpoint {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TransactionRepository repository;

    ObjectMapper mapper;

    public SaveTransactionQueueEndpoint() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Async
    public void save(TransactionModel transaction) {        
        try {
            logger.info("Persisting transaction");
            TransactionQueueModel model = new TransactionQueueModel(transaction, mapper.writeValueAsString(transaction));
            repository.save(model);
        } catch (JsonProcessingException e) {
            logger.error("", e);
        }
    }
}
