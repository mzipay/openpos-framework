package org.jumpmind.jumppos.common;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FormatInfo {

    @Value("#{jumpProperties['tx.sequence.number.size']?:6}")
    private int transactionSequenceNumberSize;

    @Value("#{jumpProperties['store.id.size']?:5}")
    private int storeIdSize;

    @Value("#{jumpProperties['workstation.id.size']?:3}")
    private int workstationIdSize;

    @Value("#{jumpProperties['business.date.format']?:'yyyyMMdd'}")
    private String businessDateFormat;

    public String getBusinessDateFormat() {
        return businessDateFormat;
    }

    public int getStoreIdSize() {
        return storeIdSize;
    }

    public int getTransactionSequenceNumberSize() {
        return transactionSequenceNumberSize;
    }

    public int getWorkstationIdSize() {
        return workstationIdSize;
    }

    public String formatBusinessDate(Date date) {
        return FastDateFormat.getInstance(businessDateFormat).format(date);
    }

    public String formatSequenceNumber(int sequenceNumber) {
        return StringUtils.leftPad(Integer.toString(sequenceNumber), transactionSequenceNumberSize,
                "0");
    }

    public String formatStoreId(String storeId) {
        return StringUtils.leftPad(storeId, storeIdSize, "0");
    }

    public String formatWorkstationId(String workstationId) {
        return StringUtils.leftPad(workstationId, workstationIdSize, "0");
    }

}
