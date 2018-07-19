package org.jumpmind.pos.trans.model;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class BusinessDate extends Date {    

    private static final long serialVersionUID = 1L;
    
    static final FastDateFormat FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd");

    public BusinessDate(String businessDate) throws ParseException {
        super(FORMATTER.parse(businessDate).getTime());
    }
    
    public BusinessDate() {
    }
    
    @Override
    public String toString() {
        return toString(this);
    }
    
    public static String toString(Date date) {
        return FORMATTER.format(date);
    }
    
    public static BusinessDate toBusinessDate(String date) {
        try {
            return new BusinessDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
