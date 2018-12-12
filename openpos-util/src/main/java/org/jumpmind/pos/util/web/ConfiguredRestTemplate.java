package org.jumpmind.pos.util.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.jumpmind.pos.util.model.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ConfiguredRestTemplate extends RestTemplate {

    ObjectMapper mapper;
    
    public ConfiguredRestTemplate() {
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);       
        
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        mapper.registerModule(module);
        getMessageConverters().add(new MappingJackson2HttpMessageConverter(mapper));
        setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
                    ErrorResult result = mapper.convertValue(response.getBody(), ErrorResult.class);
                    Throwable serverError = result.getThrowable();
                    String serverMessage = result.getMessage();
                    if (serverError != null) {
                        throw new ServerException(serverMessage != null ? serverMessage : serverError.getMessage(), serverError);
                    } else if (serverMessage != null) {
                        throw new ServerException(serverMessage);
                    } else {
                        super.handleError(response);
                    }
                } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {

                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new NotFoundException();
                    } else {
                        super.handleError(response);
                    }

                }
            }
        });
    }
    
    public ObjectMapper getMapper() {
        return mapper;
    }

}
