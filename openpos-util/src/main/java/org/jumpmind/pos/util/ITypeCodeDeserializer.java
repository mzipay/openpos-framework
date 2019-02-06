package org.jumpmind.pos.util;

import java.io.IOException;

import org.jumpmind.pos.util.ITypeCodeSerializer.ITypeCodeWrapper;
import org.jumpmind.pos.util.model.ITypeCode;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ITypeCodeDeserializer extends StdDeserializer<ITypeCode> {
    
    private static final long serialVersionUID = 1L;

    public ITypeCodeDeserializer() {
        super(ITypeCode.class);
    }

    @Override
    public ITypeCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ITypeCodeWrapper wrapper = jp.readValueAs(ITypeCodeWrapper.class);
        return ITypeCode.make(wrapper.clazz, wrapper.value);
    }
}