package org.jumpmind.pos.tax.model;

import java.util.Date;
import java.util.List;

public class TaxCalculationRequest {

    private List<TaxableItem> taxableItems;

    private Date calculateDate;

    private String taxCalculationGeocode;

    private String customerType;

    public TaxCalculationRequest() {
    }

    public TaxCalculationRequest(List<TaxableItem> taxableItems, Date calculateDate, String taxCalculationGeocode, String customerType) {
        this.taxableItems = taxableItems;
        this.calculateDate = calculateDate;
        this.taxCalculationGeocode = taxCalculationGeocode;
        this.customerType = customerType;
    }

    public List<TaxableItem> getTaxableItems() {
        return taxableItems;
    }

    public void setTaxableItems(List<TaxableItem> taxableItems) {
        this.taxableItems = taxableItems;
    }

    public Date getCalculateDate() {
        return calculateDate;
    }

    public void setCalculateDate(Date calculateDate) {
        this.calculateDate = calculateDate;
    }

    public String getTaxCalculationGeocode() {
        return taxCalculationGeocode;
    }

    public void setTaxCalculationGeocode(String taxCalculationGeocode) {
        this.taxCalculationGeocode = taxCalculationGeocode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

}
