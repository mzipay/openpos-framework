package org.jumpmind.pos.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javax.annotation.PostConstruct
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.stereotype.Component

@Component
class MoneyExtentions {
  
    static def $(amount) {
        CurrencyUnit usd = CurrencyUnit.of("USD");
        org.joda.money.Money.of(usd, amount);
    }
    
    @PostConstruct
    initExtensions() {
        Money.metaClass.multiply { int value ->
            multipliedBy(value);
        }
        Money.metaClass.plus { BigDecimal amountToAdd ->
            plus(amountToAdd, java.math.RoundingMode.HALF_UP);    
        }
    } 
    
}

class MoneySerializer extends StdSerializer<Money> {
    MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}

class MoneyDeserializer extends StdDeserializer<Money> {
    MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.readValueAs(String.class);
        return Money.parse(value);
    }
}
