package org.jumpmind.pos.util;

import java.io.IOException;

import org.jumpmind.pos.util.model.ITypeCode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ITypeCodeSerializer extends StdSerializer<ITypeCode> {
    private static final long serialVersionUID = 1L;

    public ITypeCodeSerializer() {
        super(ITypeCode.class);
    }

    @Override
    public void serialize(ITypeCode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(new ITypeCodeWrapper(value));
    }
    
    /**
     * Wrapper class to allow for custom serializing/deserializing of ITypeCode
     * instances. Used in conjuction with Jackson JsonDeserialize and JsonSerialize
     * annotations found on ITypeCode interface. 
     */
    final static public class ITypeCodeWrapper {
        @JsonProperty
        String value;
        
        @JsonProperty("class")
        Class<? extends ITypeCode> clazz;
        
        @SuppressWarnings("unused")
        private ITypeCodeWrapper() {}
        
        ITypeCodeWrapper(ITypeCode typeCode) {
            this.value = typeCode != null ? typeCode.value() : null;
            this.clazz = typeCode != null ? typeCode.getClass() : null;
        }
    }
}
