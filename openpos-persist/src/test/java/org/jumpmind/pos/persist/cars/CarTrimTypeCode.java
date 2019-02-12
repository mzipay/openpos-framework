package org.jumpmind.pos.persist.cars;

import org.jumpmind.pos.util.model.AbstractTypeCode;

public class CarTrimTypeCode extends AbstractTypeCode {
    private static final long serialVersionUID = 1L;
    
    public static final CarTrimTypeCode EX = new CarTrimTypeCode("EX");
    public static final CarTrimTypeCode LX = new CarTrimTypeCode("LX");
    public static final CarTrimTypeCode SR = new CarTrimTypeCode("SR");
    
    public static CarTrimTypeCode of(String value) {
        return AbstractTypeCode.of(CarTrimTypeCode.class, value);
    }
    
    private CarTrimTypeCode(String value) {
        super(value);
    }
}
