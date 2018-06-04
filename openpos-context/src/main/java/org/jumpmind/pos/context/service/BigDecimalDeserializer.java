package org.jumpmind.pos.context.service;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DecimalNode;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, 
        JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (node instanceof DecimalNode) {
//            node.isFloatingPointNumber()
            int id = (Integer) ((DecimalNode) node.get("id")).numberValue();
        }
//        String itemName = node.get("itemName").asText();
        
        return null;
    }

}
