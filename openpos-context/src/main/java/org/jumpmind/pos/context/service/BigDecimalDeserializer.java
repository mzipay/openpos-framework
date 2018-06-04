package org.jumpmind.pos.context.service;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, 
        JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (node instanceof DoubleNode) {
            DoubleNode doubleNode = (DoubleNode) node;
            Double n = (Double) doubleNode.numberValue();
            BigDecimal bd = new BigDecimal(n.toString());
            return bd;
        } else {            
            return null;
        }
    }

}
;