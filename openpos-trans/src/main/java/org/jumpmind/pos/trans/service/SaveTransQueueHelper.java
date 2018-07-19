package org.jumpmind.pos.trans.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.jumpmind.pos.trans.model.TransModel;
import org.jumpmind.pos.trans.model.TransQueueModel;
import org.jumpmind.pos.trans.model.TransRepository;
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
public class SaveTransQueueHelper {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TransRepository repository;

    ObjectMapper mapper;

    public SaveTransQueueHelper() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Async
    public void saveInBackground(TransModel transaction) {        
        this.save(transaction);
    }
    
    public void save(TransModel transaction) {        
        try {
            logger.info("Persisting transaction");
            TransQueueModel model = new TransQueueModel(transaction, mapper.writeValueAsString(transaction));
            repository.save(model);
        } catch (JsonProcessingException e) {
            logger.error("", e);
        }
    }

}
